package nl.noviaal.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import nl.noviaal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc
public class AllControllerTests {

  private final UserRepository userRepo;
  private final MockMvc mockMvc;

  @Test
  @WithUserDetails("tester@test.com")
  void givenLoggedInUser_getAllUsers_shouldSucceed() throws Exception {
    var count = userRepo.count();
    mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElements").value(count));
  }

  @Test
  @WithUserDetails("tester@test.com")
  void givenLoggedInUser_getThisUserById_shouldSucceed() throws Exception {
    var ouser = userRepo.findByEmail("tester@test.com");
    assertThat(ouser).isPresent();
    var user = ouser.get();
    mockMvc.perform(get("/api/users/" + user.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Tester"));
  }

  @Test
  @WithUserDetails("tester@test.com")
  void givenLoggedInUser_getOtherUserById_shouldSucceed() throws Exception {
    var ouser = userRepo.findByEmail("an@other.com");
    assertThat(ouser).isPresent();
    var user = ouser.get();
    mockMvc.perform(get("/api/users/" + user.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Another"));
  }

  @Test
  @WithUserDetails("tester@test.com")
  void givenLoggedInNormalUser_promoteOtherUser_shouldFail() throws Exception {
    var ouser = userRepo.findByEmail("an@other.com");
    assertThat(ouser).isPresent();
    var user = ouser.get();
    mockMvc.perform(put("/api/users/" + user.getId() + "/promote")).andExpect(status().is4xxClientError());
  }

  @Test
  @WithUserDetails("admin@tester.com")
  void givenLoggedInAdmin_promoteOtherUser_shouldSucceed() throws Exception {
    var ouser = userRepo.findByEmail("an@other.com");
    assertThat(ouser).isPresent();
    var user = ouser.get();
    mockMvc.perform(put("/api/users/" + user.getId() + "/promote")).andExpect(status().is2xxSuccessful());
  }


  /* ---- NoteController tests ---- */

  @Test
  @WithUserDetails("tester@test.com")
  void whenAddingNewNote_thenTheNumberOfNotes_shouldEqualOneMore() throws Exception {
    var count = getNotesSize("tester@test.com");
    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElements").value(count));

    mockMvc.perform(post("/api/notes")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"test title\",\"body\":\"test body\"}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.title").value("test title"))
      .andExpect(jsonPath("$.body").value("test body"));

    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElements").value(count + 1));
  }

  @Transactional(readOnly = true)
  public int getNotesSize(String email) {
    var user = userRepo.findByEmail(email);
    return user.map(u -> u.getItems().size()).orElse(0);
  }

  @Test
  @WithUserDetails("an@other.com")
  void whenAddingCommentsToExistingNote_thenTheNumberOfComments_shouldIncreaseByOne() throws Exception {
    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElements").value(0));

    mockMvc.perform(get("/api/users/items"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElements").value(0));

    MvcResult res = mockMvc.perform(post("/api/notes")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"title\":\"test title\",\"body\":\"test body\"}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.title").value("test title"))
      .andReturn();

    String id = JsonPath.read(res.getResponse().getContentAsString(), "$.id");
    mockMvc.perform(post("/api/notes/" + id + "/comments")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"comment\":\"geen commentaar\",\"stars\":3}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.comment").value("geen commentaar"));

    mockMvc.perform(get("/api/users/items"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElements").value(1));

    mockMvc.perform(get("/api/notes/" + id + "/comments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(1));
  }


  /* ---- MediaController tests ---- */

  @Test
  @WithUserDetails("tester@test.com")
  void whenUploadingFile_thenStoringThatFile_shouldReturnMediaUploadedResponse() throws Exception {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "image.png", MediaType.IMAGE_PNG_VALUE, inputStream);
      mockMvc.perform(multipart("/api/media").file(file))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("image.png"));
    }
  }

  @Test
  @WithUserDetails("tester@test.com")
  void whenUploadingNull_thenStoringThatNullFile_shouldThrowException() throws Exception {
    mockMvc.perform(post("/api/media").contentType(MediaType.MULTIPART_FORM_DATA))
      .andExpect(status().is4xxClientError());
  }

  @Test
  @WithUserDetails("tester@test.com")
  void whenUploadingFileWithEmptyName_thenSendingThatFile_shouldThrowException() throws Exception {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "", MediaType.IMAGE_PNG_VALUE, inputStream);
      mockMvc.perform(multipart("/api/media").file(file))
        .andExpect(status().is4xxClientError());
    }
  }

  @Test
  @WithUserDetails("tester@test.com")
  void whenUploadingFileWithInvalidName_thenSendingThatFile_shouldThrowException() throws Exception {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image.png")) {
      MockMultipartFile file = new MockMultipartFile("file", "../image.png", MediaType.IMAGE_PNG_VALUE, inputStream);
      mockMvc.perform(multipart("/api/media").file(file))
        .andExpect(status().is4xxClientError());
    }
  }
}
