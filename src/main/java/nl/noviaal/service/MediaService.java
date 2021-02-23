package nl.noviaal.service;

import lombok.extern.slf4j.Slf4j;
import nl.noviaal.domain.User;
import nl.noviaal.domain.Media;
import nl.noviaal.exception.MediaInvalidException;
import nl.noviaal.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MediaService {

  private final MediaRepository repo;

  public MediaService(MediaRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Media store(MultipartFile file, User user) {
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

    try {
      var video = new Media(filename, file.getContentType(), file.getBytes());
      video.setAuthor(user);
      return repo.save(video);
    } catch (IOException e) {
      throw new MediaInvalidException("Could not store " + filename + ": " + e.getMessage());
    }
  }

  public Optional<Media> find(UUID id) {
    return repo.findById(id);
  }
}
