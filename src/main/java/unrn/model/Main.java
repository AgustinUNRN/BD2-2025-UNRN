package unrn.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.TypedQuery;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.tool.schema.Action;

import java.util.List;

public class Main {
    // iniciar el servidor de la bd derby con:
    // java -jar derbyrun.jar server start
    //public static final String IN_MEMORY_DB_URL = "jdbc:derby:memory:ejemplo;create=true";
    public static final String CLIENT_DB_URL = "jdbc:derby://localhost:1527/ejemplo;create=true";
    private static final String DB_USER = "app";
    private static final String DB_PWD = "app";

    public static void main(String[] args) {
        var emf = createEmf();

        emf.runInTransaction((em) -> {
            insertIfNotExists(em, "@odin__", "odin@asgard.com", new String[]{
                    "He visto el Yggdrasil vibrar bajo la luna; hoy guardo más secretos.",
                    "Los cuervos traen historias de tierras lejanas; las escucho con cuidado.",
                    "Un sacrificio por el saber fortalece el alma y afila la visión."
            });

            insertIfNotExists(em, "@thor__", "thor@asgard.com", new String[]{
                    "Con Mjölnir hice temblar las nubes y celebré con un gran banquete."
            });

            insertIfNotExists(em, "@loki__", "loki@asgard.com", new String[]{
                    "Hoy cambié las señales de tráfico... el caos es un arte.",
                    "Presté mi sombra a un dios vecino; ahora llega tarde a todo.",
                    "Vendí mapas falsos a un viajero — lo enriquecí con historias.",
                    "Una broma al viento y todo el pueblo discutió por una semana.",
                    "Me disfracé de cuervo y robé tres risas por el camino.",
                    "Conjuro un acertijo y la verdad toma vacaciones.",
                    "Intercambié etiquetas: 'veneno' por 'té' — elegante caos.",
                    "Hice una moneda bailar y decidió nunca volver a casa.",
                    "¿Problema social? Un buen engaño y una mala memoria.",
                    "Hoy regalé un laberinto invisible; la confusión es mi presente.",
                    "Prometí sinceridad, luego la presté a otro por diversión.",
                    "Cambiar el nombre de la luna es fácil; la gente no reclama.",
                    "Hice que un espejo pidiera perdón antes de devolverlo.",
                    "Planté una mentira y coseché curiosidad y dos sonrisas.",
                    "Bailé en dos caminos y el mundo aplaudió a ambos.",
                    "Si pierdo, regreso en forma de chiste — la revancha espera."
            });
        });

        // nueva transacción: crear al menos 3 retweets para @thor__
        emf.runInTransaction(em -> {
            User thor = em.find(User.class, "@thor__");
            if (thor == null) {
                System.out.println("User '@thor__' no encontrado, no se crearán retweets.");
                return;
            }

            // obtener hasta 2 tweets de loki
            TypedQuery<Tweet> qLoki = em.createQuery(
                    "select t from Tweet t where t.userCreator.username = :u", Tweet.class);
            qLoki.setParameter("u", "@loki__");
            qLoki.setMaxResults(2);
            List<Tweet> lokiTweets = qLoki.getResultList();

            // obtener 1 tweet de odin
            TypedQuery<Tweet> qOdin = em.createQuery(
                    "select t from Tweet t where t.userCreator.username = :u", Tweet.class);
            qOdin.setParameter("u", "@odin__");
            qOdin.setMaxResults(1);
            List<Tweet> odinTweets = qOdin.getResultList();

            int added = 0;
            for (Tweet t : lokiTweets) {
                thor.createRetweet(t);
                added++;
            }
            for (Tweet t : odinTweets) {
                if (added >= 3) break;
                thor.createRetweet(t);
                added++;
            }

            em.merge(thor);
            System.out.println("Se añadieron " + added + " retweets para " + thor.getUsername());
        });

        emf.close();
    }

    private static void insertIfNotExists(EntityManager em, String username, String email, String[] tweets) {
        User existing = em.find(User.class, username);
        if (existing != null) {
            System.out.println("User '" + username + "' already exists; skipping insert.");
            return;
        }

        var user = new User(username, email);
        for (String t : tweets) {
            user.createTweet(t);
        }
        em.persist(user);
        System.out.println("Inserted user '" + username + "' with " + tweets.length + " tweets.");
    }

    private static EntityManagerFactory createEmf() {
        PersistenceConfiguration config = new PersistenceConfiguration("ejemplo")
                .managedClass(ReTweet.class)
                .managedClass(User.class)
                .managedClass(Tweet.class)
                .property(PersistenceConfiguration.JDBC_DRIVER, "org.apache.derby.client.ClientAutoloadedDriver")
                .property(PersistenceConfiguration.JDBC_URL, CLIENT_DB_URL)
                .property(PersistenceConfiguration.JDBC_USER, DB_USER)
                .property(PersistenceConfiguration.JDBC_PASSWORD, DB_PWD)
                .property(JdbcSettings.SHOW_SQL, true)
                .property(JdbcSettings.FORMAT_SQL, true)
                .property(JdbcSettings.HIGHLIGHT_SQL, true)
                // use UPDATE so we don't attempt to drop constraints/tables that already exist on server
                .property(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, Action.UPDATE);

        return config.createEntityManagerFactory();
    }
}
