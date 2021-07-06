package nl.noviaal.model.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  @Value("${noviaal.jwt.expiration.ms}")
  private int jwtExpirationMs;


  public String generateJwtToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    Date now = new Date();
    return Jwts.builder()
             .setSubject((userPrincipal.getEmail()))
             .setIssuedAt(now)
             .setExpiration(new Date(now.getTime() + jwtExpirationMs))
             .signWith(KEY)
             .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return parseJwtToken(token).getBody().getSubject();
  }

  private Jws<Claims> parseJwtToken(String token) {
    return Jwts.parserBuilder()
             .setSigningKey(KEY)
             .build()
             .parseClaimsJws(token);
  }

  public boolean validateJwtToken(String token) {
    if (token != null) {
      try {
        parseJwtToken(token);
        return true;
      } catch (SecurityException e) {
        logger.error("Invalid JWT security: {}", e.getMessage());
      } catch (MalformedJwtException e) {
        logger.error("Invalid JWT token: {}", e.getMessage());
      } catch (ExpiredJwtException e) {
        logger.error("JWT token is expired: {}", e.getMessage());
      } catch (UnsupportedJwtException e) {
        logger.error("JWT token is unsupported: {}", e.getMessage());
      } catch (IllegalArgumentException e) {
        logger.error("JWT claims string is empty: {}", e.getMessage());
      }
    }
    return false;
  }
}
