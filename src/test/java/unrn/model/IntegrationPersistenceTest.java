package unrn.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.schema.Action;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IntegrationPersistenceTest {
    private static final String IN_MEMORY_DB_URL = "jdbc:h2:mem:itdb;DB_CLOSE_DELAY=-1";

    private EntityManagerFactory createEmf() {
        // Use Hibernate's programmatic bootstrap to build an EntityManagerFactory for tests
        Map<String,Object> settings = new HashMap<>();
        // Use H2 in-memory DB for integration tests to avoid touching server DB and embedded driver issues
        settings.put(Environment.DRIVER, "org.h2.Driver");
        settings.put(Environment.URL, IN_MEMORY_DB_URL);
        // Leave dialect unset so Hibernate autodetects it from JDBC metadata
        // Use create-drop to create schema for the test run
        settings.put(Environment.HBM2DDL_AUTO, "create-drop");
        // No user/password required for embedded in-memory DB
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.FORMAT_SQL, "true");

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Tweet.class)
                .addAnnotatedClass(ReTweet.class);

        Metadata metadata = sources.buildMetadata();
        // buildSessionFactory returns a Hibernate SessionFactory which is also an EntityManagerFactory implementation
        return metadata.buildSessionFactory();
    }

    @Test
    public void integration_user_tweet_retweet_and_deletion_flow() {
        var emf = createEmf();
        try {
            // create users and relations
            emf.runInTransaction(em -> {
                User a = new User("userA", "a@example.com");
                User b = new User("userB", "b@example.com");

                a.createTweet("hello from A");
                // persist A and B
                em.persist(a);
                em.persist(b);

                // B retweets A's first tweet
                Tweet tweetA = a.getTweets().get(0);
                b.createRetweet(tweetA);
                // persist B again not necessary but keep em.merge to attach state
                em.merge(b);
            });

            // verify counts after creation
            emf.runInTransaction(em -> {
                Long tweets = em.createQuery("select count(t) from Tweet t", Long.class).getSingleResult();
                Long retweets = em.createQuery("select count(r) from ReTweet r", Long.class).getSingleResult();
                Assertions.assertEquals(1L, tweets);
                Assertions.assertEquals(1L, retweets);
            });

            // delete userB (should remove its retweets)
            emf.runInTransaction(em -> {
                User b = em.find(User.class, "userB");
                em.remove(b);
            });

            emf.runInTransaction(em -> {
                Long tweets = em.createQuery("select count(t) from Tweet t", Long.class).getSingleResult();
                Long retweets = em.createQuery("select count(r) from ReTweet r", Long.class).getSingleResult();
                Assertions.assertEquals(1L, tweets);
                Assertions.assertEquals(0L, retweets);
            });

            // delete userA (should remove its tweets)
            emf.runInTransaction(em -> {
                User a = em.find(User.class, "userA");
                em.remove(a);
            });

            emf.runInTransaction(em -> {
                Long tweets = em.createQuery("select count(t) from Tweet t", Long.class).getSingleResult();
                Long retweets = em.createQuery("select count(r) from ReTweet r", Long.class).getSingleResult();
                Assertions.assertEquals(0L, tweets);
                Assertions.assertEquals(0L, retweets);
            });

        } finally {
            emf.close();
        }
    }
}
