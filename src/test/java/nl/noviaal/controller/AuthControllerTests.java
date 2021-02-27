package nl.noviaal.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.noviaal.NoviaalApplication;
import nl.noviaal.domain.User;
import nl.noviaal.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = NoviaalApplication.class
)
@AutoConfigureMockMvc
public class AuthControllerTests {

  @MockBean
  private AuthService authService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnCreatedUser() throws Exception {
    User createdUser = new User("Frodo", "frodo@hobbiton.shire", "password");
    createdUser.prePersist();
    when(authService.register(any(User.class))).thenReturn(createdUser);

    mockMvc.perform(
      post("/api/auth/register")
        .contentType("application/json")
        .content("{\"name\":\"Frodo\",\"email\":\"frodo@hobbiton.shire\",\"password\":\"password\"}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Frodo"));
  }

}
