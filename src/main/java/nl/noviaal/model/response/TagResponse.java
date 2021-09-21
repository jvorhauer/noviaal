package nl.noviaal.model.response;

import nl.noviaal.domain.Tag;

import java.util.UUID;

public record TagResponse(UUID id, String name) {
  public static TagResponse ofTag(Tag tag) {
    return new TagResponse(tag.getId(), tag.getName());
  }
}
