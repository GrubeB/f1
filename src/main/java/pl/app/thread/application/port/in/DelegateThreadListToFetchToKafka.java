package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;

public interface DelegateThreadListToFetchToKafka {
    void delegateThreadListToFetchToKafka(ThreadListToFetchMessage message);
}
