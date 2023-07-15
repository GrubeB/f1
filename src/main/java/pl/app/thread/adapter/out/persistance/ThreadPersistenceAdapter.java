package pl.app.thread.adapter.out.persistance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.app.thread.application.port.out.persistance.*;
import pl.app.thread.domain.Thread;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Setter
@Getter
@RequiredArgsConstructor
class ThreadPersistenceAdapter implements
        CreatePort,
        CreateIfNotExistsByThreadIdPort,
        DeletePort,
        FetchAllPort,
        FindAllBetweenDates,
        FetchAllByMainThreadIdPort,
        FetchByIdPort,
        FetchByThreadIdPort,
        UpdatePort,
        CountPort {

    private final ThreadEntityRepository repository;
    private final ThreadEntityMapper mapper = Mappers.getMapper(ThreadEntityMapper.class);

    @Override
    public Thread create(Thread domain) {
        ThreadEntity threadEntity = mapper.domainToEntity(domain);
        ThreadEntity saved = repository.save(threadEntity);
        return mapper.entityToDomain(saved);
    }

    @Override
    public Thread createIfNotExistsByThreadIdPort(Thread domain) {
        ThreadEntity threadEntity = mapper.domainToEntity(domain);
        Optional<ThreadEntity> byThreadId = repository.findByThreadId(threadEntity.getThreadId());
        if (byThreadId.isPresent()) {
            return mapper.entityToDomain(byThreadId.get());
        } else {
            return mapper.entityToDomain(repository.save(threadEntity));
        }
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Thread> fetchAll() {
        return repository.findAll().stream()
                .map(mapper::entityToDomain).toList();
    }

    @Override
    public List<Thread> findAllBetweenDates(LocalDate from, LocalDate to) {
        List<ThreadEntity> mainThreadList = repository.findAllByIsMainThreadTrueAndCreateDateTimeGreaterThanEqualAndCreateDateTimeLessThanEqual(
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        );
        List<Long> mainThreadIdList = mainThreadList.stream().map(ThreadEntity::getThreadId).toList();
        List<ThreadEntity> result = mainThreadIdList.stream().collect(blockCollector(65_535))
                .stream().map(repository::findAllByMainThreadIdIn)
                .flatMap(List::stream)
                .toList();// IN operator can have at most 65 535 parameters used in ThreadEntityRepository#findAllByMainThreadIdIn
        return result.stream().map(mapper::entityToDomain).toList();
    }
    public static <T> Collector<T, List<List<T>>, List<List<T>>> blockCollector(int blockSize) {
        return Collector.of(
                ArrayList::new,
                (list, value) -> {
                    List<T> block = (list.isEmpty() ? null : list.get(list.size() - 1));
                    if (block == null || block.size() == blockSize)
                        list.add(block = new ArrayList<>(blockSize));
                    block.add(value);
                },
                (r1, r2) -> { throw new UnsupportedOperationException("Parallel processing not supported"); }
        );
    }

    @Override
    public List<Thread> fetchAllByMainThreadId(Long mainThreadId) {
        return repository.findAllByMainThreadId(mainThreadId).stream()
                .map(mapper::entityToDomain).toList();
    }

    @Override
    public Thread fetchById(Long id) {
        return repository.findById(id).map(mapper::entityToDomain)
                .orElseThrow(() -> new RuntimeException("Not found object with id: " + id));
    }

    @Override
    public Thread fetchByThreadId(Long threadId) {
        return repository.findByThreadId(threadId).map(mapper::entityToDomain)
                .orElseThrow(() -> new RuntimeException("Not found object with id: " + threadId));
    }

    @Override
    public Thread update(Long id, Thread domain) {
        Thread thread = fetchById(id);
        ThreadEntity threadEntity = mapper.domainToEntity(domain);
        threadEntity.setId(id);
        ThreadEntity saved = repository.save(threadEntity);
        return mapper.entityToDomain(saved);
    }

    @Override
    public Long count() {
        return repository.count();
    }
}
