package nl.noviaal.controller;

import nl.noviaal.domain.User;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.model.auth.UserDetailsImpl;
import nl.noviaal.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.UUID;

public abstract class AbstractController {

  private final static String USER_CLASS = "org.springframework.security.core.userdetails.User";

  protected final UserService userService;
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public AbstractController(UserService userService) {
    this.userService = userService;
  }

  String getUserEmail(Authentication authentication) {
    if (authentication == null) {
      throw new AccessDeniedException("Authentication is null!");
    }
    return authentication.getPrincipal().getClass().getCanonicalName().equalsIgnoreCase(USER_CLASS) ?
             ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername() :
             ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
  }

  protected User findUserById(UUID id) {
    return userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  protected User findCurrentUser(Authentication authentication) {
    var email = getUserEmail(authentication);
    return userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }

  /**
   * The Spring-provided annotations do not work as expected.
   * One of the many reasons to avoid annotations like the plague: you can't debug them.
   * @param authentication Authentication
   * @throws AccessDeniedException if the logged in user does not have ADMIN role.
   */
  protected void assertIsAdmin(Authentication authentication) {
    var admin = findCurrentUser(authentication);
    if (!admin.getRoles().contains("ADMIN")) {
      throw new AccessDeniedException("User " + admin.getName() + " is not an admin!");
    }
  }

  protected <T> boolean isInvalid(T t) {
    return !validator.validate(t).isEmpty();
  }
}
