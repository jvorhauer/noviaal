package nl.noviaal.repository;

import nl.noviaal.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowRepository  extends JpaRepository<Follow, UUID> {
}
