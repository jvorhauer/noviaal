package nl.novi.user;

import lombok.Data;
import nl.novi.state.Note;
import org.springframework.lang.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

  @Id
  private UUID    id;
  private String  email;
  private String  password;
  private Boolean enabled;

  @Enumerated(EnumType.STRING)
  private Role role;

  private ZonedDateTime created;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Note> notes;


  public User() {
    id      = UUID.randomUUID();
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
