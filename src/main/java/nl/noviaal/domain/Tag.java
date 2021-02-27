package nl.noviaal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tag", indexes = @Index(name = "idx_name", unique = true, columnList = "name"))
public class Tag {
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToMany
  @JoinTable(
    name = "item_tag",
    joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "item_id")
  )
  private Set<Item> items = new HashSet<>();

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
  }

  public Tag() {}
  public Tag(String name) {
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<Item> getItems() { return items; }
  public void addItem(Item item) {
    items.add(item);
    item.addTag(this);
  }
}
