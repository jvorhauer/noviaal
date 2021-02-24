package nl.noviaal.service;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.NoteNotFoundException;
import nl.noviaal.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteService {
  private final NoteRepository noteRepository;

  @Transactional(readOnly = true)
  public Note find(UUID id) {
    return noteRepository.findById(id)
             .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " not found"));
  }

  @Transactional
  public Note save(Note note) {
    return noteRepository.save(note);
  }

  @Transactional
  public Optional<Comment> addCommentToNote(Note note, User user, Comment comment) {
    note.addComment(comment);
    comment.setAuthor(user);
    Note saved = save(note);
    return saved.getComments().stream().min(Comparator.comparing(Comment::getCreated));
  }
}
