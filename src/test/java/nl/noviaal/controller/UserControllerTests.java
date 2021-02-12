package nl.noviaal.controller;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import nl.noviaal.service.UserTestSupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTests {
  private final UserController userController;
  private final UserTestSupportService support;


  @BeforeEach
  void beforeEach() {
    support.truncate();
  }

  @Test
  void testSomething() {
    assertThat(userController.findAll(null)).isNotNull();
  }
}
