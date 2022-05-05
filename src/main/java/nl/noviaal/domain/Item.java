package nl.noviaal.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item {

  @Id
  @Column(columnDefinition = "uuid")
  UUID id;

  @Version
  Integer version;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  Instant created;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  Instant updated;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  User author;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("created DESC")
  private List<Comment> comments = new ArrayList<>();

  @ManyToMany
  @JoinTable(
    name = "item_tag",
    joinColumns = @JoinColumn(name = "item_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
  )
  private Set<Tag> tags = new HashSet<>();

  public void setId(UUID id) { this.id = id; }
  public UUID getId() { return id; }

  public Integer getVersion() { return version; }
  public void setVersion(Integer version) { this.version = version; }

  public User getAuthor() { return author; }
  public void setAuthor(User author) { this.author = author; }
  public Item claim(User user) {
    this.author = user;
    return this;
  }

  public ZonedDateTime getCreated() { return created.atZone(ZoneId.of("CET")); }
  public void setCreated(ZonedDateTime created) { this.created = created.toInstant(); }

  public void setComments(List<Comment> comments) { this.comments = comments; }
  public void addComment(Comment comment) { this.comments.add(comment); }
  public List<Comment> getComments() { return comments; }

  public void setTags(Set<Tag> tags) { this.tags = tags; }
  public void addTag(Tag tag) { this.tags.add(tag); }
  public Set<Tag> getTags() { return tags; }

  public ZonedDateTime getUpdated() { return updated != null ? updated.atZone(ZoneId.of("CET")) : getCreated(); }
  public void setUpdated(Instant updated) { this.updated = updated; }
  public void setUpdatedToNow() {
    setUpdated(ZonedDateTime.now(ZoneId.of("UTC")).toInstant());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!(o instanceof Item)) { return false; }
    return Objects.equals(getId(), ((Item) o).getId());
  }

  @Override
  public int hashCode() { return Objects.hashCode(getClass()); }

  void prePersist() {
    id = UUID.randomUUID();
    if (created == null) {
      created = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
    if (updated == null) {
      updated = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
  }
}
