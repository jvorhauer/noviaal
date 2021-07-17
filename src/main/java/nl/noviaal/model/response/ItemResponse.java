package nl.noviaal.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Media;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.Tag;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ItemResponse {

    private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String id;
    private final String created;
    private final String updated;
    private final String type;
    private final String username;
    private String userId;
    private final List<CommentResponse> comments = new ArrayList<>();
    private String tags;

    // Note content, leave null or empty to not include in JSON output
    private String title;
    private String body;

    // Media content, leave null or blank to not include in JSON output
    private String name;
    private String contentType;


    public void addComment(Comment comment) {
        comments.add(CommentResponse.ofComment(comment));
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static ItemResponse ofItem(Item item) {
        final ItemResponse itemResponse = ItemResponse.builder()
                .id(item.getId().toString())
                .created(item.getCreated().format(DTF))
                .contentType(item.getClass().getSimpleName())
                .name(item.getAuthor().getName())
                .userId(item.getAuthor().getId().toString())
                .updated(item.getUpdated() != null ? item.getUpdated().format(DTF) : item.getCreated().format(DTF))
                .build();
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
