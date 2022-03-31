package nl.noviaal.service;

import java.util.UUID;

import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.NoteNotFoundException;
import nl.noviaal.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {
  private final NoteRepository noteRepository;

  @Autowired
  public NoteService(NoteRepository noteRepository) {
    this.noteRepository = noteRepository;
  }

  @Transactional(readOnly = true)
  public Note find(UUID id) {
    return noteRepository.findById(id)
             .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " not found"));
  }

  @Transactional
  public Note save(Note note) {
    note.setUpdatedToNow();
    return noteRepository.save(note);
  }

  @Transactional(readOnly = true)
  public Page<Item> getNotesPageForUser(User user, Pageable pageable) {
    return noteRepository.paginateNotes(user, pageable);
  }

  @Transactional
  public Note addCommentToNote(Note note, User user, Comment comment) {
    note.addComment(comment);
    comment.setAuthor(user);
    return save(note);
  }
}
