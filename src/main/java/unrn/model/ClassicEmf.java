package unrn.model;

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
                .property(PersistenceConfiguration.JDBC_DRIVER, "org.apache.derby.jdbc.ClientDriver")
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

            // Trabaja con tus entidades aquí

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }
}