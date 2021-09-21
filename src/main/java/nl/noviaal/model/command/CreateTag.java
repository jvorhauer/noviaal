package nl.noviaal.model.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateTag(@NotBlank @Size(max = 255) String name) {}
