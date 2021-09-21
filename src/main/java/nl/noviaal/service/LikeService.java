package nl.noviaal.service;

import java.util.List;
import java.util.UUID;

import nl.noviaal.domain.Like;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.repository.LikeRepository;
import nl.noviaal.repository.NoteRepository;
import nl.noviaal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
  private final LikeRepository likeRepository;
  private final NoteRepository noteRepository;
  private final UserRepository userRepository;

  @Autowired
  public LikeService(LikeRepository likeRepository, NoteRepository noteRepository, UserRepository userRepository) {
    this.likeRepository = likeRepository;
    this.noteRepository = noteRepository;
    this.userRepository = userRepository;
  }

  public Like save(Like like) {
    return likeRepository.save(like);
  }

  public Like save(UUID userId, UUID noteId) {
    return save(new Like(userId, noteId));
  }

  public List<Note> liked(User user) {
    var likedNoteIds = likeRepository.findByUserId(user.getId()).map(Like::getNoteId);
    return noteRepository.findAllById(likedNoteIds);
  }

  public List<User> liked(Note note) {
    var likedByIds = likeRepository.findByNoteId(note.getId()).map(Like::getUserId);
    return userRepository.findAllById(likedByIds);
  }
}
