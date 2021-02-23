package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.noviaal.domain.Media;
import nl.noviaal.domain.User;
import nl.noviaal.exception.MediaInvalidException;
import nl.noviaal.support.UserTestSupportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class MediaServiceTests {

  @Autowired
  private AuthService authService;

  @Autowired
  private MediaService mediaService;

  @Autowired
  private UserTestSupportService support;


  @Test
  void wenFileUploaded_thenVerifyStatusAndResponse() throws IOException {
    try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "image.png", MediaType.IMAGE_PNG_VALUE, inputStream);
      User user = authService.register(new User("tester", "tester@test.com", "password"));

      Media media = mediaService.store(file, user);
      assertThat(media).isNotNull();
      assertThat(media.getName()).isEqualTo("image.png");
      assertThat(media.getContentType()).isEqualTo("image/png");
      assertThat(media.getContent()).isNotNull();
    }
  }

  @Test
  void whenNullFileUploaded_thenVerifyResponseIsMediaInvalid() {
    User user = authService.register(new User("tester", "tester@test.com", "password"));
    assertThrows(MediaInvalidException.class, () -> mediaService.store(null, user));
  }

  @Test
  void whenFileWithoutFileNameUploaded_thenVerifyResponseIsMediaInvalid() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png");
    MockMultipartFile file = new MockMultipartFile("file", null, MediaType.IMAGE_PNG_VALUE, inputStream);
    User user = authService.register(new User("tester", "tester@test.com", "password"));

    assertThrows(MediaInvalidException.class, () -> mediaService.store(file, user));
  }

  @Test
  void whenFileWithBadFileNameUploaded_thenVerifyResponseIsMediaInvalid() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png");
    MockMultipartFile file = new MockMultipartFile("file", "../error.png", MediaType.IMAGE_PNG_VALUE, inputStream);
    User user = authService.register(new User("tester", "tester@test.com", "password"));

    assertThrows(MediaInvalidException.class, () -> mediaService.store(file, user));
  }


  @BeforeEach
  public void before() {
    support.truncate();
  }

  @AfterEach
  public void after() {
    support.truncate();
  }
}
