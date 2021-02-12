package nl.noviaal.model.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import nl.noviaal.model.User;

import java.time.format.DateTimeFormatter;

@Value
@AllArgsConstructor
public class UserResponse {

  private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  String id;
  String name;
  String email;
  String joined;

  public static UserResponse fromUser(User user) {
    return new UserResponse(
      user.getId().toString(),
      user.getName(),
      user.getEmail(),
      user.getJoined().format(DTF)
    );
  }
}
