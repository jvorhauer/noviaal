package nl.noviaal.model.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateNote(@NotBlank @Size(max = 255) String title,
                         @NotBlank @Size(max = 1024) String body) {}
