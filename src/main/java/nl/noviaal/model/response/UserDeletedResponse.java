package nl.noviaal.model.response;

import nl.noviaal.domain.User;

import java.util.UUID;

public class UserDeletedResponse {
  private final UUID   id;
  private final String email;
  private final String name;

  public UserDeletedResponse(UUID id, String email, String name) {
    this.id = id;
    this.email = email;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public static UserDeletedResponse ofUser(User user) {
    return new UserDeletedResponse(user.getId(), user.getEmail(), user.getName());
  }
}
