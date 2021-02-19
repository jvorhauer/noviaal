package nl.noviaal.service;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.domain.User;
import nl.noviaal.model.auth.JwtResponse;
import nl.noviaal.model.auth.JwtUtils;
import nl.noviaal.model.auth.UserDetailsImpl;
import nl.noviaal.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtUtils jwtUtils;


  public AuthService(
    AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtils jwtUtils
  ) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.jwtUtils = jwtUtils;
  }

  public JwtResponse login(String email, String password) {
    var authentication = new UsernamePasswordAuthenticationToken(email, password);
    var authenticated = authenticationManager.authenticate(authentication);

    SecurityContextHolder.getContext().setAuthentication(authenticated);
    String jwt = jwtUtils.generateJwtToken(authenticated);
    UserDetailsImpl userDetails = (UserDetailsImpl) authenticated.getPrincipal();
    return new JwtResponse(
      jwt,
      userDetails.getId(),
      userDetails.getUsername(),
      userDetails.getEmail(),
      "USER"
    );
  }

  @Transactional
  public User register(User user) {
    log.info("register: {}", user);
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new EmailAddressInUseException(user.getEmail());
    }
    return userRepository.save(user);
  }
}
