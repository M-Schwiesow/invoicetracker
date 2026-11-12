package lab.home.invoicetracker.resource;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lab.home.invoicetracker.dto.UserDTO;
import lab.home.invoicetracker.model.HttpResponse;
import lab.home.invoicetracker.model.User;
import lab.home.invoicetracker.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping (path = "/user")
@RequiredArgsConstructor 
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
            HttpResponse.builder()
            .timeStamp(LocalDateTime.now().toString())
            .data(Map.of("user", userDTO))
            .message("User created")
            .status(HttpStatus.CREATED)
            .statusCode(HttpStatus.CREATED.value())
            .build()
        );
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
