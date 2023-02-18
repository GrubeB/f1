package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.app.thread.application.exception.FailedToReadPageException;
import pl.app.thread.application.port.in.*;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;
import pl.app.thread.application.port.out.DelegateThreadListToFetchKafka;
import pl.app.thread.application.port.out.DelegateThreadToFetchKafka;
import pl.app.thread.application.port.out.DelegateThreadWithListToFetchKafka;
import pl.app.thread.application.port.out.ReadPage;
import pl.app.thread.application.port.out.persistance.CreatePort;
import pl.app.thread.application.port.out.persistance.FetchAllByMainThreadIdPort;
import pl.app.thread.application.port.out.persistance.FetchByThreadIdPort;
import pl.app.thread.domain.Thread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
class ThreadService implements
        DelegateThreadListToFetchToKafka,
        DelegateThreadToFetchToKafka,
        DelegateThreadWithListToFetchToKafka,
        FetchAndSaveThread,
        FetchThread,
        FetchThreadListAndDelegateAllToKafka,
        FetchThreadWithListAndDelegateNotFetchedToKafka
{
    private final Logger logger = LoggerFactory.getLogger(ThreadService.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ReadPage readPage;
    private final FetchByThreadIdPort fetchByThreadIdPort;
    private final CreatePort createPort;
    private final FetchAllByMainThreadIdPort fetchAllByMainThreadIdPort;
    private final DelegateThreadListToFetchKafka delegateThreadListToFetchKafka;
    private final DelegateThreadToFetchKafka delegateThreadToFetchKafka;
    private final DelegateThreadWithListToFetchKafka delegateThreadWithListToFetchKafka;


    @Override
    public void fetchThreadListAndDelegateAllToKafka(ThreadListToFetchMessage message) {
        List<Thread> threadList = extractThreadListFromThreadList(message.getUrl());
        threadList.forEach(thread -> delegateThreadWithListToFetchToKafka(thread.getURL()));
    }
    @Override
    public void fetchAndSaveThread(ThreadToFetchMessage message) {
        Thread fetchedThread = fetchThread(message.getUrl());
        fetchedThread.setMainThreadId(message.getMainThreadId());
        createPort.create(fetchedThread);
    }
    @Override
    public void fetchThreadWithListAndDelegateNotFetchedToKafka(ThreadWithListToFetchMessage message) {
        Thread fetchedThread = fetchThread(message.getUrl());
        List<Thread> fetchedThreadList = extractThreadsFromThreadTree(message.getUrl());
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
                .forEach(thread -> this.delegateThreadToFetchToKafka(thread.getURL(), thread.getMainThreadId()));
    }
    @Override
    public Thread fetchThread(String url) {
        Document doc = readPage.readPage(url).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));
        Thread fetchedThread = extractThreadFromBoxThread(doc.select("#boxThread"));
        fetchedThread.setThreadId(extractThreadIdFromLink(url));
        fetchedThread.setURL(url);
        fetchedThread.setHasBeenFetched(true);
        return fetchedThread;
    }

    //-------------------------DELEGATE------------------------------------------------------------------------------------------
    @Override
    public void delegateThreadListToFetchToKafka(String url) {
        ThreadListToFetchMessage message = new ThreadListToFetchMessage(url);
        delegateThreadListToFetchKafka.sendToKafka(message);
    }
    @Override
    public void delegateThreadToFetchToKafka(String url, Long mainThreadId) {
        ThreadToFetchMessage message = new ThreadToFetchMessage(url, mainThreadId);
        delegateThreadToFetchKafka.sendToKafka(message);
    }
    @Override
    public void delegateThreadWithListToFetchToKafka(String url) {
        ThreadWithListToFetchMessage message = new ThreadWithListToFetchMessage(url);
        delegateThreadWithListToFetchKafka.sendToKafka(message);
    }


    // TODO extract that to another class
    //-------------------------EXTRACT------------------------------------------------------------------------------------------
    private Thread extractThreadFromBoxThread(Elements boxThread) {
        try {
            Elements boxMeta = boxThread.select(".boxMeta");
            Elements entryMeta = boxMeta.select(".entry-meta");
            String authorName = entryMeta.select(".author").text().split(":", 2)[1].trim();
            String createDateTimeString = entryMeta.select(".entry-date").text().trim();
            LocalDateTime createDateTime = LocalDateTime.parse(createDateTimeString, formatter);

            Elements lastPrice = boxMeta.select(".lastPrice");
            String exchangeRateText = lastPrice.text().trim();
            String exchangeRateString = exchangeRateText.substring(exchangeRateText.indexOf(":") + 1, exchangeRateText.indexOf("zł")).trim().replace(',', '.');
            Double exchangeRate = Double.parseDouble(exchangeRateString);
            String exchangeRateChangeText = lastPrice.select(".change").text();
            String exchangeRateChangeString = exchangeRateChangeText.substring(0, exchangeRateChangeText.indexOf("%")).trim().replace(',', '.');
            Double exchangeRateChange = Double.parseDouble(exchangeRateChangeString);

            String title = boxThread.select(".boxHeader h1").text();

            Elements boxContent = boxThread.select(".boxContent");
            String comment = boxContent.select(".p").text();

            Elements boxFooter = boxThread.select(".boxFooter");
            String numberOfDislikesString = boxFooter.select(".addCommentDown .voteValue").text();
            Integer numberOfDislikes = Integer.parseInt(numberOfDislikesString);
            String numberOfLikesString = boxFooter.select(".addCommentUp .voteValue").text();
            Integer numberOfLikes = Integer.parseInt(numberOfLikesString);

            return Thread.builder()
                    .title(title)
                    .authorName(authorName)
                    .createDateTime(createDateTime)
                    .exchangeRate(exchangeRate)
                    .exchangeRateChange(exchangeRateChange)
                    .comment(comment)
                    .numberOfDislikes(numberOfDislikes)
                    .numberOfLikes(numberOfLikes)
                    .build();
        } catch (Exception exception) {
            return new Thread();
        }
    }

    private List<Thread> extractThreadListFromThreadList(String link) {
        Document doc = readPage.readPage(link).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));

        List<Thread> threadList = new ArrayList<>(10);
        for (Element row : doc.select(".threadsList tbody tr")) {
            try {
                String title = row.select(".threadTitle a").get(0).text().trim();
                String threadLink = row.select(".threadTitle a").get(0).absUrl("href");
                Long threadId = extractThreadIdFromLink(threadLink);
                String authorName = row.select(".threadAuthor").get(0).text().trim();
                String numberOfSubThreadsString = row.select(".threadCount span").get(0).text().trim();
                String createDateTimeText = row.select(".createDate").get(0).text().trim();
                LocalDateTime createDateTime = LocalDateTime.parse(createDateTimeText, formatter);
                Thread thread = Thread.builder()
                        .threadId(threadId)
                        .title(title)
                        .URL(threadLink)
                        .authorName(authorName)
                        .createDateTime(createDateTime)
                        .hasBeenFetched(false)
                        .build();
                threadList.add(thread);
            } catch (Exception exception) {
            }
        }
        return threadList;
    }

    private List<Thread> extractThreadsFromThreadTree(String link) {
        Document doc = readPage.readPage(link).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));

        List<Thread> threadList = new ArrayList<>(10);
        for (Element row : doc.select("#boxThreadTree .boxContent .threadTree li")) {
            try {
                String title = row.select("a").text().trim();
                String threadLink = row.select("a").get(0).absUrl("href");
                Long threadId = extractThreadIdFromLink(threadLink);
                String authorName = row.select(".author").get(0).text().split(":", 2)[1].trim();
                String createDateTimeText = row.select(".entry-date").get(0).text().trim();
                LocalDateTime createDateTime = LocalDateTime.parse(createDateTimeText, formatter);
                Thread thread = Thread.builder()
                        .threadId(threadId)
                        .title(title)
                        .URL(threadLink)
                        .authorName(authorName)
                        .createDateTime(createDateTime)
                        .hasBeenFetched(false)
                        .build();
                threadList.add(thread);
            } catch (Exception exception) {
            }
        }
        return threadList;
    }

    private Long extractThreadIdFromLink(String link) {
        try {
            String substring = link.substring(0, link.lastIndexOf(".html"));
            String idString = substring.substring(link.lastIndexOf(",") + 1);
            return Long.parseLong(idString);

        } catch (Exception exception) {
            return -1L;
        }
    }
}
