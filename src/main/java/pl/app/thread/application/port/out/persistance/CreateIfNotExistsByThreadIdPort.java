package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

public interface CreateIfNotExistsByThreadIdPort {
    Thread createIfNotExistsByThreadIdPort(Thread domain);
}
