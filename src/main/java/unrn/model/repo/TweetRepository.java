package unrn.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import unrn.model.Tweet;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    List<Tweet> findByUserCreator_Username(String userName);

    void deleteAllByUserCreator_Username(String userName);
}
