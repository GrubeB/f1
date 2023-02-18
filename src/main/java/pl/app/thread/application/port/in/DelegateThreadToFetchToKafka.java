package pl.app.thread.application.port.in;

import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;

public interface DelegateThreadToFetchToKafka {
    void delegateThreadToFetchToKafka(ThreadToFetchMessage message);
}
