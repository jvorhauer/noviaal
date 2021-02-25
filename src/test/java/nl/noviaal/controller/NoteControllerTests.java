package nl.noviaal.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.User;
import nl.noviaal.repository.NoteRepository;
import nl.noviaal.service.UserService;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc
public class NoteControllerTests {

  private final Setup setup;
  private final MockMvc mockMvc;
  private final UserService userService;
  private final NoteRepository noteRepository;

  @BeforeEach
  void before() {
    setup.initDataStore();
  }

  @Test
  @WithUserDetails("tester@test.com")
  void whenAddingNewNote_thenTheNumberOfNotes_shouldEqualOneMore() throws Exception {
    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(0));

    mockMvc.perform(post("/api/notes")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"title\":\"test title\",\"body\":\"test body\"}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.title").value("test title"));

    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(1));
  }

  @Test
  @WithUserDetails("an@other.com")
  @Transactional
  void whenAddingCommentsToExistingNote_thenTheNumberOfComments_shouldIncreaseByOne() throws Exception {
    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(0));

    mockMvc.perform(get("/api/users/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(0));

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

    mockMvc.perform(get("/api/users/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(1));
  }
}
