package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import nl.noviaal.domain.Like;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LikeServiceTests {

  private final LikeService likeService;
  private final UserService userService;

  @Autowired
  public LikeServiceTests(LikeService likeService, UserService userService) {
    this.likeService = likeService;
    this.userService = userService;
  }

  @Test
  void addNewLike() {
    var ouser = userService.findByEmail("tester@test.com");
    assertThat(ouser).isPresent();
    var user = ouser.get();
    var noteId = UUID.randomUUID();
    var saved = likeService.save(new Like(user.getId(), noteId));
    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getUserId()).isEqualTo(user.getId());
    assertThat(saved.getNoteId()).isEqualTo(noteId);
    assertThat(saved.getLiked()).isBefore(ZonedDateTime.now(ZoneId.of("UTC")).toInstant());
  }
}
