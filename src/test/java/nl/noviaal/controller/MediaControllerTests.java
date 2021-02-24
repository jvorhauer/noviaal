package nl.noviaal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.RequiredArgsConstructor;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc
public class MediaControllerTests {

  private final MockMvc mockMvc;
  private final Setup setup;

  @BeforeEach
  void before() {
    setup.initDataStore();
  }

  @Test
  @WithUserDetails("test@tester.com")
  void whenUploadingFile_thenStoringThatFile_shouldReturnMediaUploadedResponse() throws Exception {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "image.png", MediaType.IMAGE_PNG_VALUE, inputStream);
      mockMvc.perform(multipart("/api/media").file(file))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("image.png"));
    }
  }

  @Test
  @WithUserDetails("test@tester.com")
  void whenUploadingNull_thenStoringThatNullFile_shouldThrowException() throws Exception {
      mockMvc.perform(post("/api/media").contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithUserDetails("test@tester.com")
  void whenUploadingFileWithEmptyName_thenSendingThatFile_shouldThrowException() throws Exception {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "", MediaType.IMAGE_PNG_VALUE, inputStream);
      mockMvc.perform(multipart("/api/media").file(file))
        .andExpect(status().is4xxClientError());
    }
  }

  @Test
  @WithUserDetails("test@tester.com")
  void whenUploadingFileWithInvalidName_thenSendingThatFile_shouldThrowException() throws Exception {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "../image.png", MediaType.IMAGE_PNG_VALUE, inputStream);
      mockMvc.perform(multipart("/api/media").file(file))
        .andExpect(status().is4xxClientError());
    }
  }
}
