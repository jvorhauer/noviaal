package nl.noviaal.model.command;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record CreateComment(@NotBlank String comment, @Min(1) @Max(5) Integer stars) {}
