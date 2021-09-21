package nl.noviaal.model.response;

import nl.noviaal.domain.Comment;
import nl.noviaal.helper.Formatters;

public record CommentResponse(
  String comment,
  String created,
  String author,
  Integer stars) {

  public static CommentResponse ofComment(Comment comment) {
    return new CommentResponse(
      comment.getComment(),
      comment.getCreated().format(Formatters.dateTimeFormatter()),
      comment.getAuthor().getName(),
      comment.getStars()
    );
  }
}
