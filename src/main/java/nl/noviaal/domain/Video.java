package nl.noviaal.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;

@Entity
@Table(name = "video")
public class Video extends BaseItem {

  private String name;

  private String contentType;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] video;

  public Video() {}
  public Video(String name, String ct, byte[] video) {
    this.name = name;
    this.contentType = ct;
    this.video = video;
  }


  public void setName(String name) { this.name = name; }
  public String getName() { return name; }

  public void setContentType(String ct) { contentType = ct; }
  public String getContentType() { return contentType; }

  public void setVideo(byte[] video) { this.video = video; }
  public byte[] getVideo() { return video; }
}
