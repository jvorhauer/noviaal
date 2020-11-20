package nl.novi.controller;

import lombok.extern.slf4j.Slf4j;
import nl.novi.action.NoteService;
import nl.novi.user.UserDetailsImpl;
import nl.novi.view.NoteForm;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class NoteController {

  private final NoteService noteService;
  private final UserDetailsService userDetailsService;

  public NoteController(NoteService noteService, UserDetailsService userDetailsService) {
    this.noteService = noteService;
    this.userDetailsService = userDetailsService;
  }

  @GetMapping("/note")
  public String get(final Model model) {
    log.info("get note");
    model.addAttribute("form", new NoteForm());
    return "note";
  }

  @PostMapping("/note")
  public String post(@Valid @ModelAttribute NoteForm form, BindingResult errors, Model model, Principal principal) {
    log.info("post: {} by {}", form, principal.getName());
    if (!errors.hasErrors()) {
      noteService.create(form, principal.getName());
    } else {
      model.addAttribute("error", "");
      log.error("post: {}", errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining()));
    }
    return "redirect:/notes";
  }

  @GetMapping("/notes")
  public String notes(final Model model) {
    log.info("notes");
    model.addAttribute("notes", noteService.all());
    return "notes";
  }
}
