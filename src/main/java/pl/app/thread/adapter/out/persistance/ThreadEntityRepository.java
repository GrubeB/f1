package pl.app.thread.adapter.out.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.app.thread.domain.Thread;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThreadEntityRepository extends JpaRepository<ThreadEntity,Long> {
    List<ThreadEntity> findAllByMainThreadId(Long mainThreadId);
    Optional<ThreadEntity> findByThreadId(Long threadId);
}
