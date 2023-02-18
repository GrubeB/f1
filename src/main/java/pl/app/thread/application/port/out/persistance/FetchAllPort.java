package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

import java.util.List;

public interface FetchAllPort {
    List<Thread> fetchAll();
}
