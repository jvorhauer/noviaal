package nl.noviaal.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Media;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.Tag;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemResponse {

    private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String id;
    private final String created;
    private final String type;
    private final String username;
    private final List<CommentResponse> comments = new ArrayList<>();
    private String tags;

    // Note content, leave null or empty to not include in JSON output
    private String title;
    private String body;

    // Media content, leave null or blank to not include in JSON output
    private String name;
    private String contentType;


    public ItemResponse(String id, String created, String type, String username) {
        this.id = id;
        this.created = created;
        this.type = type;
        this.username = username;
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

    public String getUsername() {
      return username;
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

    public void addComment(Comment comment) {
        comments.add(CommentResponse.ofComment(comment));
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public static ItemResponse ofItem(Item item) {
        final ItemResponse itemResponse = new ItemResponse(
                item.getId().toString(),
                item.getCreated().format(DTF),
                item.getClass().getSimpleName(),
                item.getAuthor().getName()
        );
        for (Comment comment : item.getComments()) {
            itemResponse.addComment(comment);
        }
        itemResponse.tags = item.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));
        if (item instanceof Note note) {
            itemResponse.setTitle(note.getTitle());
            itemResponse.setBody(note.getBody());
        }
        if (item instanceof Media media) {
            itemResponse.setName(media.getName());
            itemResponse.setContentType(media.getContentType());
        }
        return itemResponse;
    }
}
