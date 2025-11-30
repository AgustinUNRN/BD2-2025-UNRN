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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Constructor injection used by Spring in tests
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> list() {
        return userService.listAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> get(@PathVariable String username) {
        UserDto dto = userService.findByUserName(username);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        if (userService.existsByUserName(userDto.getUserName())) {
            return ResponseEntity.status(409).build();
        }
        UserDto saved = userService.createUser(userDto);
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        if (!userService.existsByUserName(username)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
