package unrn.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.tool.schema.Action;
import unrn.model.ReTweet;
import unrn.model.User;
import unrn.model.Tweet;

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

            insertIfNotExists(entityManager, "loki_", "loki@asgard.com", new String[]{}); // sin tweets

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
