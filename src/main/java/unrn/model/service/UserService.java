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

    @Autowired
    private UserRepository userRepository;
    private TweetRepository tweetRepository;
    private ReTweetRepository reTweetRepository;

    public List<UserDto> listAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDto(u.getUsername(), u.getEmail(), null, null))
                .collect(Collectors.toList());
    }

    public UserDto findByUserName(String userName) {
        Optional<User> userOpcional = userRepository.findByUsername(userName);

        if (userOpcional.isEmpty()){
            throw new RuntimeException("User not found");
        }

        User user = userOpcional.get();
        return new UserDto(user.getUsername(), user.getEmail(), null, null);
    }

    public Boolean existsByUserName(String userName) {
        UserDto aux = findByUserName(userName);
        return aux.getUserName().equals(userName);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = new User(userDto.getUserName(), userDto.getEmail());
        userRepository.save(user);
        return userDto;
    }

    @Transactional
    public void deleteUser(String userName) {
        userRepository.deleteByUsername(userName);
        tweetRepository.deleteAllByUserCreator_Username(userName);
        reTweetRepository.deleteAllByUserRetweeted_Username(userName);

    }
}
