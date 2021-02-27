package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.User;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.model.auth.JwtResponse;
import nl.noviaal.model.command.CreateUser;
import nl.noviaal.model.command.LoginUser;
import nl.noviaal.model.response.UserResponse;
import nl.noviaal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;
  private final Validator validator;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(AuthService authService, Validator validator, PasswordEncoder pe) {
    this.authService = authService;
    this.validator   = validator;
    this.passwordEncoder = pe;
  }

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtResponse> login(@RequestBody LoginUser loginRequest) {
    if (!validator.validate(loginRequest).isEmpty()) {
      log.error("login: {}", loginRequest);
      throw new InvalidCommand("LoginUser");
    }
    JwtResponse res = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "/register")
  public ResponseEntity<UserResponse> register(@RequestBody CreateUser createUser) {
    var errors = validator.validate(createUser);
    if (errors.size() > 0) {
      log.error("register: invalid: {}", createUser);
      throw new InvalidCommand(
        "CreateUser" + errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "))
      );
    }

    String encryptedPassword = passwordEncoder.encode(createUser.getPassword());
    User user = User.builder()
                  .name(createUser.getName())
                  .email(createUser.getEmail())
                  .password(encryptedPassword)
                  .build();
    User created = authService.register(user);
    return ResponseEntity.ok(UserResponse.ofUser(created));
  }
}
