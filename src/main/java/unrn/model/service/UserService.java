package unrn.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unrn.model.User;
import unrn.model.dto.UserDto;
import unrn.model.repo.ReTweetRepository;
import unrn.model.repo.TweetRepository;
import unrn.model.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final ReTweetRepository reTweetRepository;

    public UserService(UserRepository userRepository,
                       TweetRepository tweetRepository,
                       ReTweetRepository reTweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
        this.reTweetRepository = reTweetRepository;
    }

    public List<UserDto> listAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDto(u.getUsername(), u.getEmail(), null, null))
                .collect(Collectors.toList());
    }

    public UserDto findByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .map(u -> new UserDto(u.getUsername(), u.getEmail(), null, null))
                .orElse(null);
    }

    public Boolean existsByUserName(String userName) {
        return userRepository.findByUsername(userName).isPresent();
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {

        User user = new User(userDto.getUserName(), userDto.getEmail());
        userRepository.save(user);
        return userDto;
    }

    @Transactional
    public void deleteUser(String userName) {
        reTweetRepository.deleteAllByUserRetweeted_Username(userName);
        tweetRepository.deleteAllByUserCreator_Username(userName);
        userRepository.deleteByUsername(userName);
    }
}
