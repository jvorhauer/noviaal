package nl.noviaal.model.response;

import nl.noviaal.domain.Follow;

import java.util.UUID;

public class UserFollowedResponse {
  private UUID userId;
  private UUID followedId;

  public UserFollowedResponse(UUID userId, UUID followedId) {
    this.userId = userId;
    this.followedId = followedId;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getFollowedId() {
    return followedId;
  }

  public static UserFollowedResponse ofFollow(Follow follow) {
    return new UserFollowedResponse(follow.getFollowedId(), follow.getFollowerId());
  }
}
