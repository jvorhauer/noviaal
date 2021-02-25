package nl.noviaal.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.domain.Follow;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.repository.FollowRepository;
import nl.noviaal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;


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
    if (user.getId() == null && userRepository.findByEmail(user.getEmail()).isPresent()) {
      log.error("save: Email address {} already in use!", user.getEmail());
      throw new EmailAddressInUseException(user.getEmail());
    }
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(@NonNull UUID id) {
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByEmail(@NonNull String email) {
    return userRepository.findByEmail(email);
  }

  @Transactional
  public void delete(@NonNull User user) {
    userRepository.delete(user);
  }

  @Transactional
  public void addNote(User user, Note note) {
    user.addItem(note);
    userRepository.save(user);
  }


  @Transactional
  public Follow follow(User user, User follow) {
    log.info("follow: user {} has {} followers", user.getName(), user.getFollowers().size());
    Follow following = new Follow(user, follow);
    Follow savedFollow = followRepository.save(following);
    user.addFollower(savedFollow);
    User savedUser = userRepository.save(user);
    log.info("follow: user {} now has {} follower(s)", savedUser.getName(), savedUser.getFollowers().size());
    return savedFollow;
  }
}
