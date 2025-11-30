package unrn.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.model.User;
import unrn.model.repo.UserRepository;
import unrn.model.service.UserService;
import unrn.model.dto.UserDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> get(@PathVariable String username) {
        Optional<User> u = userRepository.findById(username);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        if (userService.existsByUserName(userDto.getUserName())) {
            return ResponseEntity.status(409).build();
        }
        UserDto saved = userService.createUser(userDto);
        return ResponseEntity.ok(saved);
    }
}

