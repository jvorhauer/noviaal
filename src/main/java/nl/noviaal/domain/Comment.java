package nl.noviaal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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
  private UUID id;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  private Instant created;

  @NotBlank
  @Column(nullable = false)
  private String comment;

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
    if (this.created == null) {
      this.created = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
  }


  public UUID getId() { return id; }

  public Instant getCreated() { return created; }
  public void setCreated(Instant created) { this.created = created; }

  public String getComment() { return comment; }
  public void setComment(String comment) { this.comment = comment; }
}
