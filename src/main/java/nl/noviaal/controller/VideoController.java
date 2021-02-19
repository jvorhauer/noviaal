package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.User;
import nl.noviaal.domain.Video;
import nl.noviaal.exception.UserNotFoundException;
import nl.noviaal.exception.VideoNotFoundException;
import nl.noviaal.model.response.VideoUploadResponse;
import nl.noviaal.service.UserService;
import nl.noviaal.service.VideoService;
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
@RequestMapping("/api/videos")
@Slf4j
public class VideoController extends AbstractController {

  private final VideoService videoService;
  private final UserService userService;

  public VideoController(VideoService videoService, UserService userService) {
    this.videoService = videoService;
    this.userService = userService;
  }


  @PostMapping(value = {"", "/"})
  public VideoUploadResponse upload(@RequestParam("file") MultipartFile file, Authentication authentication) {
    var email = getUserEmail(authentication);
    log.info("upload: {} by {}", file.getOriginalFilename(), email);
    Optional<User> ouser = userService.findByEmail(email);
    Video video = ouser.map(user -> videoService.store(file, user))
                    .orElseThrow(() -> new UserNotFoundException(email));
    return new VideoUploadResponse(video.getId(), video.getName(), video.getContentType(), file.getSize());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> get(@PathVariable("id") UUID id) {
    Video video = videoService.find(id).orElseThrow(() -> new VideoNotFoundException(id));
    MediaType mt = MediaType.parseMediaType(video.getContentType());
    return ResponseEntity.ok()
             .contentType(mt)
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getName() + "\"")
             .body(new ByteArrayResource(video.getVideo()));
  }
}
