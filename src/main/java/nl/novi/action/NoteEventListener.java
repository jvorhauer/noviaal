package nl.novi.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NoteEventListener {

  @EventListener
  public void handle(NoteEvent event) {
    log.info("handle: received event: {}", event);
  }
}
