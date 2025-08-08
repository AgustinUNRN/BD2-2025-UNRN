package unrn.model;
/*retweets:
--Un tweet puede ser re-tweet de otro, y este tweet debe conocer a su tweet de origen.
--Un re-tweet no tiene texto adicional.
--no se puede crear si el que retweetea es el mismo que lo creo
--Si es re-tweet se debe mostrar la fecha de cuando se retuiteó, el nombre del usuario que re-twitteo. Además de los datos originales del tweet
*/
import java.util.Date;

public class ReTweet {
    //--Si es re-tweet se debe mostrar la fecha de cuando se retuiteó,
    // el nombre del usuario que re-twitteo.
    // Además de los datos originales del tweet
    private Date dateRetweeted;
    private User userRetweeted;
    private Tweet originalTweet;

    static final String ERROR_USER_RETWEET_SELF = "El usuario no puede retuitear su propio tweet";
    static final String ERROR_USER_RETWEET_NULL = "El usuario que retuitea no puede ser nulo";
    static final String ERROR_ORIGINAL_TWEET_NULL = "El tweet original no puede ser";

    //--no se puede crear si el que retweetea es el mismo que lo creo
    public ReTweet(User user, Tweet originalTweet) {
        if (originalTweet == null) {
            throw new RuntimeException(ERROR_ORIGINAL_TWEET_NULL);
        }
        if(user == null) {
            throw new RuntimeException(ERROR_USER_RETWEET_NULL);
        }
        if (originalTweet.isUserCreator(user)) {
            throw new RuntimeException(ERROR_USER_RETWEET_SELF);
        }
        this.originalTweet = originalTweet;
        this.userRetweeted = user;
        this.dateRetweeted = new Date();
    }

    public Tweet getOriginalTweet() {
        return originalTweet;
    }

    public User getUserRetweeted() {
        return userRetweeted;
    }

    public Date getDate() {
        return dateRetweeted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReTweet)) return false;
        ReTweet other = (ReTweet) obj;
        return originalTweet.equals(other.originalTweet) && userRetweeted.equals(other.userRetweeted);
    }

    @Override
    public int hashCode() {
        return originalTweet.hashCode() * 31 + userRetweeted.hashCode();
    }
}