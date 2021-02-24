package nl.noviaal.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import lombok.RequiredArgsConstructor;
import nl.noviaal.NoviaalApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.UUID;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = NoviaalApplication.class
)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthControllerIntegrationTests {

  private final MockMvc mockMvc;

  @Test
  void createUserWithCorrectCommandJson() throws Exception {
    mockMvc.perform(post("/api/auth/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"name\":\"Frodo\",\"email\":\"frodo@hobbiton.shire\",\"password\":\"password\"}"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    mockMvc.perform(post("/api/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"username\":\"frodo@hobbiton.shire\",\"password\":\"password\"}"))
                      .andDo(MockMvcResultHandlers.print())
                      .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
    ;
  }

  @Disabled
  void tryToCreateUserTwiceWithSameCommandJsonShouldFailTheSecondAttempt() throws Exception {
    mockMvc.perform(post(URI.create("/users"))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"name\":\"Frodo\",\"email\":\"frodo@hobbiton.shire\",\"password\":\"password\"}"))
      .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    mockMvc.perform(post(URI.create("/users"))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"name\":\"Bilbo\",\"email\":\"frodo@hobbiton.shire\",\"password\":\"password\"}"))
      .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

  @Disabled
  void postNewUserAndRetrieveAllUsersShouldReturnListWithOneUser() throws Exception {
    mockMvc.perform(post(URI.create("/users"))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"name\":\"Frodo\",\"email\":\"frodo@hobbiton.shire\",\"password\":\"password\"}"))
      .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    mockMvc.perform(get(URI.create("/users")))
      .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
    ;
  }

  @Test
  void findNonExistentUserShouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/users/" + UUID.randomUUID())
                      .with(user("frodo@hobbiton.shire")))
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
