package nl.noviaal.model.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger("AuthEntryPointJwt");

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException
  ) throws IOException {
    logger.error("commence: unauthorized error: {} for {}", authException.getMessage(), requestInfo(request));
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }

  private String requestInfo(HttpServletRequest request) {
    if (request != null) {
      return request.getMethod() + " " + request.getRequestURL().toString();
    }
    return "request is null!";
  }
}
