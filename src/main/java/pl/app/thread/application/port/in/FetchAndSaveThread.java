package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;

public interface FetchAndSaveThread {
    void fetchAndSaveThread(ThreadToFetchMessage message);
}
