package nl.noviaal.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.noviaal.domain.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "followed_id")
  @JsonBackReference
  private User followed;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "follower_id")
  @JsonBackReference
  private User follower;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  private Instant since;


  public Follow(User user, User follower) {
    if (this.since == null) {
      this.since = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
    this.followed = user;
    this.follower = follower;
  }

  public UUID getFollowerId() { return follower.getId(); }
  public UUID getFollowedId() { return followed.getId(); }
}
