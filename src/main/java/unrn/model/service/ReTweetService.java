package unrn.model.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unrn.model.ReTweet;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.model.dto.ReTweetDto;
import unrn.model.repo.ReTweetRepository;
import unrn.model.repo.TweetRepository;
import unrn.model.repo.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service public class ReTweetService {
    @Autowired
    private ReTweetRepository reTweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    public List<ReTweetDto> listAll() {
        return reTweetRepository.findAll()
                .stream()
                .map(rt -> new ReTweetDto(rt.getId(),rt.getUserRetweeted().getUsername(),
                        new unrn.model.dto.TweetDto(rt.getOriginalTweet().getId(),
                                rt.getOriginalTweet().getUserCreatorUsername(),
                                rt.getOriginalTweet().getText(),
                                rt.getOriginalTweet().getDateCreated() != null ? Date.from(rt.getOriginalTweet().getDateCreated().toInstant()) : null),
                        rt.getDate()))
                .collect(Collectors.toList());
    }

    public List<ReTweetDto> listByUser(String userName) {
        return reTweetRepository.findByUserRetweeted_Username(userName)
                .stream()
                .map(rt -> new ReTweetDto(rt.getId(),rt.getUserRetweetedUsername(),
                        new unrn.model.dto.TweetDto(
                                rt.getOriginalTweet().getId(),
                                rt.getOriginalTweet().getUserCreatorUsername(),
                                rt.getOriginalTweet().getText(),
                                rt.getOriginalTweet().getDateCreated() != null ? Date.from(rt.getOriginalTweet().getDateCreated().toInstant()) : null),
                        rt.getDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReTweetDto create(ReTweetDto reTweetDto) {
        String retweeterUserName = reTweetDto.getUserRetweeted();
        int originalId = reTweetDto.getOriginalTweet().getId();

        User user = userRepository.findById(retweeterUserName)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + retweeterUserName));

        Tweet original = tweetRepository.findById(originalId)
                .orElseThrow(() -> new IllegalArgumentException("Tweet original no encontrado: " + originalId));

        ReTweet reTweet = new ReTweet(user, original);
        reTweetRepository.save(reTweet);
        return reTweetDto;
    }

    @Transactional
    public void deleteReTweetByUser(String userName) {
        reTweetRepository.deleteAllByUserRetweeted_Username(userName);
    }
}