package lab.home.invoicetracker.resource;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lab.home.invoicetracker.dto.UserDTO;
import lab.home.invoicetracker.form.LoginForm;
import lab.home.invoicetracker.model.HttpResponse;
import lab.home.invoicetracker.model.User;
import lab.home.invoicetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping (path = "/user")
@RequiredArgsConstructor 
@Slf4j 
public class UserResource {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

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

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        log.info("login attempt with email {} and password {} (i am so secure right now)", loginForm.getEmail(), loginForm.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        UserDTO userDTO = userService.getUserByEmail(loginForm.getEmail());
        return userDTO.getIsUsingMfa() ? sendVerificationCode(userDTO) : sendResponse(userDTO);
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
        return ResponseEntity.ok()
            .body(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of("user", userDTO))
                .message("user logged in")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO userDTO) {
        userService.sendVerificationCode(userDTO);
        return ResponseEntity.ok()
            .body(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of("user", userDTO))
                .message("Verification code sent.")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
