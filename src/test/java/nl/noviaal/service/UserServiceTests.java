package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.RequiredArgsConstructor;
import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceTests {

  private final UserService userService;
  private final UserTestSupportService support;

  @Test
  void findAllWithEmptyDatabase() {
    assertThat(userService.findAll()).hasSize(0);
  }

  @Test
  void saveUserAndFindAllSuccessfully() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var saved = userService.save(user);
    assertThat(saved.getId()).isNotNull();
    assertThat(userService.findAll()).hasSize(1);
  }

  @Test
  void saveUserAndSaveOtherUserWithSameEmailAgainShouldFailWithInvalidCommand() {
    var user1 = new User("Frodo", "frodo@hobbiton.shire", "password");
    userService.save(user1);
    var user2 = new User("Bilbo", "frodo@hobbiton.shire", "password");
    assertThatThrownBy(() -> userService.save(user2))
        .isInstanceOf(EmailAddressInUseException.class)
        .hasMessage("Email address frodo@hobbiton.shire already in use");
  }

  @Test
  void saveAndThenDeleteTheUserShouldNotFindThatUser() {
    var user = userService.save(new User("Frodo", "frodo@hobbiton.shire", "password"));
    var id = user.getId();
    assertThat(id).isNotNull();
    userService.delete(user);
    assertThat(userService.findById(id)).isEmpty();
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
