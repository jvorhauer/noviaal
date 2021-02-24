package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;

import nl.noviaal.domain.Media;
import nl.noviaal.domain.User;
import nl.noviaal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MediaServiceTests {

  @Autowired
  private MediaService mediaService;

  @Autowired
  private UserRepository userRepository;


  @Test
  void wenFileUploaded_thenVerifyStatusAndResponse() throws IOException {
    try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "image.png", MediaType.IMAGE_PNG_VALUE, inputStream);
//      User user = authService.register(new User("tester", "tester@test.com", "password"));
      Optional<User> ouser = userRepository.findByEmail("tester@test.com");
      assertThat(ouser).isPresent();
      User user = ouser.get();

      Media media = mediaService.store(file, user);
      assertThat(media).isNotNull();
      assertThat(media.getName()).isEqualTo("image.png");
      assertThat(media.getContentType()).isEqualTo("image/png");
      assertThat(media.getContent()).isNotNull();
    }
  }
}
