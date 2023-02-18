package pl.app.thread.adapter.out.persistance;

import org.mapstruct.Mapper;
import pl.app.thread.domain.Thread;

@Mapper
public interface ThreadEntityMapper {
    ThreadEntity domainToEntity(Thread domain);
    Thread entityToDomain(ThreadEntity entity);
}
