package nl.noviaal.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Value
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateComment {
  @NotBlank
  String comment;

  @Min(1)
  @Max(5)
  Integer stars;
}
