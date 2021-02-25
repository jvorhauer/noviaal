package nl.noviaal.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Media;
import nl.noviaal.domain.Note;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemResponse {

  private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final String id;
  private final String created;
  private final String type;

  // Note content, leave null or empty to not include in JSON output
  private String title;
  private String body;

  // Media content, leave null or blank to not include in JSON output
  private String name;
  private String contentType;


  public ItemResponse(String id, String created, String type) {
    this.id = id;
    this.created = created;
    this.type = type;
  }
  public ItemResponse(UUID id, ZonedDateTime created, String type) {
    this.id = id.toString();
    this.created = created.format(DTF);
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public String getCreated() {
    return created;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  public String getName() {
    return name;
  }

  public String getContentType() {
    return contentType;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public static ItemResponse ofItem(Item item) {
    final ItemResponse itemResponse = new ItemResponse(
      item.getId().toString(),
      item.getCreated().format(DTF),
      item.getClass().getSimpleName()
    );
    if (item instanceof Note) {
      Note note = (Note) item;
      itemResponse.setTitle(note.getTitle());
      itemResponse.setBody(note.getBody());
    }
    if (item instanceof Media) {
      Media media = (Media) item;
      itemResponse.setName(media.getName());
      itemResponse.setContentType(media.getContentType());
    }
    return itemResponse;
  }
}
