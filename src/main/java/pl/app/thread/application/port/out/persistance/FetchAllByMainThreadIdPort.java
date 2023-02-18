package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

import java.util.List;

public interface FetchAllByMainThreadIdPort {
    List<Thread> fetchAllByMainThreadId(Long mainThreadId);
}
