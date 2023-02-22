package pl.app.thread.adapter.out.persistance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.app.thread.application.port.out.persistance.*;
import pl.app.thread.domain.Thread;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@Getter
@RequiredArgsConstructor
class ThreadPersistenceAdapter implements
        CreatePort,
        CreateIfNotExistsByThreadIdPort,
        DeletePort,
        FetchAllPort,
        FetchAllByMainThreadIdPort,
        FetchByIdPort,
        FetchByThreadIdPort,
        UpdatePort,
        CountPort{

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
