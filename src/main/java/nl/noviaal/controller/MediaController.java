package nl.noviaal.controller;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.Media;
import nl.noviaal.exception.MediaInvalidException;
import nl.noviaal.exception.MediaNotFoundException;
import nl.noviaal.model.response.ItemResponse;
import nl.noviaal.model.response.MediaUploadResponse;
import nl.noviaal.service.MediaService;
import nl.noviaal.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    if (file == null) {
      log.error("store: file is null");
      throw new MediaInvalidException("Media file is null");
    }
    if (!StringUtils.hasText(file.getOriginalFilename())) {
      log.error("store: original file name is null or empty");
      throw new MediaInvalidException("Media file name is null or empty");
    }
    log.info("store: original file name: [{}]", file.getOriginalFilename());
    var filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (filename.contains("..")) {
      log.error("store: original file name contains illegal characters");
      throw new MediaInvalidException("Media name contains characters that could be used for malicious purposes");
    }

    var user = findCurrentUser(authentication);
    log.info("upload: {} by {}", filename, user.getEmail());
    Media media = mediaService.store(file, user);
    return MediaUploadResponse.ofMedia(media, file.getSize());
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

  @GetMapping("/{userId}/list")
  public List<ItemResponse> listByUser(@PathVariable("userId") UUID userId) {
    return findUserById(userId)
             .getMedia().stream()
             .map(ItemResponse::from)
             .collect(Collectors.toList());
  }
}
