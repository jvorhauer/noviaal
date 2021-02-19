package nl.noviaal.service;

import nl.noviaal.domain.User;
import nl.noviaal.domain.Video;
import nl.noviaal.exception.VideoStorageException;
import nl.noviaal.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class VideoService {

  private final VideoRepository repo;

  public VideoService(VideoRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Video store(MultipartFile file, User user) {
    if (file == null) {
      throw new VideoStorageException("Video file is null");
    }
    if (file.getOriginalFilename() == null) {
      throw new VideoStorageException("Video name is null");
    }
    var filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (filename.contains("..")) {
      throw new VideoStorageException("Video name contains characters that could be used for malicious purposes");
    }

    try {
      var video = new Video(filename, file.getContentType(), file.getBytes());
      video.setAuthor(user);
      return repo.save(video);
    } catch (IOException e) {
      throw new VideoStorageException("Could not store " + filename + ": " + e.getMessage());
    }
  }

  public Optional<Video> find(UUID id) {
    return repo.findById(id);
  }
}
