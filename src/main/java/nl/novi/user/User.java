package nl.novi.user;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

  @Id
  private String email;
  private String password;
  private Boolean enabled;

  @Enumerated(EnumType.STRING)
  private Role role;

  private ZonedDateTime created;

  public User() {
    created = ZonedDateTime.now();
    enabled = true;
  }
  public User(@NonNull String email, @NonNull String password) {
    this();
    this.email = email;
    this.password = password;
  }
  public User(@NonNull String email, @NonNull String password, @NonNull Role role) {
    this(email, password);
    this.role = role;
  }
}
