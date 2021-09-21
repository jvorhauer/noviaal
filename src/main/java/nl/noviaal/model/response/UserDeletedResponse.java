package nl.noviaal.model.response;

import nl.noviaal.domain.User;

import java.util.UUID;

public record UserDeletedResponse(UUID id, String email, String name) {
  public static UserDeletedResponse ofUser(User user) {
    return new UserDeletedResponse(user.getId(), user.getEmail(), user.getName());
  }
}
