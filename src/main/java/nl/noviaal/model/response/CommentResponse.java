package nl.noviaal.model.response;

import lombok.Data;
import nl.noviaal.domain.Comment;
import nl.noviaal.helper.Formatters;

@Data
public class CommentResponse {

  private final String comment;
  private final String created;
  private final String author;
  private final Integer stars;

  public static CommentResponse ofComment(Comment comment) {
    return new CommentResponse(
            comment.getComment(),
            comment.getCreated().format(Formatters.dateTimeFormatter()),
            comment.getAuthor().getName(),
            comment.getStars()
    );
  }
}
