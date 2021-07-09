package nl.noviaal.config;

import nl.noviaal.domain.User;
import nl.noviaal.service.AuthService;
import nl.noviaal.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!mocked")
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

  private final AuthService authService;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public ApplicationReadyListener(
    AuthService authService, UserService userService, PasswordEncoder passwordEncoder
  ) {
    this.authService = authService;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    initDataStore();
  }

  @Transactional
  public void initDataStore() {
    if (userService.findAll().isEmpty()) {
      String encryptedPassword = passwordEncoder.encode("password");
      authService.register(User.builder()
                             .name("Tester")
                             .email("tester@test.com")
                             .password(encryptedPassword)
                             .build());
      authService.register(User.builder()
                             .name("Another")
                             .email("an@other.com")
                             .password(encryptedPassword)
                             .build());
      var admin = User.builder()
                    .name("Admin")
                    .email("admin@tester.com")
                    .password(encryptedPassword)
                    .roles("USER,ADMIN")
                    .build();
      authService.register(admin);
    }
  }
}
