package nl.novi.state;

import lombok.Data;
import nl.novi.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "notes")
@Data
public class Note {
  @Id
  private UUID id;

  private ZonedDateTime created;
  private String title;
  private String body;

  @ManyToOne(optional = false)
  @MapsId
  private User user;

  protected Note() {}
  public Note(final User user) {
    this();
    this.user = user;
  }
  public Note(final User user, final String title, final String body) {
    this(user);
    this.title = title;
    this.body = body;
  }

  @PrePersist
  protected void prePersist() {
    if (created == null) created = ZonedDateTime.now();
    if (id == null) id = UUID.randomUUID();
  }
}
