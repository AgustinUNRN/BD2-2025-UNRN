package unrn.model.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.model.dto.ReTweetDto;
import unrn.model.dto.TweetDto;
import unrn.model.dto.UserDto;
import unrn.model.service.ReTweetService;
import unrn.model.service.TweetService;
import unrn.model.service.UserService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {
    private final TweetService tweetService;
    private final UserService userService;
    private final ReTweetService reTweetService;

    public TweetController(TweetService tweetService, UserService userService, ReTweetService reTweetService) {
        this.tweetService = tweetService;
        this.userService = userService;
        this.reTweetService = reTweetService;
    }

    @GetMapping
    public List<TweetDto> list() {
        return tweetService.listAll();
    }

    @GetMapping("/{username}")
    public List<TweetDto> listTweetsByUser(@PathVariable String username) {
        return tweetService.listByUser(username);
    }

    @GetMapping("/retweets")
    public List<ReTweetDto> listReTweets() {
        return reTweetService.listAll();
    }

    @GetMapping("/retweets/{username}")
    public List<ReTweetDto> listReTweetsByUser(@PathVariable String username) {
        return reTweetService.listByUser(username);
    }


    @PostMapping
    public ResponseEntity<TweetDto> create(@RequestParam String username, @RequestParam String text) {
        UserDto u = userService.findByUserName(username);
        if (u==null) return ResponseEntity.badRequest().build();
        TweetDto tweetDto = new TweetDto(0,u.getUserName(),text, new Date());
        TweetDto saved = tweetService.createTweet(tweetDto);
        return ResponseEntity.ok(saved);
    }

}

