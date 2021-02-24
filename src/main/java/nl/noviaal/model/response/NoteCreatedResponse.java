package nl.noviaal.model.response;

import nl.noviaal.domain.Note;

import java.util.UUID;

public class NoteCreatedResponse {
  private UUID id;
  private String title;

  public NoteCreatedResponse(UUID id, String title) {
    this.id = id;
    this.title = title;
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public static NoteCreatedResponse ofNote(Note note) {
    return new NoteCreatedResponse(note.getId(), note.getTitle());
  }
}
