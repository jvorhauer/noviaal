package nl.noviaal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.noviaal.model.Note;
import nl.noviaal.model.User;
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

@SpringBootTest
@Transactional
public class UserRepositoryTests {

  @Autowired
  UserRepository userRepository;

  @Test
  void saveUserShouldResultInNonNullID() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    assertThat(frodo.getId()).isNotNull();
    assertThat(frodo.getName()).isEqualTo("Frodo");
    assertThat(frodo.getEmail()).isEqualTo("frodo@hobbiton.shire");
  }

  @Test
  void saveUserShouldHaveOneUserInFindAll() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    var list = userRepository.findAll();
    assertThat(list).hasSize(1);
    assertThat(list.iterator().next().getId()).isEqualTo(frodo.getId());
  }

  @Test
  void saveSameUserTwiceShouldSucceed() {
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
  void saveUserWithSpecificJoinedDate() {
    var ldt = LocalDateTime.of(2021, 1, 18, 9, 0);
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    user.setJoined(ZonedDateTime.of(ldt, ZoneId.of("CET")));
    var saved = userRepository.saveAndFlush(user);
    assertThat(saved.getJoined()).isEqualTo(ZonedDateTime.of(ldt, ZoneId.of("CET")));
  }

  @Test
  void saveUserWithExistingEmailShouldResultInException() {
    var user1 = new User("Frodo", "frodo@hobbiton.shire", "password");
    userRepository.saveAndFlush(user1);
    var user2 = new User("User2", "frodo@hobbiton.shire", "password");
    assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(user2));
  }

  @Test
  void saveUserWithoutNameShouldThrowException() {
    var user = new User(null, "frodo@hobbiton.shire", "password");
    assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user));
  }

  @Test
  void saveUserWithoutEmailShouldThrowException() {
    var user = new User("Frodo", null, "password");
    assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user));
  }


  @Test
  void saveUserWithOneNoteShouldSucceed() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var frodo = userRepository.saveAndFlush(user);
    frodo.addNote(new Note("About Hobbits", "Hobbits are actually quite nice!"));
    var noted = userRepository.saveAndFlush(frodo);

    assertThat(noted.getNotes()).hasSize(1);
  }

  @Test
  void saveUserWithNoteAndThenRemoveItShouldSucceed() {
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
    assertThat(list).hasSize(1);

    var found = list.iterator().next();
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


  @BeforeEach
  public void before() {
    userRepository.deleteAll();
    userRepository.flush();
  }
}
