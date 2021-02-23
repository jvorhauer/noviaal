package nl.noviaal.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtResponse {
  private String token;
  private final String type = "Bearer";
  private UUID id;
  private String username;
  private String email;
  private List<String> roles;
}
