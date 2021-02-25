package nl.noviaal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "note")
public class Note extends BaseItem {

  @Column(nullable = false, length = 255)
  private String title;

  @Column(length = 1024)
  private String body;


  @PrePersist
  void prePersist() {
    id = UUID.randomUUID();
    if (created == null) {
      created = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
  }


  public Note() {}
  public Note(String title) {
    this.title = title;
  }
  public Note(String title, String body) {
    this(title);
    this.body = body;
  }


  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getBody() { return body; }
  public void setBody(String body) { this.body = body; }
}
