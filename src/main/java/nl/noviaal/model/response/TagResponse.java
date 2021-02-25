package nl.noviaal.model.response;

import nl.noviaal.domain.Tag;

import java.util.UUID;

public class TagResponse {
  private final UUID id;
  private final String name;

  public TagResponse(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static TagResponse ofTag(Tag tag) {
    return new TagResponse(tag.getId(), tag.getName());
  }
}
