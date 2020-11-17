package nl.novi.state;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "notes")
public class Note {
  @Id
  private UUID id;

  private ZonedDateTime created;
  private String title;
  private String body;

  public Note() {
    id = UUID.randomUUID();
  }
  public Note(ZonedDateTime created, String title, String body) {
    this(title, body);
    this.created = created;
  }
  public Note(String title, String body) {
    this();
    this.title = title;
    this.body = body;
  }

  @PrePersist
  protected void prePersist() {
    if (created == null) {
      created = ZonedDateTime.now();
    }
  }

  public UUID          getId()      { return id; }
  public ZonedDateTime getCreated() { return created; }
  public String        getTitle()   { return title; }
  public String        getBody()    { return body; }

  public void setId(UUID id)                    { this.id = id; }
  public void setCreated(ZonedDateTime created) { this.created = created; }
  public void setTitle(String title)            { this.title = title; }
  public void setBody(String body)              { this.body = body; }
}
