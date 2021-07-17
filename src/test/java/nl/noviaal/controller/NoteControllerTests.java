package nl.noviaal.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc
@ActiveProfiles("testerdetest")   // really weird bug in Spring (or Maven)! Without this it seems this class inherits "mocked" ??!!??!
class NoteControllerTests {

  private final MockMvc mockMvc;

  @Test
  @WithUserDetails("tester@test.com")
  void whenAddingNewNote_thenTheNumberOfNotes_shouldEqualOneMore() throws Exception {
    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(0));

    mockMvc.perform(post("/api/notes")
                      .with(user("tester@test.com").password("password"))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"title\":\"test title\",\"body\":\"test body\"}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.title").value("test title"))
      .andExpect(jsonPath("$.body").value("test body"));

    mockMvc.perform(get("/api/notes").with(user("tester@test.com").password("password")))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(1))
      ;
  }

  @Test
  @WithUserDetails("an@other.com")
  void whenAddingCommentsToExistingNote_thenTheNumberOfComments_shouldIncreaseByOne() throws Exception {
    mockMvc.perform(get("/api/notes"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(0));

    mockMvc.perform(get("/api/users/items"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(0));

    MvcResult res = mockMvc.perform(post("/api/notes")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"title\":\"test title\",\"body\":\"test body\"}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.title").value("test title"))
      .andReturn();

    String id = JsonPath.read(res.getResponse().getContentAsString(), "$.id");
    System.out.println("id: " + id);
    mockMvc.perform(post("/api/notes/" + id + "/comments")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"comment\":\"geen commentaar\",\"stars\":3}"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.comment").value("geen commentaar"));

    mockMvc.perform(get("/api/users/items"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(1));

    mockMvc.perform(get("/api/notes/" + id + "/comments"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(1));
  }
}
