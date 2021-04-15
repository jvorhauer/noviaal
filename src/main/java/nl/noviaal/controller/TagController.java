package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Tag;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.TagNotFoundException;
import nl.noviaal.model.command.CreateTag;
import nl.noviaal.model.response.ItemResponse;
import nl.noviaal.model.response.TagResponse;
import nl.noviaal.service.TagService;
import nl.noviaal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/tags")
@Slf4j
public class TagController extends AbstractController {

  private final TagService tagService;

  public TagController(TagService tagService, UserService userService) {
    super(userService);
    this.tagService = tagService;
  }

  @PostMapping(value = {"", "/"})
  public TagResponse save(@RequestBody CreateTag createTag, Authentication authentication) {
    assertIsAdmin(authentication);
    log.info("save: {}", createTag);
    if (isInvalid(createTag)) {
      log.error("save: createTag is invalid: {}", createTag);
      throw new InvalidCommand("Create Tag is invalid");
    }
    if (tagService.find(createTag.getName()).isPresent()) {
      log.error("save: tag with name {} already exists", createTag.getName());
      throw new InvalidCommand("Create Tag for existing tag " + createTag.getName());
    }
    return TagResponse.ofTag(tagService.save(new Tag(createTag.getName())));
  }

  @GetMapping(value = {"", "/"})
  public List<TagResponse> list() {
    return tagService.findAll().stream().map(TagResponse::ofTag).collect(Collectors.toList());
  }

  @GetMapping("/{name}")
  public TagResponse getByName(@PathVariable("name") String name) {
    return tagService.find(name)
             .map(TagResponse::ofTag)
             .orElseThrow(() -> new TagNotFoundException("Tag " + name + " not found"));
  }

  @GetMapping("/{name}/items")
  public Set<ItemResponse> getItems(@PathVariable("name") String name) {
    return tagService.find(name)
             .map(Tag::getItems)
             .map(setOfItems -> setOfItems.stream()
                                  .map(ItemResponse::ofItem)
                                  .collect(Collectors.toSet()))
             .orElse(Set.of());
  }
}
