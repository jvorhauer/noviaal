package nl.noviaal.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class BinderControllerAdvice {
  @InitBinder
  public void setAllowedFields(WebDataBinder wdb) {
    wdb.setDisallowedFields("class.*", "Class.*", "*.class.*", "*.Class.*");
  }
}
