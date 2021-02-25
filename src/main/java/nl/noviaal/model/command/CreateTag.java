package nl.noviaal.model.command;

import javax.validation.constraints.NotBlank;

public class CreateTag {

  @NotBlank
  private final String name;

  public CreateTag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
