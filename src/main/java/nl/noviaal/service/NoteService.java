package nl.noviaal.service;

import lombok.RequiredArgsConstructor;
import nl.noviaal.exception.NoteNotFoundException;
import nl.noviaal.domain.Note;
import nl.noviaal.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteService {
  private final NoteRepository noteRepository;

  public List<Note> findAll() {
    return noteRepository.findAll();
  }

  public Note find(UUID id) {
    return noteRepository.findById(id)
             .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " not found"));
  }
}
