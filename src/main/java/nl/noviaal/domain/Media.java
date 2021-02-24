package nl.noviaal.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "media")
public class Media extends BaseItem {

  private String name;

  private String contentType;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] content;

  public Media() {}
  public Media(String name, String ct, byte[] content) {
    this.name = name;
    this.contentType = ct;
    this.content = content;
  }


  public void setName(String name) { this.name = name; }
  public String getName() { return name; }

  public void setContentType(String ct) { contentType = ct; }
  public String getContentType() { return contentType; }

  public void setContent(byte[] video) { this.content = video; }
  public byte[] getContent() { return content; }
}