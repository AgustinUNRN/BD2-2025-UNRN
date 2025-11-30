package unrn.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import unrn.model.ReTweet;
import java.util.List;

public interface ReTweetRepository extends JpaRepository<ReTweet, Integer> {
    List<ReTweet> findByUserRetweeted_Username(String userName);

    void deleteAllByUserRetweeted_Username(String userName);
}

