package nl.noviaal.model.response;

import lombok.Value;
import nl.noviaal.model.Note;

import java.time.format.DateTimeFormatter;

@Value
public class NoteResponse {

  private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  String id;
  String title;
  String body;
  String created;
  String author;
  String authorId;


  public static NoteResponse fromNote(Note note) {
    return new NoteResponse(
      note.getId().toString(),
      note.getTitle(),
      note.getBody(),
      note.getCreated().format(DTF),
      note.getAuthor().getName(),
      note.getAuthor().getId().toString()
    );
  }
}
