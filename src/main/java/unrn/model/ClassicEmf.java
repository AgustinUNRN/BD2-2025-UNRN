package unrn.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.tool.schema.Action;

public class ClassicEmf {
    public static final String CLIENT_DB_URL = "jdbc:derby://localhost:1527/ejemplo;create=true";
    private static final String DB_USER = "app";
    private static final String DB_PWD = "app";

    public static void main(String[] args) {
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
                .property(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, Action.CREATE_DROP);

        var entityManagerFactory = config.createEntityManagerFactory();
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            insertIfNotExists(entityManager, "odin_", "odin@asgard.com", new String[]{
                    "He visto el Yggdrasil vibrar bajo la luna; hoy guardo más secretos.",
                    "Los cuervos traen historias de tierras lejanas; las escucho con cuidado.",
                    "Un sacrificio por el saber fortalece el alma y afila la visión."
            });

            insertIfNotExists(entityManager, "thor_", "thor@asgard.com", new String[]{
                    "Con Mjölnir hice temblar las nubes y celebré con un gran banquete."
            });

            insertIfNotExists(entityManager, "loki__", "loki@asgard.com", new String[]{
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

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
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
}
