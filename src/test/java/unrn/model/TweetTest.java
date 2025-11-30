package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class TweetTest {

    @Test
    @DisplayName("Tweet se crea correctamente con texto válido y usuario válido")
    void constructor_creaTweetCorrectamente() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        String texto = "Hola mundo";
        // Ejercitación
        Tweet tweet = new Tweet(texto, user, new Date());
        // Verificación
        assertEquals(texto, tweet.getText(), "El texto del tweet debe coincidir");
        assertTrue(tweet.isUserCreator(user), "El usuario debe ser el creador del tweet");
        assertNotNull(tweet.getDateCreated(), "La fecha de creación no debe ser nula");
    }

    @Test
    @DisplayName("Tweet lanza excepción si el texto es nulo o vacío")
    void constructor_lanzaExcepcionSiTextoInvalido() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        // Ejercitación y Verificación
        var ex1 = assertThrows(RuntimeException.class, () -> new Tweet(null, user, new Date()));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex1.getMessage(), "Debe lanzar excepción por texto nulo");
        var ex2 = assertThrows(RuntimeException.class, () -> new Tweet("", user, new Date()));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex2.getMessage(), "Debe lanzar excepción por texto vacío");
    }

    @Test
    @DisplayName("Tweet lanza excepción si el texto supera los 280 caracteres")
    void constructor_lanzaExcepcionSiTextoMuyLargo() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        String textoLargo = "a".repeat(281);
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new Tweet(textoLargo, user, new Date()));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex.getMessage(), "Debe lanzar excepción por texto demasiado largo");
    }

    @Test
    @DisplayName("Tweet lanza excepción si el usuario es nulo")
    void constructor_lanzaExcepcionSiUsuarioNulo() {
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new Tweet("Hola", null, new Date()));
        assertEquals(Tweet.ERROR_USER_CREATOR_NULL, ex.getMessage(), "Debe lanzar excepción por usuario nulo");
    }

    @Test
    @DisplayName("retweetBy crea un retweet correctamente si el usuario es válido y no es el creador")
    void retweetBy_creaRetweetCorrectamente() {
        // Setup
        User creador = new User("usuario1", "mail@mail.com");
        User retweeter = new User("usuario2", "mail2@mail.com");
        Tweet tweet = new Tweet("Hola", creador, new Date());
        // Ejercitación
        ReTweet retweet = tweet.retweetBy(retweeter);
        // Verificación
        assertEquals(tweet, retweet.getOriginalTweet(), "El retweet debe referenciar al tweet original");
        assertEquals(retweeter, retweet.getUserRetweeted(), "El usuario debe ser el que retuiteó");
        assertNotNull(retweet.getDate(), "La fecha de retweet no debe ser nula");
    }

    @Test
    @DisplayName("retweetBy lanza excepción si el usuario es nulo")
    void retweetBy_lanzaExcepcionSiUsuarioNulo() {
        // Setup
        User creador = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("Hola", creador,new Date());
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> tweet.retweetBy(null));
        assertEquals(Tweet.ERROR_USER_RETWEETED_NULL, ex.getMessage(), "Debe lanzar excepción por usuario nulo");
    }

    @Test
    @DisplayName("retweetBy lanza excepción si el usuario es el creador del tweet")
    void retweetBy_lanzaExcepcionSiUsuarioEsCreador() {
        // Setup
        User creador = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("Hola", creador, new Date());
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> tweet.retweetBy(creador));
        assertEquals(Tweet.ERROR_USER_RETWEET_SELF, ex.getMessage(), "Debe lanzar excepción por retuitear su propio tweet");
    }

    @Test
    @DisplayName("getUserCreatorUsername retorna el username del creador correctamente")
    void getUserCreatorUsername_retornaUsernameCorrectamente() {
        // Setup: Crear usuario y tweet
        User user = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("Hola mundo", user, new Date());
        // Ejercitación: Obtener username del creador
        String username = tweet.getUserCreatorUsername();
        // Verificación: El username debe coincidir con el del usuario
        assertEquals("usuario1", username, "El username retornado debe ser el del creador del tweet");
    }

    @Test
    @DisplayName("Tweet con el mismo id son iguales, con distinto id son distintos")
    void equals_y_hashCode_funcionanPorId() {
        User user = new User("usuario1", "mail@mail.com");
        int id = 123;
        Tweet t1 = new Tweet(id, "texto", user, new Date());
        Tweet t2 = new Tweet(id, "otro texto", user, new Date());
        Tweet t3 = new Tweet(321, "texto", user, new Date());
        assertEquals(t1, t2, "Tweets con el mismo id deben ser iguales");
        assertEquals(t1.hashCode(), t2.hashCode(), "Tweets con el mismo id deben tener el mismo hashCode");
        assertNotEquals(t1, t3, "Tweets con distinto id deben ser distintos");
    }

    @Test
    @DisplayName("getId retorna el id correctamente")
    void getId_retornaIdCorrectamente() {
        User user = new User("usuario1", "mail@mail.com");
        int id = 456;
        Tweet tweet = new Tweet(id, "texto", user, new Date());
        assertEquals(id, tweet.getId(), "El id retornado debe ser el mismo que el asignado");
    }

    @Test
    @DisplayName("ReTweets con mismo tweet de origen y usuario son iguales, si cambia alguno son distintos")
    void equals_y_hashCode_funcionanPorTweetYUsuario() {
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario2", "mail2@mail.com");
        Tweet tweet1 = new Tweet(1, "texto", user1, new Date());
        Tweet tweet2 = new Tweet(1, "otro texto", user1, new Date());
        ReTweet r1 = new ReTweet(user2, tweet1);
        ReTweet r2 = new ReTweet(user2, tweet2);
        Tweet tweet3 = new Tweet(2, "texto", user1, new Date());
        ReTweet r3 = new ReTweet(user2, tweet3);
        // No se prueba retweet propio porque lanza excepción
        assertEquals(r1, r2, "ReTweets con mismo tweet de origen y usuario deben ser iguales");
        assertEquals(r1.hashCode(), r2.hashCode(), "ReTweets con mismo tweet de origen y usuario deben tener el mismo hashCode");
        assertNotEquals(r1, r3, "ReTweets con distinto tweet de origen deben ser distintos");
    }

    @Test
    @DisplayName("Tweet lanza excepción si el texto es nulo usando constructor con id")
    void constructorConId_lanzaExcepcionSiTextoNulo() {
        User user = new User("usuario1", "mail@mail.com");
        var ex = assertThrows(RuntimeException.class, () -> new Tweet(1, null, user, new Date()));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex.getMessage());
    }

    @Test
    @DisplayName("Tweet lanza excepción si el texto es vacío usando constructor con id")
    void constructorConId_lanzaExcepcionSiTextoVacio() {
        User user = new User("usuario1", "mail@mail.com");
        var ex = assertThrows(RuntimeException.class, () -> new Tweet(1, "", user, new Date()));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex.getMessage());
    }

    @Test
    @DisplayName("Tweet lanza excepción si el texto es muy largo usando constructor con id")
    void constructorConId_lanzaExcepcionSiTextoMuyLargo() {
        User user = new User("usuario1", "mail@mail.com");
        String textoLargo = "a".repeat(281);
        var ex = assertThrows(RuntimeException.class, () -> new Tweet(1, textoLargo, user, new Date()));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex.getMessage());
    }

    @Test
    @DisplayName("Tweet lanza excepción si el usuario es nulo usando constructor con id")
    void constructorConId_lanzaExcepcionSiUsuarioNulo() {
        var ex = assertThrows(RuntimeException.class, () -> new Tweet(1, "texto", null, new Date()));
        assertEquals(Tweet.ERROR_USER_CREATOR_NULL, ex.getMessage());
    }

    @Test
    @DisplayName("Tweet acepta texto de longitud 1")
    void constructor_acceptsMinLengthOne() {
        User user = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("a", user, new Date());
        assertEquals("a", tweet.getText());
    }

    @Test
    @DisplayName("Tweet acepta texto de longitud 280")
    void constructor_acceptsMaxLengthTwoHundredEighty() {
        User user = new User("usuario1", "mail@mail.com");
        String texto = "a".repeat(280);
        Tweet tweet = new Tweet(texto, user, new Date());
        assertEquals(texto, tweet.getText());
    }
}
