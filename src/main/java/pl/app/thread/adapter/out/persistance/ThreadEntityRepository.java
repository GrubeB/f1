package pl.app.thread.adapter.out.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface ThreadEntityRepository extends JpaRepository<ThreadEntity, Long> {
    List<ThreadEntity> findAllByMainThreadId(Long mainThreadId);

    Optional<ThreadEntity> findByThreadId(Long threadId);
    List<ThreadEntity> findAllByIsMainThreadTrueAndCreateDateTimeGreaterThanEqualAndCreateDateTimeLessThanEqual(LocalDateTime from, LocalDateTime to);
    List<ThreadEntity> findAllByMainThreadIdIn(List<Long> mainThreadIdList);
}
