package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

import java.util.Optional;

public interface FetchByIdPort {
    Thread fetchById(Long id);
}
