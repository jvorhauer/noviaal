package nl.novi.view;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class NoteForm {
  @Pattern(regexp = "^[a-zA-Z0-9 ]+$")
  private String title;
  private String body;

  public String toJson() {
    return "{\"title\":\"" + title + "\",\"body\":\"" + (body != null ? body : "") + "\"}";
  }
}
