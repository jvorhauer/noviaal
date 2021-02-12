package nl.noviaal.service;

import nl.noviaal.model.auth.UserDetailsImpl;
import nl.noviaal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final var user = userRepository.findByEmail(username)
                       .orElseThrow(() -> new UsernameNotFoundException("User [" + username + "] not found"));
    return UserDetailsImpl.build(user);
  }
}
