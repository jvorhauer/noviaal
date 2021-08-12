package nl.noviaal.model.response;

import nl.noviaal.domain.User;
import nl.noviaal.helper.Formatters;

public record UserResponse(
  String id,
  String name,
  String email,
  String joined,
  Integer noteCount,
  Integer follows,
  Integer followed
) {

  public static UserResponse ofUser(User user) {
    return new UserResponse(
      user.getId().toString(),
      user.getName(),
      user.getEmail(),
      user.getJoined().format(Formatters.dateTimeFormatter()),
      user.getNotes().size(),
      user.getFollowed().size(),
      user.getFollowers().size()
    );
  }
}
