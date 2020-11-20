package nl.novi.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service("userDetailsService")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository users;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public UserDetailsServiceImpl(UserRepository users) {
    this.users = users;
  }

  @PostConstruct
  @Transactional
  public void postConstruct() {
    log.info("postConstruct: create two users");
    var encpw = encoder.encode("password");
    var u1 = new User("nick@novi.nl", encpw);
    u1.setRole(Role.USER);
    users.save(u1);

    var u2 = new User("jurjen@vorhauer.nl", encpw);
    u2.setRole(Role.USER);
    users.save(u2);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("loadUserByUsername: {}", username);
    var user = users.findByEmail(username);
    if (user.isEmpty()) {
      log.error("loadUserByUsername: {} not found", username);
      throw new UsernameNotFoundException("Could not find user " + username);
    }
    log.info("loadUserByUsername: found {}", user.get());
    return user.map(UserDetailsImpl::new).get();
  }
}
