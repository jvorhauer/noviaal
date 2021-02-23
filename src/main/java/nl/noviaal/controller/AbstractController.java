package nl.noviaal.controller;

import nl.noviaal.domain.User;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.model.auth.UserDetailsImpl;
import nl.noviaal.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public abstract class AbstractController {

  protected final UserService userService;

  public AbstractController(UserService userService) {
    this.userService = userService;
  }

  String getUserEmail(Authentication authentication) {
    if (authentication == null) {
      throw new AccessDeniedException("Authentication is null!");
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
  }

  protected User findUserById(UUID id) {
    return userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  protected User findCurrentUser(Authentication authentication) {
    var email = getUserEmail(authentication);
    return userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }
}
