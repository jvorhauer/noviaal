package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.NoteNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class NoteServiceTests {

  @Autowired
  private NoteService noteService;

  @Autowired
  private UserService userService;

  @Test
  void whenNewNoteSaved_thenFindAll_shouldFindOneNote() {
    var user = userService.save(new User("Tester", "tester@test.com", "password"));
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
