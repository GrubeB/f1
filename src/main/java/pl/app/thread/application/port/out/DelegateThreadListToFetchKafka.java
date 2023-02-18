package pl.app.thread.application.port.out;

import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;

public interface DelegateThreadListToFetchKafka {
    void sendToKafka(final ThreadListToFetchMessage data);
}
