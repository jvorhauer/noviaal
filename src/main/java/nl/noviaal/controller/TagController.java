package nl.noviaal.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.noviaal.domain.Tag;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.TagNotFoundException;
import nl.noviaal.model.command.CreateTag;
import nl.noviaal.model.response.ItemResponse;
import nl.noviaal.model.response.TagResponse;
import nl.noviaal.service.TagService;
import nl.noviaal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/tags")
public class TagController extends AbstractController {

  private static final Logger logger = LoggerFactory.getLogger("TagController");
  private final TagService tagService;

  public TagController(TagService tagService, UserService userService) {
    super(userService);
    this.tagService = tagService;
  }

  @PostMapping(value = {"", "/"})
  public TagResponse save(@RequestBody CreateTag createTag, Authentication authentication) {
    assertIsAdmin(authentication);
    logger.info("save: {}", createTag);
    if (!isValid(createTag)) {
      logger.error("save: createTag is invalid: {}", createTag);
      throw new InvalidCommand("Create Tag is invalid");
    }
    if (tagService.find(createTag.name()).isPresent()) {
      logger.error("save: tag with name {} already exists", createTag.name());
      throw new InvalidCommand("Create Tag for existing tag " + createTag.name());
    }
    return TagResponse.ofTag(tagService.save(new Tag(createTag.name())));
  }

  @GetMapping(value = {"", "/"})
  public List<TagResponse> list() {
    return tagService.findAll().stream().map(TagResponse::ofTag).toList();
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
                                  .map(ItemResponse::from)
                                  .collect(Collectors.toSet()))
             .orElse(Set.of());
  }
}
