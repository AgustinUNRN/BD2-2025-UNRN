package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("createTweet agrega un tweet correctamente a la lista de tweets del usuario")
    void createTweet_agregaTweetCorrectamente() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        String texto = "Hola mundo";
        // Ejercitación
        user.createTweet(texto);
        // Verificación
        List<Tweet> tweets = user.getTweets();
        assertEquals(1, tweets.size(), "El usuario debe tener un tweet en la lista");
        assertEquals(texto, tweets.get(0).getText(), "El texto del tweet no coincide");
        assertEquals(user, tweets.get(0).isUserCreator(user) ? user : null, "El usuario debe ser el creador del tweet");
    }

    @Test
    @DisplayName("createRetweet agrega un retweet correctamente a la lista de retweets del usuario")
    void createRetweet_agregaRetweetCorrectamente() {
        // Setup
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario2", "mail2@mail.com");
        user1.createTweet("Tweet original");
        Tweet tweet = user1.getTweets().get(0);
        // Ejercitación
        user2.createRetweet(tweet);
        // Verificación
        List<ReTweet> retweets = user2.getRetweets();
        assertEquals(1, retweets.size(), "El usuario debe tener un retweet en la lista");
        assertEquals(tweet, retweets.get(0).getOriginalTweet(), "El retweet debe referenciar al tweet original");
        assertEquals(user2, retweets.get(0).getUserRetweeted(), "El usuario debe ser el que retuiteó");
    }

    @Test
    @DisplayName("createRetweet lanza excepción si el usuario intenta retuitear su propio tweet")
    void createRetweet_lanzaExcepcionSiRetuiteaPropioTweet() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        user.createTweet("Tweet propio");
        Tweet tweet = user.getTweets().get(0);
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> user.createRetweet(tweet));
        assertEquals(ReTweet.ERROR_USER_RETWEET_SELF, ex.getMessage(), "Debe lanzar excepción por retuitear su propio tweet");
    }

    @Test
    @DisplayName("getUsername retorna el nombre de usuario correctamente")
    void getUsername_retornaUsernameCorrectamente() {
        // Setup
        String username = "usuario1";
        User user = new User(username, "mail@mail.com");
        // Ejercitación
        String result = user.getUsername();
        // Verificación
        assertEquals(username, result, "El username retornado no es correcto");
    }

    @Test
    @DisplayName("getTweets retorna lista vacía si el usuario no tiene tweets")
    void getTweets_listaVaciaSiNoTieneTweets() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        // Ejercitación
        List<Tweet> tweets = user.getTweets();
        // Verificación
        assertTrue(tweets.isEmpty(), "La lista de tweets debe estar vacía");
    }

    @Test
    @DisplayName("getRetweets retorna lista vacía si el usuario no tiene retweets")
    void getRetweets_listaVaciaSiNoTieneRetweets() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        // Ejercitación
        List<ReTweet> retweets = user.getRetweets();
        // Verificación
        assertTrue(retweets.isEmpty(), "La lista de retweets debe estar vacía");
    }

    @Test
    @DisplayName("getTweets retorna la lista de tweets del usuario")
    void getTweets_retornaListaDeTweets() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        user.createTweet("Tweet 1");
        user.createTweet("Tweet 2");
        // Ejercitación
        List<Tweet> tweets = user.getTweets();
        // Verificación
        assertEquals(2, tweets.size(), "El usuario debe tener dos tweets en la lista");
        assertEquals("Tweet 1", tweets.get(0).getText(), "El primer tweet debe ser 'Tweet 1'");
        assertEquals("Tweet 2", tweets.get(1).getText(), "El segundo tweet debe ser 'Tweet 2'");
    }

    @Test
    @DisplayName("getRetweets retorna la lista de retweets del usuario")
    void getRetweets_retornaListaDeRetweets() {
        // Setup
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario2", "mail2@mail.com");
        user1.createTweet("Tweet original");
        Tweet tweet = user1.getTweets().get(0);
        user2.createRetweet(tweet);
        // Ejercitación
        List<ReTweet> retweets = user2.getRetweets();
        // Verificación
        assertEquals(1, retweets.size(), "El usuario debe tener un retweet en la lista");
        assertEquals(tweet, retweets.get(0).getOriginalTweet(), "El retweet debe referenciar al tweet original");
    }

    @Test
    @DisplayName("getUserName retorna el nombre de usuario correctamente")
    void getUserName_retornaNombreDeUsuario() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        // Ejercitación
        String username = user.getUsername();
        // Verificación
        assertEquals("usuario1", username, "El nombre de usuario debe ser 'usuario1'");
    }

    @Test
    @DisplayName("userName no puede ser menor a 5 ni mayor a 25 caracteres")
    void userNameLongitudInvalida_lanzaExcepcion() {
        // Setup, Ejercitación y Verificación
        var ex1 = assertThrows(RuntimeException.class, () -> {
            new User("abc", "mail@mail.com");
        });
        assertEquals(User.ERROR_USERNAME_SHORT, ex1.getMessage());
        var ex2 = assertThrows(RuntimeException.class, () -> {
            new User("a12345678901234567890123456", "mail@mail.com");
        });
        assertEquals(User.ERROR_USERNAME_LONG, ex2.getMessage());
    }

    @Test
    @DisplayName("Eliminar usuario elimina todos sus tweets y retweets")
    void eliminarUsuario_eliminaTweetsYRetweets() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        user.createTweet("Tweet 1");
        user.createTweet("Tweet 2");
        assertEquals(2, user.getTweets().size(), "Debe tener 2 tweets antes de eliminar");
        // Ejercitación
        user.delete();
        // Verificación
        assertTrue(user.getTweets().isEmpty(), "Los tweets deben eliminarse al borrar el usuario");
        assertTrue(user.getRetweets().isEmpty(), "Los retweets deben eliminarse al borrar el usuario");
    }

    @Test
    @DisplayName("No se puede crear tweet con texto menor a 1 o mayor a 280 caracteres desde User")
    void createTweet_textoInvalido_lanzaExcepcion() {
        // Setup
        User user = new User("usuario1", "mail@mail.com");
        // Ejercitación y Verificación
        var ex1 = assertThrows(RuntimeException.class, () -> user.createTweet(""));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex1.getMessage());
        var ex2 = assertThrows(RuntimeException.class, () -> user.createTweet("a".repeat(281)));
        assertEquals(Tweet.ERROR_TWEET_TEXT_LENGTH, ex2.getMessage());
    }

    @Test
    @DisplayName("No se puede crear usuario con username nulo o vacío")
    void crearUsuario_usernameNuloOVacio_lanzaExcepcion() {
        // Ejercitación y Verificación
        var ex1 = assertThrows(RuntimeException.class, () -> new User(null, "mail@mail.com"));
        assertEquals(User.ERROR_USERNAME_EMPTY, ex1.getMessage());
        var ex2 = assertThrows(RuntimeException.class, () -> new User("", "mail@mail.com"));
        assertEquals(User.ERROR_USERNAME_EMPTY, ex2.getMessage());
    }

    @Test
    @DisplayName("No se puede crear usuario con username menor a 5 caracteres")
    void crearUsuario_usernameMenorA5_lanzaExcepcion() {
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new User("abcd", "mail@mail.com"));
        assertEquals(User.ERROR_USERNAME_SHORT, ex.getMessage());
    }

    @Test
    @DisplayName("No se puede crear usuario con username mayor a 25 caracteres")
    void crearUsuario_usernameMayorA25_lanzaExcepcion() {
        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> new User("a12345678901234567890123456", "mail@mail.com"));
        assertEquals(User.ERROR_USERNAME_LONG, ex.getMessage());
    }

    @Test
    @DisplayName("No se puede crear usuario con email nulo o vacío")
    void crearUsuario_emailNuloOVacio_lanzaExcepcion() {
        // Ejercitación y Verificación
        var ex1 = assertThrows(RuntimeException.class, () -> new User("usuario1", null));
        assertEquals(User.ERROR_EMAIL_EMPTY, ex1.getMessage());
        var ex2 = assertThrows(RuntimeException.class, () -> new User("usuario1", ""));
        assertEquals(User.ERROR_EMAIL_EMPTY, ex2.getMessage());
    }

    @Test
    @DisplayName("No se agrega dos veces el mismo tweet (objeto) a la lista de tweets del usuario")
    void createTweet_noAgregaMismoObjetoTweetDuplicado() {
        User user = new User("usuario1", "mail@mail.com");
        Tweet tweet = new Tweet("Tweet único", user);
        user.getTweets().add(tweet);
        // Intenta agregar el mismo objeto Tweet
        if (!user.getTweets().contains(tweet)) {
            user.getTweets().add(tweet);
        }
        assertEquals(1, user.getTweets().size(), "No debe agregarse dos veces el mismo objeto tweet");
    }

    @Test
    @DisplayName("No se agrega dos veces un tweet con el mismo id a la lista de tweets del usuario")
    void createTweet_noAgregaTweetConMismoIdDuplicado() {
        User user = new User("usuario1", "mail@mail.com");
        String id = "id-1";
        Tweet tweet1 = new Tweet(id, "texto", user);
        Tweet tweet2 = new Tweet(id, "otro texto", user);
        user.getTweets().add(tweet1);
        // Intenta agregar otro tweet con el mismo id
        if (!user.getTweets().contains(tweet2)) {
            user.getTweets().add(tweet2);
        }
        assertEquals(1, user.getTweets().size(), "No debe agregarse dos veces un tweet con el mismo id");
    }

    @Test
    @DisplayName("No se agrega dos veces el mismo retweet (mismo tweet de origen y usuario) a la lista de retweets del usuario")
    void createRetweet_noAgregaMismoRetweetDuplicado() {
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario2", "mail2@mail.com");
        Tweet tweet1 = new Tweet("id-1", "texto", user1);
        Tweet tweet2 = new Tweet("id-1", "otro texto", user1);
        ReTweet retweet1 = new ReTweet(user2, tweet1);
        ReTweet retweet2 = new ReTweet(user2, tweet2);
        user2.getRetweets().add(retweet1);
        // Intenta agregar otro retweet con el mismo tweet de origen y usuario
        if (!user2.getRetweets().contains(retweet2)) {
            user2.getRetweets().add(retweet2);
        }
        assertEquals(1, user2.getRetweets().size(), "No debe agregarse dos veces el mismo retweet (mismo tweet de origen y usuario)");
    }

    @Test
    @DisplayName("createTweet lanza excepción si se intenta agregar un tweet duplicado (mismo id)")
    void createTweet_lanzaExcepcionSiTweetDuplicado() {
        User user = new User("usuario1", "mail@mail.com");
        String id = "id-1";
        Tweet tweet1 = new Tweet(id, "texto", user);
        Tweet tweet2 = new Tweet(id, "otro texto", user);
        user.getTweets().add(tweet1);
        var ex = assertThrows(RuntimeException.class, () -> {
            if (!user.getTweets().contains(tweet2)) {
                user.getTweets().add(tweet2);
            } else {
                throw new RuntimeException(User.ERROR_TWEET_DUPLICATE);
            }
        });
        assertEquals(User.ERROR_TWEET_DUPLICATE, ex.getMessage());
    }

    @Test
    @DisplayName("createRetweet lanza excepción si se intenta agregar un retweet duplicado (mismo tweet de origen y usuario)")
    void createRetweet_lanzaExcepcionSiRetweetDuplicado() {
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario2", "mail2@mail.com");
        Tweet tweet1 = new Tweet("id-1", "texto", user1);
        Tweet tweet2 = new Tweet("id-1", "otro texto", user1);
        ReTweet retweet1 = new ReTweet(user2, tweet1);
        ReTweet retweet2 = new ReTweet(user2, tweet2);
        user2.getRetweets().add(retweet1);
        var ex = assertThrows(RuntimeException.class, () -> {
            if (!user2.getRetweets().contains(retweet2)) {
                user2.getRetweets().add(retweet2);
            } else {
                throw new RuntimeException(User.ERROR_RETWEET_DUPLICATE);
            }
        });
        assertEquals(User.ERROR_RETWEET_DUPLICATE, ex.getMessage());
    }

    @Test
    @DisplayName("Usuarios iguales tienen el mismo hashCode")
    void usuariosIguales_tienenMismoHashCode() {
        // Setup
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario1", "mail@mail.com");
        // Verificación
        assertEquals(user1.hashCode(), user2.hashCode(), "Usuarios iguales deben tener el mismo hashCode");
    }

    @Test
    @DisplayName("Usuarios distintos tienen hashCodes diferentes")
    void usuariosDistintos_tienenHashCodesDiferentes() {
        // Setup
        User user1 = new User("usuario1", "mail@mail.com");
        User user2 = new User("usuario2", "mail2@mail.com");
        // Verificación
        assertNotEquals(user1.hashCode(), user2.hashCode(), "Usuarios distintos deben tener hashCodes diferentes");
    }
}
