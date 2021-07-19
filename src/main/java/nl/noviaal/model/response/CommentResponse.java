package nl.noviaal.model.response;

import lombok.Data;
import nl.noviaal.domain.Comment;

import java.time.format.DateTimeFormatter;

@Data
public class CommentResponse {

  private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final String comment;
  private final String created;
  private final String author;
  private final Integer stars;

  public static CommentResponse ofComment(Comment comment) {
    return new CommentResponse(
            comment.getComment(),
            comment.getCreated().format(DTF),
            comment.getAuthor().getName(),
            comment.getStars()
    );
  }
}
