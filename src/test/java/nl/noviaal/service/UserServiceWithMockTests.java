package nl.noviaal.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import nl.noviaal.domain.User;
import nl.noviaal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("mocked")
public class UserServiceWithMockTests {

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Test
  void whenDatabaseIsEmpty_thenFindAll_shouldReturnEmptyList() {
    when(userRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
      .thenReturn(Page.empty());

    assertThat(userService.findAll()).isNotNull();
    assertThat(userService.findAll().getTotalElements()).isEqualTo(0L);
    assertThat(userService.findAll().hasContent()).isFalse();
    assertThat(userService.findAll().getContent()).hasSize(0);
  }

  @Test
  void whenOneUserSaved_thenFindAll_shouldReturnListWithThatUser() {
    when(userRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
      .thenReturn(new PageImpl<>(Collections.singletonList(new User("tester", "tester@test.com", "password"))));

    assertThat(userService.findAll().getTotalElements()).isEqualTo(1);
    Optional<User> ouser = userService.findAll().get().findFirst();
    assertThat(ouser).isPresent();
    assertThat(ouser.get().getEmail()).isEqualTo("tester@test.com");
  }
}
