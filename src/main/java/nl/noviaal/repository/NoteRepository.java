package nl.noviaal.repository;

import nl.noviaal.domain.Item;
import nl.noviaal.domain.Note;
import nl.noviaal.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
  @Query("select item from User u inner join u.items item where u=:user")
  public Page<Item> paginateNotes(@Param("user") User user, Pageable pageable);

  @Query("select item from User u inner join u.items item where u in :list")
  public Page<Item> timeline(@Param("list") List<User> list, Pageable pageable);
}
