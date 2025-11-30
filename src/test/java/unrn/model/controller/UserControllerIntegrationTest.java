package unrn.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import unrn.model.User;
import unrn.model.dto.UserDto;
import unrn.model.repo.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User saveUser(String username, String email) {
        User u = new User(username, email);
        return userRepository.save(u);
    }

    @Test
    void listUsers_returnsList() throws Exception {
        saveUser("alice", "alice@example.com");

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userName").isNotEmpty());
    }

    @Test
    void getUser_found() throws Exception {
        saveUser("bob", "bob@example.com");

        mvc.perform(get("/api/users/{username}", "bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    void getUser_notFound() throws Exception {
        mvc.perform(get("/api/users/{username}", "no-such-user"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_success() throws Exception {
        UserDto dto = new UserDto("charlie", "charlie@example.com", null, null);
        String body = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("charlie"));

        assertThat(userRepository.findByUsername("charlie")).isPresent();
    }

    @Test
    void createUser_conflict() throws Exception {
        saveUser("dave", "dave@example.com");
        UserDto dto = new UserDto("dave", "dave@example.com", null, null);
        String body = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUser_success() throws Exception {
        saveUser("erin", "erin@example.com");

        mvc.perform(delete("/api/users/{username}", "erin"))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findByUsername("erin")).isEmpty();
    }

    @Test
    void deleteUser_notFound() throws Exception {
        mvc.perform(delete("/api/users/{username}", "ghost"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_cors_header_present() throws Exception {
        saveUser("frank", "frank@example.com");

        mvc.perform(delete("/api/users/{username}", "frank")
                        .header("Origin", "http://localhost:5173"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }
}
