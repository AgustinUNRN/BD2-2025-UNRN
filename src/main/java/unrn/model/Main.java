package unrn.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.tool.schema.Action;

public class Main {
    // iniciar el servidor de la bd derby con:
    // java -jar derbyrun.jar server start
    //~/Descargas/db-derby-10.17.1.0-bin/lib
    //public static final String IN_MEMORY_DB_URL = "jdbc:derby:memory:ejemplo;create=true";
    public static final String CLIENT_DB_URL = "jdbc:derby://localhost:1527/ejemplo;create=true";
    private static final String DB_USER = "app";
    private static final String DB_PWD = "app";

    public static void main(String[] args) {
        var emf = createEmf();

        emf.runInTransaction((em) -> {
            insertIfNotExists(em, "@odin", "odin@asgard.com", new String[]{
                    "He visto el Yggdrasil vibrar bajo la luna; hoy guardo más secretos.",
                    "Los cuervos traen historias de tierras lejanas; las escucho con cuidado.",
                    "Un sacrificio por el saber fortalece el alma y afila la visión."
            });

            insertIfNotExists(em, "@thor", "thor@asgard.com", new String[]{
                    "Con Mjölnir hice temblar las nubes y celebré con un gran banquete."
            });

            insertIfNotExists(em, "@loki", "loki@asgard.com", new String[]{
                    // sin tweets intencionalmente
            });
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
