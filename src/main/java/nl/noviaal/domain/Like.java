package nl.noviaal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Data
@Table(name = "likes")
public class Like {
  @Id
  private UUID id;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  private Instant liked;

  @NotNull
  private UUID userId;
  @NotNull
  private UUID noteId;

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
    if (this.liked == null) {
      this.liked = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
  }

  public Like() {}
  public Like(UUID userId, UUID noteId) {
    this.userId = userId;
    this.noteId = noteId;
  }
}
