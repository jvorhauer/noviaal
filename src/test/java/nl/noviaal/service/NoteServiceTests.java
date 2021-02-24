package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.noviaal.config.ApplicationReadyListener;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.NoteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteServiceTests {

  @Autowired
  private NoteService noteService;

  @Autowired
  private UserService userService;

  @Autowired
  private ApplicationReadyListener applicationReadyListener;

  @BeforeEach
  void before() {
    applicationReadyListener.initDataStore();
  }

  @Test
  @Transactional
  void whenNewNoteSaved_thenFindAll_shouldFindOneNote() {
    Optional<User> ouser = userService.findByEmail("tester@test.com");
    assertThat(ouser).isPresent();
    User user = ouser.get();
    assertThat(user.getNotes()).isNotNull();
    assertThat(user.getNotes()).hasSizeGreaterThanOrEqualTo(0);
    user.addNote(new Note("test title", "test body"));
    var saved = userService.save(user);

    assertThat(saved.getNotes()).hasSize(1);
    var onote = saved.getNotes().stream().findFirst();
    assertThat(onote).isPresent();
    var note = onote.get();
    assertThat(note.getTitle()).isEqualTo("test title");

    var retrieved = noteService.find(note.getId());
    assertThat(retrieved.getTitle()).isEqualTo("test title");
  }

  @Test
  void whenLookingForNonExistentNote_thenFind_shouldThrowNoteNotFoundExceptioon() {
    var nonExistingId = UUID.randomUUID();
    assertThatThrownBy(() -> noteService.find(nonExistingId))
      .isInstanceOf(NoteNotFoundException.class)
      .hasMessage("Note with id " + nonExistingId + " not found");
  }
}
