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

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String username;
    private String email;
    private List<Tweet> tweets;
    private List<ReTweet> retweets;

    static final String ERROR_USERNAME_EMPTY = "El nombre no puede estar vacío";
    static final String ERROR_USERNAME_SHORT = "El nombre debe tener más de 5 caracteres";
    static final String ERROR_USERNAME_LONG = "El nombre no puede tener más de 25 caracteres";
    static final String ERROR_EMAIL_EMPTY = "El correo electrónico no puede estar vacío";
    static final String ERROR_TWEET_DUPLICATE = "El tweet ya existe";
    static final String ERROR_RETWEET_DUPLICATE = "El retweet ya existe";

    public User (String username, String email) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException(ERROR_USERNAME_EMPTY);
        }
        if (username.length() < 5) {
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

    public void createTweet(String text) {
        Tweet tweet = new Tweet(text, this);
        if (tweets.contains(tweet)) {
            throw new RuntimeException(ERROR_TWEET_DUPLICATE);
        }
        tweets.add(tweet);
    }

    //--un usuario no puede retuitear su propio tweet
    public void createRetweet(Tweet tweet) {
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
