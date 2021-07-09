package nl.noviaal.service;

import nl.noviaal.domain.User;
import nl.noviaal.domain.Media;
import nl.noviaal.exception.MediaInvalidException;
import nl.noviaal.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService {

  private final MediaRepository repo;

  public MediaService(MediaRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Media store(MultipartFile file, User user) {
    String filename = file.getOriginalFilename();
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
