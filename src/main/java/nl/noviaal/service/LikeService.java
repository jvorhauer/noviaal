package nl.noviaal.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.Like;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.repository.LikeRepository;
import nl.noviaal.repository.NoteRepository;
import nl.noviaal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LikeService {
  private final LikeRepository likeRepository;
  private final NoteRepository noteRepository;
  private final UserRepository userRepository;

  public Like save(Like like) {
    return likeRepository.save(like);
  }

  public Like save(UUID userId, UUID noteId) {
    return save(new Like(userId, noteId));
  }

  public List<Note> liked(User user) {
    var likedNoteIds = likeRepository.findByUserId(user.getId())
      .stream()
      .map(Like::getNoteId)
      .collect(Collectors.toList());
    return noteRepository.findAllById(likedNoteIds);
  }

  public List<User> liked(Note note) {
    var likedByIds = likeRepository.findByNoteId(note.getId())
      .stream()
      .map(Like::getUserId)
      .collect(Collectors.toList());
    return userRepository.findAllById(likedByIds);
  }
}
