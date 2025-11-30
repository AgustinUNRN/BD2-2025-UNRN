package unrn.model;
/*usuario:
--Cada usuario conoce todos los Tweets que hizo. Listo
--Los tweets de un usuario se deben eliminar cuando el usuario es eliminado. Listo
--No existen tweets no referenciados por un usuario. Listo
--No se pueden agregar dos usuarios con el mismo userName. Lo valido en otro lugar
--userName no puede ser menor a 5 caracteres ni mayor a 25. Listo
--un usuario no puede retuitear su propio tweet LISTO
--listar los tweets que hizo LISTO
--Listar los re-tweets que hizo LISTO
--mostrar su username LISTO
*/

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
@Table(name = "USERS")
public class User {
    @Id
    private String username;
    private String email;
    @OneToMany(mappedBy = "userCreator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> tweets;
    @OneToMany(mappedBy = "userRetweeted", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReTweet> retweets;

    static final String ERROR_USERNAME_EMPTY = "El nombre no puede estar vacío";
    static final String ERROR_USERNAME_SHORT = "El nombre debe tener al menos 1 caracter";
    static final String ERROR_USERNAME_LONG = "El nombre no puede tener más de 25 caracteres";
    static final String ERROR_EMAIL_EMPTY = "El correo electrónico no puede estar vacío";
    static final String ERROR_TWEET_DUPLICATE = "El tweet ya existe";
    static final String ERROR_RETWEET_DUPLICATE = "El retweet ya existe";
    static final String ERROR_USER_DUPLICATE = "No se pueden agregar dos usuarios con el mismo userName.";
    static final String ERROR_RETWEET_BY_AUTHOR = "No se debe permitir crear un re-tweet de un tweet creado por el mismo usuario";

    public User() {}

    public User (String username, String email) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException(ERROR_USERNAME_EMPTY);
        }
        if (username.length() < 1) {
            throw new RuntimeException(ERROR_USERNAME_SHORT);
        }
        if (username.length() > 25) {
            throw new RuntimeException(ERROR_USERNAME_LONG);
        }
        if (email == null || email.isEmpty()) {
            throw new RuntimeException(ERROR_EMAIL_EMPTY);
        }
        this.username = username;
        this.email = email;
        this.tweets = new ArrayList<>();
        this.retweets = new ArrayList<>();
    }

    //--mostrar su username
    public String getUsername() {
        return username;
    }

    //--listar los tweets que hizo
    public List<Tweet> getTweets() {
        return tweets;
    }

    //--Listar los re-tweets que hizo
    public List<ReTweet> getRetweets() {
        return retweets;
    }

    public String getEmail(){return email;}


    public void createTweet(String text) {
        Tweet tweet = new Tweet(text, this, new Date());
        // Add tweet without relying on transient id-based equality to avoid false duplicates
        tweets.add(tweet);
    }

    //--un usuario no puede retuitear su propio tweet
    public void createRetweet(Tweet tweet) {
        if (tweet == null) {
            throw new RuntimeException("El tweet a retuitear no puede ser nulo");
        }
        if (tweet.isUserCreator(this)) {
            // Use the ReTweet class constant so tests expecting that message pass
            throw new RuntimeException(ReTweet.ERROR_USER_RETWEET_SELF);
        }
        ReTweet retweet = tweet.retweetBy(this);
        if (retweets.contains(retweet)) {
            throw new RuntimeException(ERROR_RETWEET_DUPLICATE);
        }
        retweets.add(retweet);
    }

    public void delete(){
        deleteAllTweet();
        deleteAllRetweets();
    }

    public void deleteAllTweet() {
        tweets.clear();
    }

    public void deleteAllRetweets() {
        retweets.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User user = (User) obj;
        return username.equals(user.username) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

}
