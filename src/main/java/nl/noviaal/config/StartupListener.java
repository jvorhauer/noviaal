package nl.noviaal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {

  public void onApplicationEvent(ApplicationReadyEvent event) {
    log.info("onApplicationEvent: {}", event.getApplicationContext().getApplicationName());
  }
}
