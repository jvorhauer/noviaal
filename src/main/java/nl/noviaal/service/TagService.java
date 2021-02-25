package nl.noviaal.service;

import nl.noviaal.domain.Tag;
import nl.noviaal.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagService {

  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Transactional
  public Tag save(Tag tag) {
     return find(tag.getName()).orElse(tagRepository.save(tag));
  }

  @Transactional(readOnly = true)
  public Optional<Tag> find(String name) {
    return tagRepository.findByName(name);
  }
}
