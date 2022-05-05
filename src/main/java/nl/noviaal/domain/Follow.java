package nl.noviaal.domain;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Table(name = "follow")
@Data
public class Follow {
  @Id
  @Column(columnDefinition = "uuid")
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


  public Follow() {}

  public Follow(User user, User follower) {
    if (this.since == null) {
      this.since = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
    this.followed = user;
    this.follower = follower;
  }

  public Follow(UUID id, User followed, User follower, @NotNull Instant since) {
    this.id = id;
    this.followed = followed;
    this.follower = follower;
    this.since = since;
  }

  public User getFollower() { return follower; }
  public User getFollowed() { return followed; }

  public UUID getFollowerId() { return follower.getId(); }
  public UUID getFollowedId() { return followed.getId(); }
}
