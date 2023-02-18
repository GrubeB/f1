package pl.app.thread.application.port.out.persistance;

import java.util.List;
import pl.app.thread.domain.Thread;
public interface FetchAllPort{
    List<Thread> fetchAll();
}
