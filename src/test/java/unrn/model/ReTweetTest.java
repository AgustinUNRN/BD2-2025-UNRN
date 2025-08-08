package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReTweetTest {

    @Test
    @DisplayName("ReTweet se crea correctamente con usuario y tweet original válidos")
    void constructor_creaReTweetCorrectamente() {
        // Setup
        User creador = new User("usuario1", "mail@mail.com");
        User retweeter = new User("usuario2", "mail2@mail.com");
        Tweet tweet = new Tweet("Hola", creador);
        // Ejercitación
        ReTweet retweet = new ReTweet(retweeter, tweet);
        // Verificación
        assertEquals(tweet, retweet.getOriginalTweet(), "El retweet debe referenciar al tweet original");
        assertEquals(retweeter, retweet.getUserRetweeted(), "El usuario debe ser el que retuiteó");
        assertNotNull(retweet.getDate(), "La fecha de retweet no debe ser nula");
    }

    @Test
    @DisplayName("ReTweet lanza excepción si el tweet original es nulo")
    void constructor_lanzaExcepcionSiTweetOriginalNulo() {
        // Setup
        User retweeter = new User("usuario2", "mail2@mail.com");
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new ReTweet(retweeter, null));
        assertEquals(ReTweet.ERROR_ORIGINAL_TWEET_NULL, ex.getMessage(), "Debe lanzar excepción por tweet original nulo");
    }

    @Test
    @DisplayName("ReTweet lanza excepción si el usuario es nulo")
    void constructor_lanzaExcepcionSiUsuarioNulo() {
        // Setup
        User creador = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("Hola", creador);
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new ReTweet(null, tweet));
        assertEquals(ReTweet.ERROR_USER_RETWEET_NULL, ex.getMessage(), "Debe lanzar excepción por usuario nulo");
    }

    @Test
    @DisplayName("ReTweet lanza excepción si el usuario es el creador del tweet original")
    void constructor_lanzaExcepcionSiUsuarioEsCreador() {
        // Setup
        User creador = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("Hola", creador);
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new ReTweet(creador, tweet));
        assertEquals(ReTweet.ERROR_USER_RETWEET_SELF, ex.getMessage(), "Debe lanzar excepción por retuitear su propio tweet");
    }
}

