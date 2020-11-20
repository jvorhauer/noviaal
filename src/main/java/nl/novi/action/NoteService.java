package nl.novi.action;

import lombok.extern.slf4j.Slf4j;
import nl.novi.state.Note;
import nl.novi.state.NoteRepository;
import nl.novi.user.UserRepository;
import nl.novi.view.NoteForm;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class NoteService {

  private final NoteRepository notes;
  private final UserRepository users;
  private final ApplicationEventPublisher publisher;

  public NoteService(NoteRepository notes, UserRepository users, ApplicationEventPublisher publisher) {
    this.notes = notes;
    this.users = users;
    this.publisher = publisher;
  }

  @Transactional
  public void create(final NoteForm form, final String username) {
    var ouser = users.findByEmail(username);
    if (ouser.isDefined()) {
      var user = ouser.get();
      var note = new Note(user, form.getTitle(), form.getBody());
      user.getNotes().add(note);
      var saved = users.save(user);
      if (publisher != null) {
        log.info("publishing event");
        publisher.publishEvent(new NoteEvent(note, true));
      } else {
        log.error("publisher is null!");
      }
    }
  }

  @Transactional(readOnly = true)
  public List<Note> all() {
    return notes.findAll();
  }
}
