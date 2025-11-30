package unrn.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unrn.model.Tweet;
import unrn.model.ReTweet;
import unrn.model.User;
import unrn.model.repo.TweetRepository;
import unrn.model.repo.UserRepository;
import unrn.model.dto.TweetDto;
import unrn.model.dto.ReTweetDto;


import java.time.LocalDateTime;
import java.util.Date;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TweetDto> listAll() {
        return tweetRepository.findAll()
                .stream()
                .map(t -> new TweetDto(t.getId(),t.getUserCreatorUsername(), t.getText(), t.getDateCreated() != null ? Date.from(t.getDateCreated().toInstant()) : null))
                .collect(Collectors.toList());
    }

    public List<TweetDto> listByUser(String userName) {
        return tweetRepository.findByUserCreator_Username(userName)
                .stream().map(t -> new TweetDto(t.getId(),t.getUserCreatorUsername(), t.getText(), t.getDateCreated() != null ? Date.from(t.getDateCreated().toInstant()) : null))
                .collect(Collectors.toList());
    }

    @Transactional
    public TweetDto createTweet(TweetDto tweetDto) {
        String userName = tweetDto.getUserName();
        String text = tweetDto.getText();
        Date date = tweetDto.getCreatedAt();

        User user = userRepository.findById(tweetDto.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userName));

        Tweet tweet = new Tweet(text, user, date);

        tweetRepository.save(tweet);
        return tweetDto;
    }

    @Transactional
    public void deleteTweetByUser(String userName) {
        tweetRepository.deleteAllByUserCreator_Username(userName);
    }
}