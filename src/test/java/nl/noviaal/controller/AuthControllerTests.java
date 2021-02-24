package nl.noviaal.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.noviaal.NoviaalApplication;
import nl.noviaal.domain.User;
import nl.noviaal.service.AuthService;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
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

  @Autowired
  private Setup setup;

  @BeforeEach
  void before() {
    setup.initDataStore();
  }


  @Test
  void shouldReturnCreatedUser() throws Exception {
    User createdUser = new User("Test", "frodo@hobbiton.shire", "password");
    createdUser.prePersist();
    when(authService.register(any(User.class))).thenReturn(createdUser);

    mockMvc.perform(
      post("/api/auth/register").contentType("application/json")
                      .content("{\"name\":\"Frodo\",\"email\":\"frodo@hobbiton.shire\",\"password\":\"password\"}")
    ).andExpect(status().isCreated())
      .andExpect(header().exists("Location"));
  }

}
