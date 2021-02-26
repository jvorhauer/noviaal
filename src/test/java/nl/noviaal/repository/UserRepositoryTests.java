package nl.noviaal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.noviaal.NoviaalApplication;
import nl.noviaal.config.ApplicationReadyListener;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = NoviaalApplication.class
)
@Transactional
public class UserRepositoryTests {

  @Autowired
  UserRepository userRepository;

  @Autowired
  Setup setup;

  @BeforeEach
  void before() {
    setup.initDataStore();
  }

  @Test
  void saveUser_shouldResultInNonNullID() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    assertThat(frodo.getId()).isNotNull();
    assertThat(frodo.getName()).isEqualTo("Frodo");
    assertThat(frodo.getEmail()).isEqualTo("frodo@hobbiton.shire");
  }

  @Test
  void saveUser_shouldHaveExcpetedUserInFindAll() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    var list = userRepository.findAll();
    assertThat(list).hasSize(4);
  }

  @Test
  void saveSameUserTwice_shouldSucceed() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    var frodo2 = userRepository.saveAndFlush(frodo);
    assertThat(frodo2.getId()).isEqualTo(frodo.getId());
  }

  @Test
  void saveUserAndChangeNameShouldBeRenamed() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    userRepository.saveAndFlush(user);

    user.setName("Bilbo");
    var saved = userRepository.saveAndFlush(user);
    assertThat(saved.getName()).isEqualTo("Bilbo");
    assertThat(saved.getEmail()).isEqualTo("frodo@hobbiton.shire");
  }

  @Test
  void saveUserWithSpecificJoinedDate_shouldSucceed() {
    var ldt = LocalDateTime.of(2021, 1, 18, 9, 0);
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    user.setJoined(ZonedDateTime.of(ldt, ZoneId.of("CET")));
    var saved = userRepository.saveAndFlush(user);
    assertThat(saved.getJoined()).isEqualTo(ZonedDateTime.of(ldt, ZoneId.of("CET")));
  }

  @Test
  void saveUserWithExistingEmail_shouldResultInException() {
    var user1 = new User("Frodo", "frodo@hobbiton.shire", "password");
    userRepository.saveAndFlush(user1);
    var user2 = new User("User2", "frodo@hobbiton.shire", "password");
    assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(user2));
  }

  @Test
  void saveUserWithoutName_shouldThrowException() {
    var user = new User(null, "frodo@hobbiton.shire", "password");
    assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user));
  }

  @Test
  void saveUserWithoutEmail_shouldThrowException() {
    var user = new User("Frodo", null, "password");
    assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user));
  }

  @Test
  void saveUserWithOneNote_shouldSucceed() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    frodo.addNote(new Note("About Hobbits", "Hobbits are actually quite nice!"));
    var noted = userRepository.saveAndFlush(frodo);

    assertThat(noted.getNotes()).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  void saveUserWithNoteAndThenRemoveIt_shouldSucceed() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    frodo.addNote(new Note("About Hobbits", "Hobbits are actually quite nice!"));
    var noted = userRepository.saveAndFlush(frodo);
    assertThat(noted.getNotes()).hasSize(1);

    var note = noted.getNotes().iterator().next();
    assertThat(note.getId()).isNotNull();

    frodo.deleteNote(note);
    var notNoted = userRepository.saveAndFlush(noted);
    assertThat(notNoted.getNotes()).hasSize(0);

    var list = userRepository.findAll();
    assertThat(list).hasSize(4);

    var ofound = userRepository.findByEmail("frodo@hobbiton.shire");
    assertThat(ofound).isPresent();
    User found = ofound.get();
    assertThat(found.getName()).isEqualTo("Frodo");
    assertThat(found.getNotes()).hasSize(0);
  }

  @Test
  void saveUserAndFindByEmailShouldFindUser() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);

    var found = userRepository.findByEmail("frodo@hobbiton.shire");
    assertThat(found).isNotNull();
    assertThat(found.isPresent()).isTrue();
    assertThat(found.get().getId()).isEqualTo(frodo.getId());
  }
}
