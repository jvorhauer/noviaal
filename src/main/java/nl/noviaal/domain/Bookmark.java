package nl.noviaal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bookmark")
public class Bookmark extends BaseItem {

  @Column(nullable = false)
  private String label;

  @Column(length = 1024, nullable = false)
  private String url;

  @Column(length = 1024)
  private String remark;

  public Bookmark() {}
  public Bookmark(String label, String url, String remark) {
    this.label = label;
    this.url = url;
    this.remark = remark;
  }

  public String getLabel() { return label; }
  public void setLabel(String label) { this.label = label; }

  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }

  public String getRemark() { return remark; }
  public void setRemark(String remark) { this.remark = remark; }
}
