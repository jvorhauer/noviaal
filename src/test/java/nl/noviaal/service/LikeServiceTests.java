package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.Like;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LikeServiceTests {

  private final LikeService service;
  private final UserService users;

  @Test
  void addNewLike() {
    var ouser = users.findByEmail("tester@test.com");
    assertThat(ouser).isPresent();
    var user = ouser.get();
    var noteId = UUID.randomUUID();
    var saved = service.save(new Like(user.getId(), noteId));
    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getUserId()).isEqualTo(user.getId());
    assertThat(saved.getNoteId()).isEqualTo(noteId);
    assertThat(saved.getLiked()).isBefore(ZonedDateTime.now(ZoneId.of("UTC")).toInstant());
  }
}
