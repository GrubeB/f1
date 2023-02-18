package pl.app.thread.application.port.in;

public interface DelegateThreadToFetchToKafka {
    void delegateThreadToFetchToKafka(String url, Long mainThreadId);
}
