package nl.noviaal.model.response;

import nl.noviaal.domain.Comment;

import java.time.format.DateTimeFormatter;

public class CommentResponse {

  private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private String comment;
  private String created;
  private String author;

  public CommentResponse(String comment, String created, String author) {
    this.comment = comment;
    this.created = created;
    this.author = author;
  }

  public String getComment() {
    return comment;
  }

  public String getCreated() {
    return created;
  }

  public String getAuthor() {
    return author;
  }

  public static CommentResponse ofComment(Comment comment) {
    return new CommentResponse(comment.getComment(), DTF.format(comment.getCreated()), comment.getAuthor().getName());
  }
}
