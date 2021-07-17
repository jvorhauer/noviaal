package nl.noviaal.model.command;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Value
public class UpdateNote {
    @NotNull
    UUID id;

    @NotBlank
    @Size(max = 255)
    String title;

    @NotBlank
    String body;

    @NotNull
    UUID userId;
}
