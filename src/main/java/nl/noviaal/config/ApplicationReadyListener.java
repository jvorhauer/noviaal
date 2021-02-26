package nl.noviaal.config;

import nl.noviaal.domain.User;
import nl.noviaal.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

  private final UserRepository userRepo;

  public ApplicationReadyListener(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    initDataStore();
  }

  @Transactional
  public void initDataStore() {
    if (userRepo.findAll().isEmpty()) {
      userRepo.save(new User("Tester", "tester@test.com", "passwrd"));
      userRepo.save(new User("Another", "an@other.com", "password"));
      var admin = new User("Admin", "admin@tester.com", "password");
      admin.setRoles("USER,ADMIN");
      userRepo.save(admin);
    }
  }
}
