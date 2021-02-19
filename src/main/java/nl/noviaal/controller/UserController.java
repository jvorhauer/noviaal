package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.domain.Follow;
import nl.noviaal.domain.User;
import nl.noviaal.model.response.NoteResponse;
import nl.noviaal.model.response.UserResponse;
import nl.noviaal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  @GetMapping(path = {"", "/"})
  public List<UserResponse> findAll(Authentication auth) {
    log.info("user: {}", getUserEmail(auth));
    return userService.findAll()
             .stream()
             .map(UserResponse::fromUser)
             .collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserResponse getById(@PathVariable("id") UUID id) {
    log.info("getById: {}", id);
    return userService.findById(id)
             .map(UserResponse::fromUser)
             .orElseThrow(() -> new UserNotFoundException(id));
  }

  @DeleteMapping("/{id}")
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

  @PostMapping("/{id}/follow/{followerId}")
  public ResponseEntity<URI> follow(@PathVariable("id") UUID id, @PathVariable("followerId") UUID followerId) {
    var user     = findUserById(id);
    var follower = findUserById(followerId);
    log.info("follow: {} starts following {}", follower.getName(), user.getName());
    userService.follow(user, follower);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}/followers")
  public List<UserResponse> followers(@PathVariable("id") UUID id) {
    return findUserById(id).getFollowers()
             .stream()
             .map(Follow::getFollower)
             .map(UserResponse::fromUser)
             .collect(Collectors.toList());
  }


  private User findUserById(UUID id) {
    return userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }
}
