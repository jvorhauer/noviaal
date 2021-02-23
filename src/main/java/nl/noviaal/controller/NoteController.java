package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.model.command.CreateNote;
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
import java.util.List;
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
  public ResponseEntity<Object> addNote(@RequestBody CreateNote createNote, Authentication authentication) {
    if (validator.validate(createNote).size() > 0) {
      log.error("addNote: invalid: {}", createNote);
      throw new InvalidCommand("CreateNote");
    }
    User user = findCurrentUser(authentication);
    Note note = new Note(createNote.getTitle(), createNote.getBody());
    userService.addNote(user, note);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  @GetMapping(value = {"/", ""})
  public List<NoteResponse> findAllForCurrentUser(Authentication authentication) {
    return convertNotesToResponse(findCurrentUser(authentication).getNotes());
  }

  private List<NoteResponse> convertNotesToResponse(Set<Note> notes) {
    return notes != null ? notes.stream().map(NoteResponse::fromNote).collect(Collectors.toList()) : List.of();
  }

  @GetMapping("/{id}")
  public ResponseEntity<NoteResponse> find(@PathVariable("id") UUID noteId) {
    return ResponseEntity.ok().body(NoteResponse.fromNote(noteService.find(noteId)));
  }

  @GetMapping("/user/{id}")
  public List<NoteResponse> findAllForSpecifiedUser(@PathVariable("id") UUID id) {
    return convertNotesToResponse(findUserById(id).getNotes());
  }
}
