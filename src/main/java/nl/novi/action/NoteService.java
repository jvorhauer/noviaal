package nl.novi.action;

import nl.novi.state.Note;
import nl.novi.state.NoteRepository;
import nl.novi.view.NoteForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class NoteService {

  private final NoteRepository notes;

  public NoteService(NoteRepository notes) {
    this.notes = notes;
  }

  @Transactional
  public Note create(final NoteForm form) {
    var note = new Note(ZonedDateTime.now(), form.getTitle(), form.getBody());
    return notes.save(note);
  }

  @Transactional(readOnly = true)
  public List<Note> all() {
    return notes.findAll();
  }
}
