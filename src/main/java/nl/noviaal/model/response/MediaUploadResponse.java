package nl.noviaal.model.response;

import nl.noviaal.domain.Media;

import java.util.UUID;

public class MediaUploadResponse {
  private final UUID   id;
  private final String name;
  private final String contentType;
  private final Long   size;

  public MediaUploadResponse(UUID id, String name, String contentType, Long size) {
    this.id = id;
    this.name = name;
    this.contentType = contentType;
    this.size = size;
  }

  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getContentType() { return contentType; }
  public Long getSize() { return size; }

  public static MediaUploadResponse ofMedia(Media media, long size) {
    return new MediaUploadResponse(media.getId(), media.getName(), media.getContentType(), size);
  }
}
