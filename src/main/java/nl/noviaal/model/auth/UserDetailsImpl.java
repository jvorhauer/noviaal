package nl.noviaal.model.auth;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import nl.noviaal.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ToString
public class UserDetailsImpl implements UserDetails {

  private final UUID id;
  private final String username;
  private final String email;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(
    UUID id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities
  ) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {
    var authorities = Stream.of(user.getRoles() != null ? user.getRoles().split(",") : new String[]{"USER"})
                            .map(SimpleGrantedAuthority::new)
                            .toList();
    return new UserDetailsImpl(user.getId(), user.getName(), user.getEmail(), user.getPassword(), authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

  @Override
  @JsonIgnore
  public String getPassword() { return password; }

  public UUID getId() { return id; }

  public String getEmail() { return email; }

  @Override
  public String getUsername() { return username; }

  @Override
  public boolean isAccountNonExpired() { return true; }

  @Override
  public boolean isAccountNonLocked() { return true; }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() { return true; }
}
