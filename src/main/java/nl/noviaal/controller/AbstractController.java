package nl.noviaal.controller;

import nl.noviaal.model.auth.UserDetailsImpl;
import org.springframework.security.core.Authentication;

public abstract class AbstractController {

  String getUserEmail(Authentication authentication) {
    if (authentication == null) {
      return "-";
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
  }
}
