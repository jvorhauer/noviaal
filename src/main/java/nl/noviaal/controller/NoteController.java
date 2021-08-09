package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Item;
import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.Tag;
import nl.noviaal.domain.User;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.NoteNotFoundException;
import nl.noviaal.exception.TagNotFoundException;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.model.command.CreateComment;
import nl.noviaal.model.command.CreateNote;
import nl.noviaal.model.command.UpdateNote;
import nl.noviaal.model.response.ItemResponse;
import nl.noviaal.model.response.CommentResponse;
import nl.noviaal.model.response.ItemsPage;
import nl.noviaal.service.NoteService;
import nl.noviaal.service.TagService;
import nl.noviaal.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/notes")
@Slf4j
public class NoteController extends AbstractController {

  private final NoteService noteService;
  private final TagService tagService;

  public NoteController(UserService userService, NoteService noteService, TagService tagService) {
    super(userService);
    this.noteService = noteService;
    this.tagService = tagService;
  }

  @PostMapping(value = {"", "/"})
  public ResponseEntity<ItemResponse> addNote(@RequestBody CreateNote createNote, Authentication authentication) {
    if (isInvalid(createNote)) {
      log.error("addNote: invalid: {}", createNote);
      throw new InvalidCommand("CreateNote");
    }
    User user = findCurrentUser(authentication);
    Note note = new Note(createNote.getTitle(), createNote.getBody());
    Note added = userService.addNote(user, note);
    return ResponseEntity.status(HttpStatus.CREATED).body(ItemResponse.from(added));
  }

  @PutMapping(value = {"", "/"})
  public ResponseEntity<ItemResponse> updateNote(@RequestBody UpdateNote unote, Authentication auth) {
    if (isInvalid(unote)) {
      log.error("updateNote: invalid: {}", unote);
      throw new InvalidCommand("UpdateNote: invalid");
    }
    User user = findCurrentUser(auth);
    if (!unote.getUserId().equals(user.getId())) {
      log.error("updateNote: note belongs to other user: {} <> {}", user.getId(), unote.getUserId());
      throw new InvalidCommand("UpdateNote: wrong user");
    }
    Note note = noteService.find(unote.getId());
    note.setTitle(unote.getTitle());
    note.setBody(unote.getBody());
    Note saved = noteService.save(note);
    return ResponseEntity.ok(ItemResponse.from(saved));
  }


  @GetMapping(value = {"", "/"})
  public ItemsPage findAllForCurrentUser(Pageable pageable, Authentication authentication) {
    var user = findCurrentUser(authentication);
    final ItemsPage itemsPage = ItemsPage.from(noteService.getNotesPageForUser(user, pageable));
    log.info("findAllForCurrentUser: itemsPage: {}", itemsPage);
    return itemsPage;
  }

  private List<ItemResponse> convertNotesToResponse(Set<Note> notes) {
    return notes != null ? notes.stream().map(ItemResponse::from).collect(Collectors.toList()) : List.of();
  }

  @GetMapping("/{id}")
  public ItemResponse find(@PathVariable("id") UUID noteId) {
    return ItemResponse.from(noteService.find(noteId));
  }

  @GetMapping("/user/{id}")
  public ItemsPage findAllForSpecifiedUser(@PathVariable("id") UUID id, Pageable pageable) {
    var user = userService.findById(id);
    if (user.isPresent()) {
      final ItemsPage itemsPage = ItemsPage.from(noteService.getNotesPageForUser(user.get(), pageable));
      log.info("findAllForSpecifiedUser: itemsPage: {}", itemsPage);
      return itemsPage;
    } else {
      throw new UserNotFoundException(id);
    }
  }

  @PostMapping("/{id}/comments")
  public ResponseEntity<CommentResponse> addCommentToNote(
    @RequestBody CreateComment createComment,
    @PathVariable("id") UUID id,
    Authentication authentication
  ) {
    if (isInvalid(createComment)) {
      log.error("addCommentToNote: invalid: {}", createComment);
      throw new InvalidCommand("CreateComment");
    }

    User user = findCurrentUser(authentication);
    Note note = noteService.find(id);
    Comment comment = new Comment(
            createComment.getComment(), createComment.getStars() == null ? 0 : createComment.getStars()
    );
    return noteService.addCommentToNote(note, user, comment)
             .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.ofComment(c)))
             .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @GetMapping("/{id}/comments")
  public List<CommentResponse> getComments(@PathVariable("id") UUID id) {
    return noteService.find(id)
             .getComments().stream()
             .map(CommentResponse::ofComment)
             .collect(Collectors.toList());
  }

  @PostMapping("{id}/tag/{name}")
  public ItemResponse tag(@PathVariable("id") UUID id, @PathVariable("name") String name, Authentication authentication) {
    Optional<Tag> otag = tagService.find(name);
    if (otag.isEmpty()) {
      throw new TagNotFoundException(name);
    }
    Note note = noteService.find(id);
    note.addTag(otag.get());
    return ItemResponse.from(noteService.save(note));
  }
}
