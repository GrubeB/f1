package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.app.thread.application.exception.FailedToReadPageException;
import pl.app.thread.application.port.in.*;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;
import pl.app.thread.application.port.out.ReadPage;
import pl.app.thread.application.port.out.persistance.CreateIfNotExistsByThreadIdPort;
import pl.app.thread.application.port.out.persistance.CreatePort;
import pl.app.thread.application.port.out.persistance.FetchAllByMainThreadIdPort;
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
    private final CreatePort createPort;
    private final FetchAllByMainThreadIdPort fetchAllByMainThreadIdPort;
    private final CreateIfNotExistsByThreadIdPort createIfNotExistsByThreadIdPort;
    private final DelegateThreadToFetchService delegateToFetchService;
    private final ExtractThreadFromPageService extractService;

    private final DelegateThreadListToFetchToKafka delegateThreadListToFetchToKafka;
    private final DelegateThreadToFetchToKafka delegateThreadToFetchToKafka;
    private final DelegateThreadWithListToFetchToKafka delegateThreadWithListToFetchToKafka;

    @Override
    public void fetchThreadListAndDelegateAllToKafka(ThreadListToFetchMessage message) {
        try {
            List<Thread> threadList = extractService.extractThreadList(message.getUrl());
            threadList.forEach(thread -> delegateToFetchService.delegateThreadWithListToFetchToKafka(
                    new ThreadWithListToFetchMessage(thread.getURL(), message.getIndustryName())));
        } catch (FailedToReadPageException exception) {
            delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(message);
        }
    }

    @Override
    public void fetchAndSaveThread(ThreadToFetchMessage message) {
        try {
            Thread fetchedThread = fetchThread(message.getUrl());
            fetchedThread.setMainThreadId(message.getMainThreadId());
            fetchedThread.setIndustryName(message.getIndustryName());
            createPort.create(fetchedThread);
        } catch (FailedToReadPageException exception) {
            delegateThreadToFetchToKafka.delegateThreadToFetchToKafka(message);
        }

    }

    @Override
    public void fetchThreadWithListAndDelegateNotFetchedToKafka(ThreadWithListToFetchMessage message) {
        try {
            Thread fetchedThread = fetchThread(message.getUrl());
            fetchedThread.setIndustryName(message.getIndustryName());

            List<Thread> fetchedThreadList = getThreadFromThreadList(message.getUrl());

            fetchedThreadList.add(fetchedThread);

            Thread mainThread = fetchedThreadList.stream()
                    .min(Comparator.comparing(Thread::getCreateDateTime))
                    .orElseThrow(() -> new RuntimeException("Something went wrong!"));
            fetchedThreadList.forEach(thread -> thread.setMainThreadId(mainThread.getThreadId()));

            createIfNotExistsByThreadIdPort.createIfNotExistsByThreadIdPort(fetchedThread);

            List<Long> savedThreadIdList = fetchAllByMainThreadIdPort.fetchAllByMainThreadId(mainThread.getThreadId())
                    .stream().map(Thread::getThreadId).toList();

            fetchedThreadList.stream()
                    .filter(thread -> !savedThreadIdList.contains(thread.getThreadId()))
                    .forEach(thread -> delegateToFetchService.delegateThreadToFetchToKafka(
                            new ThreadToFetchMessage(thread.getURL(), thread.getMainThreadId(), message.getIndustryName())
                    ));
        } catch (FailedToReadPageException exception) {
            delegateThreadWithListToFetchToKafka.delegateThreadWithListToFetchToKafka(message);
        }

    }


    @Override
    public Thread fetchThread(String url) {
        Thread fetchedThread = extractService.extractThread(url);
        fetchedThread.setURL(url);
        fetchedThread.setThreadIdFromURL();
        return fetchedThread;
    }

    private List<Thread> getThreadFromThreadList(String url) {
        List<Thread> fetchedThreadList = extractService.extractThreadListFromBottomTable(url);
        List<String> pageUrlList = extractService.extractPageUrls(url);
        if (pageUrlList.size() > 1 && !pageUrlList.contains(url)) {
            pageUrlList.stream()
                    .skip(1)
                    .forEach((pageUrl) -> {
                        List<Thread> threadListFormNextPageList = extractService.extractThreadListFromBottomTable(pageUrl);
                        fetchedThreadList.addAll(threadListFormNextPageList);
                    });
            return fetchedThreadList;
        } else {
            return fetchedThreadList;
        }
    }


    private void ifThereAreMorePagesDelegateThreadWithList(ThreadWithListToFetchMessage message) {
        List<String> pageUrlList = extractService.extractPageUrls(message.getUrl());
        if (pageUrlList.size() > 1 && !pageUrlList.contains(message.getUrl())) {
            pageUrlList.stream()
                    .skip(1)
                    .forEach((url) -> delegateToFetchService.delegateThreadWithListToFetchToKafka(
                            new ThreadWithListToFetchMessage(url, message.getIndustryName())
                    ));
        }
    }

}
