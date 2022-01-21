package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.Like;
import nl.noviaal.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class LikeServiceTests {

  private final LikeService likeService;
  private final UserService userService;

  @Test
  void addNewLike() {
    var user = userService.save(new User("Liker", "liker@test.com", "password"));
    var noteId = UUID.randomUUID();
    var saved = likeService.save(new Like(user.getId(), noteId));
    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getUserId()).isEqualTo(user.getId());
    assertThat(saved.getNoteId()).isEqualTo(noteId);
    assertThat(saved.getLiked()).isBefore(ZonedDateTime.now(ZoneId.of("UTC")).toInstant());
  }
}
