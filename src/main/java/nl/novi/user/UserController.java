package nl.novi.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

  @PostMapping("/register")
  public String register() {
    return "redirect:/login";
  }
}
