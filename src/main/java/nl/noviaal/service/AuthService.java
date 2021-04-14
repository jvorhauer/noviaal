package nl.noviaal.service;

import nl.noviaal.domain.User;
import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.model.auth.JwtResponse;
import nl.noviaal.model.auth.JwtUtils;
import nl.noviaal.model.auth.UserDetailsImpl;
import nl.noviaal.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtUtils jwtUtils;


  public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.jwtUtils = jwtUtils;
  }

  public JwtResponse login(String email, String password) {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
    Authentication authenticated = authenticationManager.authenticate(authentication);

    SecurityContextHolder.getContext().setAuthentication(authenticated);
    String jwt = jwtUtils.generateJwtToken(authenticated);
    UserDetailsImpl userDetails = (UserDetailsImpl) authenticated.getPrincipal();
    return new JwtResponse(
      jwt,
      userDetails.getId(),
      userDetails.getUsername(),
      userDetails.getEmail(),
      userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
    );
  }

  @Transactional
  public User register(User user) {
    return userRepository.findByEmail(user.getEmail())
             .map(userRepository::save)
             .orElseThrow(() -> new EmailAddressInUseException(user.getEmail()));
  }
}
