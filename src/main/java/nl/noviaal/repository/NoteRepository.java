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
  @Query("SELECT item FROM User u INNER JOIN u.items item WHERE u=:user ORDER BY item.updated DESC")
  Page<Item> paginateNotes(@Param("user") User user, Pageable pageable);

  @Query("SELECT item FROM User u INNER JOIN u.items item WHERE u in :list ORDER BY item.updated DESC")
  Page<Item> timeline(@Param("list") List<User> list, Pageable pageable);

  // see https://dev.to/fabiothiroki/setup-spring-and-postgres-for-full-text-search-4n97
}
