package nl.noviaal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_email", unique = true, columnList = "email"))
@ToString
public class User {

  @Id
  private UUID id;

  @NotNull
  @Column(nullable = false, length = 72)
  private String name;

  @NotNull
  @Column(nullable = false, length = 1024, unique = true)
  private String email;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
  private Instant joined;

  @Column(length = 1024)
  private String password;

  @NotBlank
  @Column(length = 100)
  private String roles;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private Set<Item> items = new HashSet<>();

  @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Follow.class)
  @JsonManagedReference
  private final Set<Follow> followers = new HashSet<>();

  @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Follow.class)
  @JsonManagedReference
  private final Set<Follow> followed = new HashSet<>();


  public User() {}
  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.roles = "USER";
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

  @PrePersist
  public void prePersist() {
    this.id = UUID.randomUUID();
    if (this.joined == null) {
      this.joined = ZonedDateTime.now(ZoneId.of("UTC")).toInstant();
    }
  }

  public void setId(UUID id) {
    if (this.id == null) {
      this.id = id;
    }
  }
  public UUID getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public ZonedDateTime getJoined() { return joined != null ? joined.atZone(ZoneId.of("CET")) : ZonedDateTime.now(); }
  public void setJoined(ZonedDateTime joined) { this.joined = joined.toInstant(); }

  public void setPassword(String password) { this.password = password; }
  public String getPassword() { return password; }

  public void setRoles(String roles) { this.roles = roles; }
  public String getRoles() { return roles; }

  public void setItems(Set<Item> items) { this.items = items; }
  public Set<Item> getItems() { return items; }
  public Item addItem(Item item) {
    items.add(item);
    item.setAuthor(this);
    return item;
  }
  public void deleteItem(Item item) {
    items.remove(item);
    item.setAuthor(null);
  }

  public Set<Note> getNotes() {
    return items.stream()
             .filter(item -> item instanceof Note)
             .map(item -> (Note) item)
             .collect(Collectors.toSet());
  }
  public void addNote(Note note) {
    addItem(note);
  }
  public void deleteNote(Note note) {
    deleteItem(note);
  }

  public Set<Media> getMedia() {
    return items.stream()
             .filter(item -> item instanceof Media)
             .map(item -> (Media) item)
             .collect(Collectors.toSet());
  }
  public void addMedia(Media media) {
    addItem(media);
  }

  public Set<Follow> getFollowers() { return this.followed; }
  public void addFollower(Follow follower) {
    this.followers.add(follower);
  }
  public Set<Follow> getFollowed() { return this.followers; }


  public static class UserBuilder {
    private @NotNull String name;
    private @NotNull String email;
    private String password;
    private @NotBlank String roles = "USER";

    UserBuilder() {}

    public UserBuilder name(@NotNull String name) {
      this.name = name;
      return this;
    }

    public UserBuilder email(@NotNull String email) {
      this.email = email;
      return this;
    }

    public UserBuilder password(String password) {
      this.password = password;
      return this;
    }

    public UserBuilder roles(@NotBlank String roles) {
      this.roles = roles;
      return this;
    }

    public User build() {
      User newUser = new User(name, email, password);
      newUser.setRoles(roles);
      return newUser;
    }
  }
}
