package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.app.thread.application.exception.FailedToReadPageException;
import pl.app.thread.application.port.in.FetchAndSaveThread;
import pl.app.thread.application.port.in.FetchThread;
import pl.app.thread.application.port.in.FetchThreadListAndDelegateAllToKafka;
import pl.app.thread.application.port.in.FetchThreadWithListAndDelegateNotFetchedToKafka;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;
import pl.app.thread.application.port.out.ReadPage;
import pl.app.thread.application.port.out.persistance.CreatePort;
import pl.app.thread.application.port.out.persistance.FetchAllByMainThreadIdPort;
import pl.app.thread.application.port.out.persistance.FetchByThreadIdPort;
import pl.app.thread.domain.Thread;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
class ThreadService implements
        FetchAndSaveThread,
        FetchThread,
        FetchThreadListAndDelegateAllToKafka,
        FetchThreadWithListAndDelegateNotFetchedToKafka {
    private final Logger logger = LoggerFactory.getLogger(ThreadService.class);
    private final ReadPage readPage;
    private final FetchByThreadIdPort fetchByThreadIdPort;
    private final CreatePort createPort;
    private final FetchAllByMainThreadIdPort fetchAllByMainThreadIdPort;
    private final DelegateThreadToFetchService delegateToFetchService;
    private final ExtractThreadFromPageService extractService;


    @Override
    public void fetchThreadListAndDelegateAllToKafka(ThreadListToFetchMessage message) {
        List<Thread> threadList = extractService.extractThreadListFromThreadList(message.getUrl());
        threadList.forEach(thread -> delegateToFetchService.delegateThreadWithListToFetchToKafka(
                new ThreadWithListToFetchMessage(thread.getURL(), message.getIndustryName())));
    }

    @Override
    public void fetchAndSaveThread(ThreadToFetchMessage message) {
        Thread fetchedThread = fetchThread(message.getUrl());
        fetchedThread.setMainThreadId(message.getMainThreadId());
        fetchedThread.setIndustryName(message.getIndustryName());
        createPort.create(fetchedThread);
    }

    @Override
    public void fetchThreadWithListAndDelegateNotFetchedToKafka(ThreadWithListToFetchMessage message) {
        Thread fetchedThread = fetchThread(message.getUrl());
        fetchedThread.setIndustryName(message.getIndustryName());

        List<Thread> fetchedThreadList = extractService.extractThreadsFromThreadTree(message.getUrl());
        fetchedThreadList.add(fetchedThread);

        Thread mainThread = fetchedThreadList.stream().min(Comparator.comparing(Thread::getCreateDateTime)).orElseThrow(() -> new RuntimeException("Something went wrong!"));
        fetchedThreadList.forEach(thread -> thread.setMainThreadId(mainThread.getThreadId()));
        // if it is new thread save him to database
        try {
            Thread thread = fetchByThreadIdPort.fetchByThreadId(fetchedThread.getThreadId());
        } catch (RuntimeException exception) {
            createPort.create(fetchedThread);
        }

        List<Long> savedThreadIdList = fetchAllByMainThreadIdPort.fetchAllByMainThreadId(mainThread.getThreadId())
                .stream().map(Thread::getThreadId).toList();

        fetchedThreadList.stream()
                .filter(thread -> !savedThreadIdList.contains(thread.getThreadId()))
                .forEach(thread -> delegateToFetchService.delegateThreadToFetchToKafka(
                        new ThreadToFetchMessage(thread.getURL(), thread.getMainThreadId(), message.getIndustryName())
                ));
    }

    @Override
    public Thread fetchThread(String url) {
        Document doc = readPage.readPage(url).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));
        Thread fetchedThread = extractService.extractThreadFromBoxThread(doc.select("#boxThread"));
        fetchedThread.setThreadId(extractService.extractThreadIdFromLink(url));
        fetchedThread.setURL(url);
        fetchedThread.setHasBeenFetched(true);
        return fetchedThread;
    }

}
