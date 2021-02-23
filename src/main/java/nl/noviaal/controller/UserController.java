package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Follow;
import nl.noviaal.model.response.NoteResponse;
import nl.noviaal.model.response.UserResponse;
import nl.noviaal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController extends AbstractController {

  @Autowired
  public UserController(UserService userService) {
    super(userService);
  }

  @GetMapping(path = {"", "/"})
  public List<UserResponse> findAll(Authentication auth, Pageable pageable) {
    log.info("user: {}", getUserEmail(auth));
    return userService.findAll(pageable)
             .stream()
             .map(UserResponse::fromUser)
             .collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserResponse getById(@PathVariable("id") UUID id) {
    log.info("getById: {}", id);
    return UserResponse.fromUser(findUserById(id));
  }

  @DeleteMapping("/{id}")
  @Secured("ADMIN")
  public ResponseEntity<Object> delete(@PathVariable("id") UUID id) {
    log.info("delete: {}", id);
    var user = findUserById(id);
    userService.delete(user);
    return ResponseEntity.ok().body("deleted user with id " + id);
  }

  @GetMapping("/{id}/notes")
  public List<NoteResponse> notes(@PathVariable("id") UUID id) {
    var user = findUserById(id);
    return user.getNotes()
             .stream()
             .map(NoteResponse::fromNote)
             .collect(Collectors.toList());
  }

  @PostMapping("/follow/{followerId}")
  public ResponseEntity<URI> follow(@PathVariable("followerId") UUID followerId, Authentication authentication) {
    var user     = findCurrentUser(authentication);
    var follower = findUserById(followerId);
    log.info("follow: {} starts following {}", follower.getName(), user.getName());
    userService.follow(user, follower);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/followers")
  public List<UserResponse> followers(Authentication authentication) {
    return findCurrentUser(authentication).getFollowers()
             .stream()
             .map(Follow::getFollower)
             .map(UserResponse::fromUser)
             .collect(Collectors.toList());
  }


  @PutMapping("/{id}/promote")
  public ResponseEntity<?> promote(@PathVariable("id") UUID id, Authentication authentication) {
    assertIsAdmin(authentication);
    var user = findUserById(id);
    user.setRoles("USER,ADMIN");
    userService.save(user);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  /**
   * The Spring-provided annotations do not work as expected.
   * One of the many reasons to avoid annotations like the plague: you can't debug them.
   * @param authentication Authentication
   */
  private void assertIsAdmin(Authentication authentication) {
    var admin = findCurrentUser(authentication);
    if (!admin.getRoles().contains("ADMIN")) {
      throw new AccessDeniedException("User " + admin.getName() + " is not an admin!");
    }
  }
}
