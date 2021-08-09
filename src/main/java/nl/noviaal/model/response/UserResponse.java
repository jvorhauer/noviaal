package nl.noviaal.model.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import nl.noviaal.domain.User;
import nl.noviaal.helper.Formatters;

@Value
@AllArgsConstructor
public class UserResponse {

  String id;
  String name;
  String email;
  String joined;

  public static UserResponse ofUser(User user) {
    return new UserResponse(
      user.getId().toString(),
      user.getName(),
      user.getEmail(),
      user.getJoined().format(Formatters.dateTimeFormatter())
    );
  }
}
