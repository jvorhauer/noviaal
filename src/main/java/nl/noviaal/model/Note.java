package nl.noviaal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "note")
public class Note {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(length = 1024)
  private String body;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private User author;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  private Instant created;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
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


  public void setId(UUID id) { this.id = id; }
  public UUID getId() { return id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getBody() { return body; }
  public void setBody(String body) { this.body = body; }

  public User getAuthor() { return author; }
  public void setAuthor(User author) { this.author = author; }

  public ZonedDateTime getCreated() { return created.atZone(ZoneId.of("CET")); }
  public void setCreated(ZonedDateTime created) { this.created = created.toInstant(); }

  public Set<Comment> getComments() { return comments; }
  public void addComment(Comment comment) {
    comments.add(comment);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!(o instanceof Note)) { return false; }
    return id != null && id.equals(((Note) o).getId());
  }

  public int hashCode() { return getClass().hashCode(); }
}
