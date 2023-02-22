package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;

public interface FetchAndSaveThreadAsync {
    void fetchAndSaveThread(ThreadToFetchMessage message);
}
