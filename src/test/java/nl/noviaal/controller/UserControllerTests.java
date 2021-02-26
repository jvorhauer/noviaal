package nl.noviaal.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.RequiredArgsConstructor;
import nl.noviaal.repository.UserRepository;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureMockMvc
public class UserControllerTests {

  private final UserRepository userRepo;
  private final MockMvc mockMvc;
  private final Setup setup;

  @BeforeEach
  void before() {
    setup.initDataStore();
  }

  @Test
  @WithUserDetails("tester@test.com")
  void givenLoggedInUser_getAllUsers_shouldSucceed() throws Exception {
    mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(3));
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
}
