package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.BaseItem;
import nl.noviaal.domain.Comment;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.model.command.CreateComment;
import nl.noviaal.model.command.CreateNote;
import nl.noviaal.model.response.CommentCreatedResponse;
import nl.noviaal.model.response.NoteResponse;
import nl.noviaal.service.NoteService;
import nl.noviaal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;
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
  private final Validator validator;


  public NoteController(UserService userService, NoteService noteService, Validator validator) {
    super(userService);
    this.noteService = noteService;
    this.validator   = validator;
  }

  @PostMapping(value = {"", "/"})
  public ResponseEntity<NoteResponse> addNote(@RequestBody CreateNote createNote, Authentication authentication) {
    if (validator.validate(createNote).size() > 0) {
      log.error("addNote: invalid: {}", createNote);
      throw new InvalidCommand("CreateNote");
    }
    User user = findCurrentUser(authentication);
    Note note = new Note(createNote.getTitle(), createNote.getBody());
    userService.addNote(user, note);
    Optional<Note> last = user.getNotes().stream().min(Comparator.comparing(BaseItem::getCreated));
    return last.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(NoteResponse.fromNote(value)))
             .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }


  @GetMapping(value = {"/", ""})
  public List<NoteResponse> findAllForCurrentUser(Authentication authentication) {
    return convertNotesToResponse(findCurrentUser(authentication).getNotes());
  }

  private List<NoteResponse> convertNotesToResponse(Set<Note> notes) {
    return notes != null ? notes.stream().map(NoteResponse::fromNote).collect(Collectors.toList()) : List.of();
  }

  @GetMapping("/{id}")
  public NoteResponse find(@PathVariable("id") UUID noteId) {
    return NoteResponse.fromNote(noteService.find(noteId));
  }

  @GetMapping("/user/{id}")
  public List<NoteResponse> findAllForSpecifiedUser(@PathVariable("id") UUID id) {
    return convertNotesToResponse(findUserById(id).getNotes());
  }

  @PostMapping("/{id}/comments")
  public ResponseEntity<CommentCreatedResponse> addCommentToNote(
    @RequestBody CreateComment createComment,
    @PathVariable("id") UUID id, Authentication authentication
  ) {
    if (validator.validate(createComment).size() > 0) {
      log.error("addCommentToNote: invalid: {}", createComment);
      throw new InvalidCommand("CreateComment");
    }

    User user = findCurrentUser(authentication);
    Note note = noteService.find(id);
    Comment comment = new Comment(createComment.getComment(), createComment.getStars() == null ? 0 : createComment.getStars());
    return noteService.addCommentToNote(note, user, comment)
             .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(CommentCreatedResponse.ofComment(c)))
             .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }
}
