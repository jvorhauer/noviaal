package nl.noviaal.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.noviaal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTestSupportService {

  private final UserRepository userRepository;

  @Transactional
  public void truncate() {
    userRepository.deleteAll();
  }
}
