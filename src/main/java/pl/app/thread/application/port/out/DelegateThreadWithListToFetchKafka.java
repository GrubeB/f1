package pl.app.thread.application.port.out;

import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;

public interface DelegateThreadWithListToFetchKafka {
    void sendToKafka(final ThreadWithListToFetchMessage data);
}
