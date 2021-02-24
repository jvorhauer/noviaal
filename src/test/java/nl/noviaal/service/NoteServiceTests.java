package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.NoteNotFoundException;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteServiceTests {

  private final NoteService noteService;
  private final UserService userService;
  private final Setup setup;

  @BeforeEach
  void before() {
    setup.initDataStore();
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
