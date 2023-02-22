package pl.app.thread.adapter.out.persistance;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.app.thread.domain.Thread;

@Mapper
interface ThreadEntityMapper {
    @Mapping(source = "domain", target = "isMainThread", qualifiedByName = "setMainThread")
    ThreadEntity domainToEntity(Thread domain);

    Thread entityToDomain(ThreadEntity entity);

    @Named("setMainThread")
    default boolean setMainThread(Thread domain) {
        return domain.getThreadId().equals(domain.getMainThreadId());
    }
}
