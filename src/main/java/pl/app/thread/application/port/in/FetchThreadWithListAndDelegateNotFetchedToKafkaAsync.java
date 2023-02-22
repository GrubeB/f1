package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;

public interface FetchThreadWithListAndDelegateNotFetchedToKafkaAsync {
    void fetchThreadWithListAndDelegateNotFetchedToKafka(ThreadWithListToFetchMessage message);
}
