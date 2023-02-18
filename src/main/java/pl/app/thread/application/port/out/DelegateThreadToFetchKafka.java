package pl.app.thread.application.port.out;

import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;

public interface DelegateThreadToFetchKafka {
    void sendToKafka(final ThreadToFetchMessage data);
}
