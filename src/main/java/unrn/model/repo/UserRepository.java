package unrn.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unrn.model.User;
import unrn.model.dto.UserDto;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
    //UserDto createUser(UserDto userDto);
}

