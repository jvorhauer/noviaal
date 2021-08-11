package nl.noviaal.repository;

import java.util.UUID;

import io.vavr.collection.List;
import nl.noviaal.domain.Like;
import nl.noviaal.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
  List<Like> findByUserId(@Param("userId") UUID userId);
  List<Like> findByNoteId(@Param("noteId") UUID noteId);
}
