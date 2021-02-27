package nl.noviaal.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateTag {

  @NotBlank
  @Size(max = 255)
  private final String name;

  @JsonCreator
  public CreateTag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
