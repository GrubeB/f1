package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.app.thread.application.port.in.FetchAndSaveThreadAsync;
import pl.app.thread.application.port.in.FetchThreadListAndDelegateAllToKafkaAsync;
import pl.app.thread.application.port.in.FetchThreadWithListAndDelegateNotFetchedToKafkaAsync;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
class ThreadAsyncService implements
        FetchAndSaveThreadAsync,
        FetchThreadListAndDelegateAllToKafkaAsync,
        FetchThreadWithListAndDelegateNotFetchedToKafkaAsync {
    private final ThreadService threadService;
    ExecutorService executor = Executors.newFixedThreadPool(50);

    @Override
    public void fetchThreadListAndDelegateAllToKafka(ThreadListToFetchMessage message) {
        executor.submit(() -> threadService.fetchThreadListAndDelegateAllToKafka(message));
    }

    @Override
    public void fetchAndSaveThread(ThreadToFetchMessage message) {
        executor.submit(() -> threadService.fetchAndSaveThread(message));
    }

    @Override
    public void fetchThreadWithListAndDelegateNotFetchedToKafka(ThreadWithListToFetchMessage message) {
        executor.submit(() -> threadService.fetchThreadWithListAndDelegateNotFetchedToKafka(message));
    }
}
