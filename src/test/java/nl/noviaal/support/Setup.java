package nl.noviaal.support;

import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Setup {

  private final UserRepository userRepo;

  public Setup(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Transactional
  public void initDataStore() {
    if (userRepo.findAll().isEmpty()) {
      userRepo.saveAndFlush(new User("Tester", "tester@test.com", "password"));
      userRepo.saveAndFlush(new User("Another", "an@other.com", "password"));

      var admin = new User("Admin", "admin@tester.com", "password");
      admin.setRoles("USER,ADMIN");
      var saved = userRepo.saveAndFlush(admin);
      var note = new Note("admin note", "as an admin I want a note");
      saved.addNote(note);
      note.setAuthor(saved);
      userRepo.saveAndFlush(saved);
    }
  }
}
