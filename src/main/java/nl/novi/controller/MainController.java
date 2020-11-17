package nl.novi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class MainController {

  @GetMapping("/")
  public String root() {
    log.info("root");
    return "redirect:/index";
  }

  @GetMapping("/index")
  public String index() {
    log.info("index");
    return "index";
  }

  @RequestMapping("/login")
  public String login(final Model model, final HttpServletRequest req) {
    log.info("login: {}", req.getMethod());
    model.addAttribute("loginError", false);
    return "login";
  }

  @GetMapping("/login-error")
  public String loginError(final Model model) {
    log.error("login-error");
    model.addAttribute("loginError", true);
    return "login";
  }
}
