package nl.noviaal.model.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Media;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.Tag;
import nl.noviaal.helper.Formatters;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ItemResponse {

  private final String id;
  private final String created;
  private final String updated;
  private final String type;
  private final String username;
  private String userId;
  private List<CommentResponse> comments;
  private String tags;
  private String likes;

  // Note content, leave null or empty to not include in JSON output
  private String title;
  private String body;

  // Media content, leave null or blank to not include in JSON output
  private String name;
  private String contentType;


  public void addComment(Comment comment) {
    comments.add(CommentResponse.ofComment(comment));
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public static ItemResponse from(Item item) {
    final var dtf = Formatters.dateTimeFormatter();
    final ItemResponse itemResponse = ItemResponse.builder()
      .id(item.getId().toString())
      .created(item.getCreated().format(dtf))
      .contentType(item.getClass().getSimpleName())
      .username(item.getAuthor().getName())
      .userId(item.getAuthor().getId().toString())
      .comments(item.getComments().stream().map(CommentResponse::ofComment).toList())
      .updated(item.getUpdated() != null ? item.getUpdated().format(dtf) : item.getCreated().format(dtf))
      .build();
    itemResponse.tags = item.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));
    if (item instanceof Note note) {
      itemResponse.setTitle(note.getTitle());
      itemResponse.setBody(note.getBody());
      itemResponse.setLikes(note.getLikeCount().toString());
    }
    if (item instanceof Media media) {
      itemResponse.setName(media.getName());
      itemResponse.setContentType(media.getContentType());
    }
    return itemResponse;
  }
}
