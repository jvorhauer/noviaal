package nl.noviaal.model.response;

import nl.noviaal.domain.Media;

import java.util.UUID;

public class MediaResponse {
  private UUID id;
  private String name;
  private String contentType;

  public MediaResponse(UUID id, String name, String contentType) {
    this.id = id;
    this.name = name;
    this.contentType = contentType;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getContentType() {
    return contentType;
  }

  public static MediaResponse ofMedia(Media media) {
    return new MediaResponse(media.getId(), media.getName(), media.getContentType());
  }
}
