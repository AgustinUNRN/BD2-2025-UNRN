package unrn.model;
/*tweets:
--No existen tweets no referenciados por un usuario.
--minimo 1 caracter y maximo 280
--Cada tweet debe mostrar el nombre del usuario, el texto del tweet y la fecha de creación.
--Al clickear en crear nuevo tweet, aparecerá en el panel principal un formulario con los siguientes inputs:
Para cargar el userid del creador del tweet (dado que no hay que implementar autenticación)
Para cargar el texto del tweet
Botón para crear el tweet.*/
import java.util.Date;
import java.util.UUID;

public class Tweet {
    //--Cada tweet debe mostrar el nombre del usuario, el texto del tweet y la fecha de creación.
    private String id;
    private String text;
    private User userCreator; //--No existen tweets no referenciados por un usuario.
    private Date dateCreated;
//    private List<User> favorite; // hace falta?
//    private List<User> retweet; // hace falta?

    static final String ERROR_TWEET_TEXT_LENGTH = "El texto del tweet debe tener entre 1 y 280 caracteres";
    static final String ERROR_USER_CREATOR_NULL = "El creador del tweet no puede ser nulo";
    static final String ERROR_USER_RETWEET_SELF = "El usuario no puede retuitear su propio tweet";
    static final String ERROR_USER_RETWEETED_NULL = "El usuario que retuitea no puede ser nulo";

    public Tweet(String id, String text, User userCreator) {
        if (id == null || id.isEmpty()) {
            throw new RuntimeException("El id no puede ser nulo o vacío");
        }
        if (text == null || text.length() < 1 || text.length() > 280) {
            throw new RuntimeException(ERROR_TWEET_TEXT_LENGTH);
        }
        if (userCreator == null) {
            throw new RuntimeException(ERROR_USER_CREATOR_NULL);
        }
        this.id = id;
        this.text = text;
        this.userCreator = userCreator;
        this.dateCreated = new Date();
    }

    public Tweet(String text, User userCreator) {
        if (text == null || text.length() < 1 || text.length() > 280) {
            throw new RuntimeException(ERROR_TWEET_TEXT_LENGTH);
        }
        if (userCreator == null) {
            throw new RuntimeException(ERROR_USER_CREATOR_NULL);
        }
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.userCreator = userCreator;
        this.dateCreated = new Date();
    }

    public String getId() {
        return id;
    }
    public String getText() {
        return text;
    }

    public boolean isUserCreator(User user) {
        return userCreator.equals(user);
    }

    public String getUserCreatorUsername() {
        return userCreator.getUsername();
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public ReTweet retweetBy(User userRetweeted) {
        if (userRetweeted == null) {
            throw new RuntimeException(ERROR_USER_RETWEETED_NULL);
        }
        if (this.isUserCreator(userRetweeted)) {
            throw new RuntimeException(ERROR_USER_RETWEET_SELF);
        }
        return new ReTweet(userRetweeted, this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tweet)) return false;
        Tweet other = (Tweet) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
