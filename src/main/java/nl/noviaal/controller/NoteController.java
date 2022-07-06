package nl.noviaal.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.Tag;
import nl.noviaal.domain.User;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.TagNotFoundException;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.model.command.CreateComment;
import nl.noviaal.model.command.CreateNote;
import nl.noviaal.model.command.UpdateNote;
import nl.noviaal.model.response.CommentResponse;
import nl.noviaal.model.response.ItemResponse;
import nl.noviaal.service.NoteService;
import nl.noviaal.service.TagService;
import nl.noviaal.service.UserService;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping(path = "/api/notes")
public class NoteController extends AbstractController {

  private static final Logger logger = LoggerFactory.getLogger("NoteController");

  private final NoteService noteService;
  private final TagService tagService;

  public NoteController(UserService userService, NoteService noteService, TagService tagService) {
    super(userService);
    this.noteService = noteService;
    this.tagService = tagService;
  }

  @PostMapping(value = { "", "/" })
  public ResponseEntity<ItemResponse> addNote(@RequestBody CreateNote createNote, Authentication authentication) {
    if (!isValid(createNote)) {
      logger.error("addNote: invalid: {}", createNote);
      throw new InvalidCommand("CreateNote");
    }
    User user = findCurrentUser(authentication);
    Note added = userService.addNote(user, Encode.forHtml(createNote.title()), Encode.forHtml(createNote.body()));
    return ResponseEntity.status(HttpStatus.CREATED).body(ItemResponse.from(added));
  }

  @PutMapping(value = { "/{id}" })
  public ResponseEntity<ItemResponse> updateNote(@PathVariable("id") UUID id, @RequestBody UpdateNote unote, Authentication auth) {
    if (!isValid(unote)) {
      logger.error("updateNote: invalid: {}", unote);
      throw new InvalidCommand("UpdateNote: invalid");
    }
    User user = findCurrentUser(auth);
    if (!unote.userId().equals(user.getId())) {
      logger.error("updateNote: note belongs to other user: {} <> {}", user.getId(), unote.userId());
      throw new InvalidCommand("UpdateNote: wrong user");
    }
    Note note = noteService.find(id);
    note.setTitle(Encode.forHtml(unote.title()));
    note.setBody(Encode.forHtml(unote.body()));
    Note saved = noteService.save(note);
    return ResponseEntity.ok(ItemResponse.from(saved));
  }

  @GetMapping(value = { "", "/" })
  public Page<ItemResponse> findAllForCurrentUser(Pageable pageable, Authentication authentication) {
    var user = findCurrentUser(authentication);
    return noteService.getNotesPageForUser(user, pageable).map(ItemResponse::from);
  }

  @GetMapping("/{id}")
  public ItemResponse find(@PathVariable("id") UUID noteId) {
    return ItemResponse.from(noteService.find(noteId));
  }

  @GetMapping("/user/{id}")
  public Page<ItemResponse> findAllForSpecifiedUser(
      @PathVariable("id") UUID id,
      Pageable pageable,
      Authentication authentication)
  {
    check(authentication);
    return userService.findById(id)
                      .map(user -> noteService.getNotesPageForUser(user, pageable)
                                              .map(ItemResponse::from))
                      .orElseThrow(() -> new UserNotFoundException(id));
  }

  @PostMapping("/{id}/comments")
  public ItemResponse addCommentToNote(
      @RequestBody CreateComment createComment,
      @PathVariable("id") UUID id,
      Authentication authentication)
  {
    if (!isValid(createComment)) {
      logger.error("addCommentToNote: invalid: {}", createComment);
      throw new InvalidCommand("CreateComment");
    }

    User user = findCurrentUser(authentication);
    Note note = noteService.find(id);
    Comment comment = new Comment(Encode.forHtml(createComment.comment()), createComment.stars() == null ? 0 : createComment.stars());
    return ItemResponse.from(noteService.addCommentToNote(note, user, comment));
  }

  @GetMapping("/{id}/comments")
  public List<CommentResponse> getComments(@PathVariable("id") UUID id, Authentication authentication) {
    check(authentication);
    var user = findCurrentUser(authentication);
    logger.info("getComments: user: {}", user.getId());
    return noteService.find(id).getComments().stream().map(CommentResponse::ofComment).toList();
  }

  @PostMapping("/{id}/like")
  public ItemResponse like(@PathVariable("id") UUID id, Authentication authentication) {
    check(authentication);
    var note = noteService.find(id);
    note.incrementLikes();
    var saved = noteService.save(note);
    return ItemResponse.from(saved);
  }

  @PostMapping("{id}/tag/{name}")
  public ItemResponse tag(@PathVariable("id") UUID id, @PathVariable("name") String name, Authentication authentication) {
    check(authentication);
    Optional<Tag> otag = tagService.find(name);
    if (otag.isEmpty()) {
      throw new TagNotFoundException(name);
    }
    Note note = noteService.find(id);
    note.addTag(otag.get());
    return ItemResponse.from(noteService.save(note));
  }
}
