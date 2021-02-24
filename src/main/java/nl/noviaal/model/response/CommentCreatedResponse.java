package nl.noviaal.model.response;

import nl.noviaal.domain.Comment;

import java.util.UUID;

public class CommentCreatedResponse {
  private final UUID id;
  private final String comment;

  public CommentCreatedResponse(UUID id, String comment) {
    this.id = id;
    this.comment = comment;
  }

  public UUID getId() {
    return id;
  }

  public String getComment() {
    return comment;
  }

  public static CommentCreatedResponse ofComment(Comment comment) {
    return new CommentCreatedResponse(comment.getId(), comment.getComment());
  }
}
