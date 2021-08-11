package nl.noviaal.config;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.User;
import nl.noviaal.repository.UserRepository;
import nl.noviaal.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Profile("!mocked")
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger logger = LoggerFactory.getLogger("ApplicationReadyListener");

  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepo;

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    if (isEmptyDataStore()) {
      initDataStore();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      logger.info("onApplicationEvent: users {}created", isEmptyDataStore() ? "NOT " : "");
    } else {
      logger.warn("onApplicationEvent: already found one or more users in the test db???");
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void initDataStore() {
    String encryptedPassword = passwordEncoder.encode("password");
    authService.register(User.builder().name("Tester").email("tester@test.com").password(encryptedPassword).build());
    authService.register(User.builder().name("Another").email("an@other.com").password(encryptedPassword).build());
    authService.register(User.builder().name("Admin").email("admin@tester.com").password(encryptedPassword).roles("USER,ADMIN").build());
  }

  @Transactional(readOnly = true)
  public boolean isEmptyDataStore() {
    return userRepo.findAll().isEmpty();
  }
}
