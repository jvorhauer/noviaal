package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Media;
import nl.noviaal.domain.User;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.exception.MediaNotFoundException;
import nl.noviaal.model.response.MediaUploadResponse;
import nl.noviaal.service.UserService;
import nl.noviaal.service.MediaService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@Slf4j
public class MediaController extends AbstractController {

  private final MediaService mediaService;

  public MediaController(MediaService mediaService, UserService userService) {
    super(userService);
    this.mediaService = mediaService;
  }


  @PostMapping(value = {"", "/"})
  public MediaUploadResponse upload(@RequestParam("file") MultipartFile file, Authentication authentication) {
    var email = getUserEmail(authentication);
    log.info("upload: {} by {}", file.getOriginalFilename(), email);
    Optional<User> ouser = userService.findByEmail(email);
    Media media = ouser.map(user -> mediaService.store(file, user))
                    .orElseThrow(() -> new UserNotFoundException(email));
    return new MediaUploadResponse(media.getId(), media.getName(), media.getContentType(), file.getSize());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> get(@PathVariable("id") UUID id) {
    Media media = mediaService.find(id).orElseThrow(() -> new MediaNotFoundException(id));
    MediaType mt = MediaType.parseMediaType(media.getContentType());
    return ResponseEntity.ok()
             .contentType(mt)
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getName() + "\"")
             .body(new ByteArrayResource(media.getContent()));
  }
}
