package unrn.model.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.model.ReTweet;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.model.repo.ReTweetRepository;
import unrn.model.repo.TweetRepository;
import unrn.model.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final ReTweetRepository reTweetRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository, ReTweetRepository reTweetRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.reTweetRepository = reTweetRepository;
    }

    @GetMapping
    public List<Tweet> list() {
        return tweetRepository.findAll();
    }

   /* @PostMapping
    public ResponseEntity<Tweet> create(@RequestParam String username, @RequestParam String text) {
        Optional<User> u = userRepository.findById(username);
        if (u.isEmpty()) return ResponseEntity.badRequest().build();
        Tweet tweet = new Tweet(text, u.get());
        Tweet saved = tweetRepository.save(tweet);
        return ResponseEntity.ok(saved);
    }*/

    @PostMapping("/{id}/retweet")
    public ResponseEntity<ReTweet> retweet(@PathVariable int id, @RequestParam String username) {
        Optional<Tweet> t = tweetRepository.findById(id);
        Optional<User> u = userRepository.findById(username);
        if (t.isEmpty() || u.isEmpty()) return ResponseEntity.badRequest().build();
        Tweet original = t.get();
        User user = u.get();
        // check not same creator
        if (original.isUserCreator(user)) return ResponseEntity.status(403).build();
        ReTweet rt = new ReTweet(user, original);
        ReTweet saved = reTweetRepository.save(rt);
        return ResponseEntity.ok(saved);
    }
}

