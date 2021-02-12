package nl.noviaal.service;

import lombok.RequiredArgsConstructor;
import nl.noviaal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTestSupportService {

  private final UserRepository userRepository;

  @Transactional
  public void truncate() {
    userRepository.deleteAll();
  }
}
