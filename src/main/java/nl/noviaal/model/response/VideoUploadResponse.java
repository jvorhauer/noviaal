package nl.noviaal.model.response;

import java.util.UUID;

public class VideoUploadResponse {
  private final UUID   id;
  private final String name;
  private final String contentType;
  private final Long   size;

  public VideoUploadResponse(UUID id, String name, String contentType, Long size) {
    this.id = id;
    this.name = name;
    this.contentType = contentType;
    this.size = size;
  }

  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getContentType() { return contentType; }
  public Long getSize() { return size; }
}
