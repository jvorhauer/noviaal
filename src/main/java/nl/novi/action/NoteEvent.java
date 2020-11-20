package nl.novi.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.novi.state.Note;

@Data
@AllArgsConstructor
public class NoteEvent {
  private Note note;
  private boolean success;
}
