package nl.noviaal.service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import nl.noviaal.domain.Follow;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.repository.FollowRepository;
import nl.noviaal.repository.NoteRepository;
import nl.noviaal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger("UserService");

  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final NoteRepository noteRepository;

  @Autowired
  public UserService(UserRepository userRepository, FollowRepository followRepository, NoteRepository noteRepository) {
    this.userRepository = userRepository;
    this.followRepository = followRepository;
    this.noteRepository = noteRepository;
  }


  @Transactional(readOnly = true)
  public Page<User> findAll(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Page<User> findAll() {
    return findAll(PageRequest.of(0, Integer.MAX_VALUE));
  }

  @Transactional
  public User save(@Valid User user) {
    Objects.requireNonNull(user, "save: user is required");
    if (user.getId() == null && userRepository.findByEmail(user.getEmail()).isPresent()) {
      logger.error("save: Email address {} already in use!", user.getEmail());
      throw new EmailAddressInUseException(user.getEmail());
    }
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(UUID id) {
    Objects.requireNonNull(id, "findById: id is required");
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    Objects.requireNonNull(email, "findByEmail: email is required");
    return userRepository.findByEmail(email);
  }

  @Transactional
  public void delete(User user) {
    Objects.requireNonNull(user, "delete: user is required");
    userRepository.delete(user);
  }

  @Transactional
  public Note addNote(User user, String title, String body) {
    Objects.requireNonNull(user, "addNote: user is required");
    Objects.requireNonNull(title, "addNote: title is required");
    Objects.requireNonNull(body, "addNote: body is required");
    return noteRepository.save(new Note(title, body).claim(user));
  }


  @Transactional
  public Follow follow(User user, User follow) {
    Objects.requireNonNull(user, "follow: user is required");
    Objects.requireNonNull(follow, "follow: follow is required");
    logger.info("follow: user {} has {} followers", user.getName(), user.getFollowers().size());
    Follow following = new Follow(user, follow);
    Follow savedFollow = followRepository.save(following);
    user.addFollower(savedFollow);
    User savedUser = userRepository.save(user);
    logger.info("follow: user {} now has {} follower(s)", savedUser.getName(), savedUser.getFollowers().size());
    return savedFollow;
  }


  public Page<Item> timeline(User user, Pageable pageable) {
    Objects.requireNonNull(user, "timeline: user is required");
    List<User> following = user.getFollowed().stream().map(Follow::getFollower).collect(Collectors.toList());
    following.add(user);    // also display my own notes
    return noteRepository.timeline(following, pageable);
  }
}
