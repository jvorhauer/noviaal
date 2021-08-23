package nl.noviaal.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateNote {
  @NotBlank
  @Size(max = 255)
  String title;

  @NotBlank
  @Size(max = 1024)
  String body;
}
