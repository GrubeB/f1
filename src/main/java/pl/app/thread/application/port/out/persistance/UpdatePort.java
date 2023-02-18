package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

public interface UpdatePort {
    Thread update(Long id, Thread domain);
}
