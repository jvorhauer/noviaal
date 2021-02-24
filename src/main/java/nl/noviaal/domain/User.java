package nl.noviaal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

@Entity
@Table(name = "users")      // sorry Nick, user is een reserved word in PostgreSQL.
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
  private Set<Note> notes = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Media> media = new HashSet<>();

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

  public void setNotes(Set<Note> notes) { this.notes = notes; }
  public Set<Note> getNotes() { return notes; }
  public void addNote(Note note) {
    this.notes.add(note);
    note.setAuthor(this);
  }
  public void deleteNote(Note note) {
    this.notes.remove(note);
    note.setAuthor(null);
  }

  public Set<Media> getMedia() {
    return media;
  }
  public void addMedia(Media media) {
    this.media.add(media);
    media.setAuthor(this);
  }
  public void removeMedia(Media media) {
    this.media.remove(media);
    media.setAuthor(null);
  }

  public Set<Follow> getFollowers() { return this.followers; }
  public void addFollower(Follow follower) {
    this.followers.add(follower);
  }

  public Set<Follow> getFollowed() { return this.followed; }
}
