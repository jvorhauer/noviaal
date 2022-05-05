package nl.noviaal.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment")
public class Comment {
  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  private Instant created = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();

  @NotBlank
  @Column(nullable = false)
  private String comment;

  @Min(0)
  @Max(5)
  @NotNull
  @Column(nullable = false)
  private Integer stars = 0;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private User author;

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
    if (this.created == null) {
      this.created = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
  }

  public Comment() {}
  public Comment(String comment) {
    this.comment = comment;
  }
  public Comment(String comment, Integer stars) {
    this(comment);
    this.stars = stars;
  }

  public UUID getId() { return id; }

  public ZonedDateTime getCreated() { return created.atZone(ZoneId.of("CET")); }
  public void setCreated(Instant created) { this.created = created; }

  public String getComment() { return comment; }
  public void setComment(String comment) { this.comment = comment; }

  public void setStars(int stars) { this.stars = stars; }
  public int getStars() { return stars; }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }
}
