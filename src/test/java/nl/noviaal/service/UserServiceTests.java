package nl.noviaal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.RequiredArgsConstructor;
import nl.noviaal.domain.User;
import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.support.Setup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class UserServiceTests {

  private final UserService userService;
  private final Setup setup;

  @BeforeEach
  void before() {
    setup.initDataStore();
  }

  @Test
  void whenDatabaseInitial_thenFindAll_shouldReturnUserList() {
    assertThat(userService.findAll().getTotalElements()).isEqualTo(3L);
    assertThat(userService.findAll().getContent()).hasSize(3);
  }

  @Test
  void whenSaveOneUser_thenFindAll_shouldFindOne() {
    var user = new User("Frodo", "frodo@hobbiton.shire", "password");
    var saved = userService.save(user);
    assertThat(saved.getId()).isNotNull();
    assertThat(userService.findAll().getTotalElements()).isEqualTo(4L);
    assertThat(userService.findAll().getContent()).hasSize(4);
  }

  @Test
  void whenSaveUserAndSaveOtherUserWithSameEmailAgain_thenSave_shouldFailWithInvalidCommand() {
    var user1 = new User("Frodo", "frodo@hobbiton.shire", "password");
    userService.save(user1);
    var user2 = new User("Bilbo", "frodo@hobbiton.shire", "password");
    assertThatThrownBy(() -> userService.save(user2))
        .isInstanceOf(EmailAddressInUseException.class)
        .hasMessage("Email address frodo@hobbiton.shire already in use");
  }

  @Test
  void whenSavedAndThenDelete_thenTheUserShouldNotFindThatUser() {
    var user = userService.save(new User("Frodo", "frodo@hobbiton.shire", "password"));
    var id = user.getId();
    assertThat(id).isNotNull();
    userService.delete(user);
    assertThat(userService.findById(id)).isEmpty();
  }
}
