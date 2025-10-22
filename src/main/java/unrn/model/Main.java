package unrn.model;

import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.tool.schema.Action;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static final String IN_MEMORY_DB_URL = "jdbc:derby:memory:ejemplo;create=true";
    public static final String CLIENT_DB_URL = "jdbc:derby://localhost:1527/ejemplo;create=true";
    private static final String DB_USER = "app";
    private static final String DB_PWD = "app";

    public static void main(String[] args) {
        var emf = createEmf();

        emf.runInTransaction((em) -> {
            var juan = new User("juan123", "juan123@mail.com");

//            var tweet1 = new Tweet("Hola, este es mi primer tweet!", juan);
//            em.persist(tweet1);
            juan.createTweet("Segundo tweet, ahora con más experiencia!");
            juan.createTweet("Tercer tweet, ¡me encanta Twitter!");
            em.persist(juan);
        }
        );
    }
    private static EntityManagerFactory createEmf() {
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
            return config.createEntityManagerFactory();
        }

}
