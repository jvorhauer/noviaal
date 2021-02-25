package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;

import nl.noviaal.domain.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TagServiceTest {

  @Autowired
  private TagService tagService;

  @Test
  void whenSaveTag_thenExpectSavedTagHasId_shouldSucceed() {
    var saved = tagService.save(new Tag("test"));
    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isNotNull();

    var found = tagService.find("test");
    assertThat(found).isPresent();
    assertThat(found.get().getId()).isEqualTo(saved.getId());
  }
}
