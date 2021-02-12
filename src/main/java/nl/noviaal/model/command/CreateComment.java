package nl.noviaal.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateComment {
  @NotBlank
  String body;
}
