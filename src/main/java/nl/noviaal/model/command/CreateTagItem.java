package nl.noviaal.model.command;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CreateTagItem {
  @NotNull
  private UUID itemId;

  public CreateTagItem(@NotNull UUID itemId) {
    this.itemId = itemId;
  }

  public UUID getItemId() {
    return itemId;
  }
}
