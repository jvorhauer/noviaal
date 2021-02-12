package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.model.Note;
import nl.noviaal.model.User;
import nl.noviaal.model.command.CreateNote;
import nl.noviaal.model.response.NoteResponse;
import nl.noviaal.repository.NoteRepository;
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
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/notes")
@Slf4j
public class NoteController extends AbstractController {

  private final UserService userService;
  private final NoteService noteService;
  private final Validator validator;


  public NoteController(UserService userService, NoteService noteService, Validator validator) {
    this.userService = userService;
    this.noteService = noteService;
    this.validator   = validator;
  }

  @PostMapping("")
  public ResponseEntity<Object> addNote(@RequestBody CreateNote createNote, Authentication authentication) {
    if (validator.validate(createNote).size() > 0) {
      log.error("addNote: invalid: {}", createNote);
      throw new InvalidCommand("CreateNote");
    }
    var email = getUserEmail(authentication);
    User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    Note note = new Note(createNote.getTitle(), createNote.getBody());
    userService.addNote(user, note);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  @GetMapping("/")
  public List<NoteResponse> findAll(Principal principal) {
    return noteService.findAll()
             .stream()
             .map(NoteResponse::fromNote)
             .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<NoteResponse> find(@PathVariable("id") UUID id) {
    return ResponseEntity.ok().body(NoteResponse.fromNote(noteService.find(id)));
  }
}
