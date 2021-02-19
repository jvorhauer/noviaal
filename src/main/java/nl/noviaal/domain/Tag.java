package nl.noviaal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "tag")
public class Tag {
  @Id
  private UUID id;

  @Column(nullable = false, length = 255, unique = true)
  private String name;

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
  }
}
