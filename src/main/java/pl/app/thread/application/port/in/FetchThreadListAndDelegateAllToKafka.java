package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;

public interface FetchThreadListAndDelegateAllToKafka {
    void fetchThreadListAndDelegateAllToKafka(ThreadListToFetchMessage message);
}
