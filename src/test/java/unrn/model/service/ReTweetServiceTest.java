package unrn.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import unrn.model.ReTweet;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.model.dto.ReTweetDto;
import unrn.model.dto.TweetDto;
import unrn.model.repo.ReTweetRepository;
import unrn.model.repo.TweetRepository;
import unrn.model.repo.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReTweetServiceTest {
    private ReTweetRepository reTweetRepository;
    private UserRepository userRepository;
    private TweetRepository tweetRepository;
    private ReTweetService reTweetService;

    @BeforeEach
    void setUp() {
        reTweetRepository = Mockito.mock(ReTweetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        tweetRepository = Mockito.mock(TweetRepository.class);
        reTweetService = new ReTweetService();
        try {
            var f1 = ReTweetService.class.getDeclaredField("reTweetRepository");
            f1.setAccessible(true);
            f1.set(reTweetService, reTweetRepository);
            var f2 = ReTweetService.class.getDeclaredField("userRepository");
            f2.setAccessible(true);
            f2.set(reTweetService, userRepository);
            var f3 = ReTweetService.class.getDeclaredField("tweetRepository");
            f3.setAccessible(true);
            f3.set(reTweetService, tweetRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listAll_mapsToDto() {
        User creator = new User("creator_thor","creator@a.com");
        User retweeter = new User("thor__","thor@a.com");
        Tweet orig = new Tweet(5,"hit the clouds", creator, new Date());
        ReTweet rt = new ReTweet(retweeter, orig);
        when(reTweetRepository.findAll()).thenReturn(List.of(rt));

        var res = reTweetService.listAll();
        assertEquals(1, res.size());
        assertEquals(5, res.get(0).getOriginalTweet().getId());
    }

    @Test
    void listByUser_filters() {
        User creator = new User("creator_loki","creator@a.com");
        User retweeter = new User("loki_","loki@a.com");
        Tweet orig = new Tweet(10,"pranks", creator, new Date());
        ReTweet rt = new ReTweet(retweeter, orig);
        when(reTweetRepository.findByUserRetweeted_Username("loki_")).thenReturn(List.of(rt));

        var res = reTweetService.listByUser("loki_");
        assertEquals(1, res.size());
        assertEquals(10, res.get(0).getOriginalTweet().getId());
    }

    @Test
    void create_savesWhenOk() {
        User creator = new User("creator_sig","c@x.com");
        User u = new User("sigmund","s@x.com");
        Tweet orig = new Tweet(20,"deep", creator, new Date());
        ReTweetDto dto = new ReTweetDto(0,"sigmund", new TweetDto(20, "creator_sig", "deep", new Date()), new Date());
        when(userRepository.findById("sigmund")).thenReturn(Optional.of(u));
        when(tweetRepository.findById(20)).thenReturn(Optional.of(orig));
        when(reTweetRepository.save(any(ReTweet.class))).thenAnswer(i -> i.getArgument(0));

        var saved = reTweetService.create(dto);
        assertNotNull(saved);
        assertEquals(20, saved.getOriginalTweet().getId());
    }

    @Test
    void create_throwsIfUserMissing() {
        ReTweetDto dto = new ReTweetDto(0,"noone", new TweetDto(1,"x","t", new Date()), new Date());
        when(userRepository.findById("noone")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reTweetService.create(dto));
    }

    @Test
    void deleteReTweetByUser_callsRepo() {
        reTweetService.deleteReTweetByUser("someone");
        verify(reTweetRepository, times(1)).deleteAllByUserRetweeted_Username("someone");
    }
}
