package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

import java.util.Optional;

public interface FetchByThreadIdPort {
    Thread fetchByThreadId(Long threadId);
}
