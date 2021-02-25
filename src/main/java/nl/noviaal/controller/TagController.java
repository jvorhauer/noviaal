package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Tag;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.TagNotFoundException;
import nl.noviaal.model.command.CreateTag;
import nl.noviaal.model.response.TagResponse;
import nl.noviaal.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Validator;

@Controller
@RequestMapping(path = "/api/tags")
@Slf4j
public class TagController {

  private final TagService tagService;
  private final Validator validator;

  public TagController(TagService tagService, Validator validator) {
    this.tagService = tagService;
    this.validator = validator;
  }

  @PostMapping(path = {"", "/"})
  public TagResponse save(@RequestBody CreateTag createTag) {
    if (!validator.validate(createTag).isEmpty()) {
      log.error("save: createTag is invalid: {}", createTag);
      throw new InvalidCommand("Create Tag is invalid");
    }
    return TagResponse.ofTag(tagService.save(new Tag(createTag.getName())));
  }

  @GetMapping(path = "/{name}")
  public TagResponse getByName(@PathVariable("name") String name) {
    return tagService.find(name).map(TagResponse::ofTag).orElseThrow(() -> new TagNotFoundException("Tag " + name + " not found"));
  }
}
