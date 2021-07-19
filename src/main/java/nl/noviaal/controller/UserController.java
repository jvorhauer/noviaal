package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Follow;
import nl.noviaal.domain.User;
import nl.noviaal.model.response.ItemResponse;
import nl.noviaal.model.response.UserDeletedResponse;
import nl.noviaal.model.response.UserFollowedResponse;
import nl.noviaal.model.response.UserResponse;
import nl.noviaal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    log.info("findAll: by user: {}", getUserEmail(auth));
    return userService.findAll(pageable)
      .stream()
      .map(UserResponse::ofUser)
      .collect(Collectors.toList());
  }

  @GetMapping(path = "/me")
  public UserResponse findMe(Authentication authentication) {
    log.info("findMe");
    return UserResponse.ofUser(findCurrentUser(authentication));
  }

  @GetMapping("/timeline")
  public List<ItemResponse> timeline(Authentication authentication) {
    User user = findCurrentUser(authentication);
    return userService.timeline(user).stream()
      .map(ItemResponse::ofItem)
      .collect(Collectors.toList());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserResponse getById(@PathVariable("id") UUID id) {
    log.info("getById: {}", id);
    return UserResponse.ofUser(findUserById(id));
  }

  @DeleteMapping("/{id}")
  public UserDeletedResponse delete(@PathVariable("id") UUID id, Authentication authentication) {
    assertIsAdmin(authentication);
    log.info("delete: {}", id);
    var user = findUserById(id);
    userService.delete(user);
    return UserDeletedResponse.ofUser(user);
  }

  @GetMapping("/items")
  public List<ItemResponse> notes(Authentication authentication) {
    log.info("notes");
    return findCurrentUser(authentication)
      .getItems()
      .stream()
      .map(ItemResponse::ofItem)
      .collect(Collectors.toList());
  }

  @PostMapping("/follow/{id}")
  public UserFollowedResponse follow(@PathVariable("id") UUID id, Authentication authentication) {
    var user = findCurrentUser(authentication);
    var follow = findUserById(id);
    log.info("follow: {} starts following {}", user.getName(), follow.getName());
    var followed = userService.follow(user, follow);
    return UserFollowedResponse.ofFollow(followed);
  }

  @GetMapping("/followers")
  public List<UserResponse> followers(Authentication authentication) {
    return findCurrentUser(authentication)
      .getFollowers()
      .stream()
      .map(Follow::getFollowed)
      .map(UserResponse::ofUser)
      .collect(Collectors.toList());
  }

  @GetMapping("/followed")
  public List<UserResponse> followed(Authentication authentication) {
    return findCurrentUser(authentication)
            .getFollowed()
            .stream()
            .map(Follow::getFollower)
            .map(UserResponse::ofUser)
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
}
