package unrn.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.model.repo.TweetRepository;
import unrn.model.repo.UserRepository;
import unrn.model.dto.TweetDto;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TweetServiceTest {
    private TweetRepository tweetRepository;
    private UserRepository userRepository;
    private TweetService tweetService;

    @BeforeEach
    void setUp() {
        tweetRepository = Mockito.mock(TweetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        tweetService = new TweetService();
        // inject mocks
        tweetService.getClass();
        // use reflection to set private fields
        try {
            var f1 = TweetService.class.getDeclaredField("tweetRepository");
            f1.setAccessible(true);
            f1.set(tweetService, tweetRepository);
            var f2 = TweetService.class.getDeclaredField("userRepository");
            f2.setAccessible(true);
            f2.set(tweetService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listAll_mapsTweetsToDto() {
        User u = new User("alice_","a@x.com");
        Tweet t = new Tweet(1,"hello",u,new Date());
        when(tweetRepository.findAll()).thenReturn(List.of(t));

        var result = tweetService.listAll();

        assertEquals(1, result.size());
        assertEquals("hello", result.get(0).getText());
        assertEquals("alice_", result.get(0).getUserCreatorUsername());
    }

    @Test
    void listByUser_filtersByUserName() {
        User u = new User("bob__","b@x.com");
        Tweet t1 = new Tweet(1,"t1",u,new Date());
        when(tweetRepository.findByUserCreator_Username("bob__")).thenReturn(List.of(t1));

        var result = tweetService.listByUser("bob__");
        assertEquals(1, result.size());
        assertEquals("t1", result.get(0).getText());
    }

    @Test
    void createTweet_savesWhenUserExists() {
        User u = new User("carol_","c@x.com");
        TweetDto dto = new TweetDto(0,"carol_","hola", new Date());
        when(userRepository.findById("carol_")).thenReturn(Optional.of(u));
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(i -> i.getArgument(0));

        var saved = tweetService.createTweet(dto);
        assertNotNull(saved);
        assertEquals("carol_", saved.getUserCreatorUsername());
    }

    @Test
    void createTweet_throwsWhenUserMissing() {
        TweetDto dto = new TweetDto(0,"nope","x", new Date());
        when(userRepository.findById("nope")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> tweetService.createTweet(dto));
    }

    @Test
    void deleteTweetByUser_callsRepo() {
        tweetService.deleteTweetByUser("someone");
        verify(tweetRepository, times(1)).deleteAllByUserCreator_Username("someone");
    }
}

