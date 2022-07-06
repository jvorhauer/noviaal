package nl.noviaal.controller;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.stream.Collectors;

import nl.noviaal.domain.User;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.model.auth.JwtResponse;
import nl.noviaal.model.command.CreateUser;
import nl.noviaal.model.command.LoginUser;
import nl.noviaal.model.response.UserResponse;
import nl.noviaal.service.AuthService;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger("AuthController");

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
      logger.error("login: {}", loginRequest);
      throw new InvalidCommand("LoginUser");
    }
    JwtResponse res = authService.login(loginRequest.username(), loginRequest.password());
    return ResponseEntity.ok(res);
  }

  @PostMapping(value = "/register")
  public ResponseEntity<UserResponse> register(@RequestBody CreateUser createUser) {
    var errors = validator.validate(createUser);
    if (errors.size() > 0) {
      logger.error("register: invalid: {}", createUser);
      throw new InvalidCommand(
        "CreateUser" + errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "))
      );
    }

    String encryptedPassword = passwordEncoder.encode(createUser.password());
    User user = new User(Encode.forHtml(createUser.name()), Encode.forHtml(createUser.email()), encryptedPassword);
    User created = authService.register(user);
    return ResponseEntity.ok(UserResponse.ofUser(created));
  }
}
