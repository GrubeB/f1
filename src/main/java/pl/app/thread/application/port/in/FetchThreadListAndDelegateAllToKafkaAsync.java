package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;

public interface FetchThreadListAndDelegateAllToKafkaAsync {
    void fetchThreadListAndDelegateAllToKafka(ThreadListToFetchMessage message);
}
