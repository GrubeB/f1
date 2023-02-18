package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

public interface FetchByIdPort {
    Thread fetchById(Long id);
}
