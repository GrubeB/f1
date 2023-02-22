package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.app.thread.application.exception.FailedToReadPageException;
import pl.app.thread.application.port.out.ReadPage;
import pl.app.thread.domain.Thread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class ExtractThreadFromPageService {
    private final Logger logger = LoggerFactory.getLogger(ExtractThreadFromPageService.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ReadPage readPage;


    public Thread extractThreadFromBoxThread(Elements boxThread) {
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

    public List<Thread> extractThreadListFromThreadList(String url) {
        Document doc = readPage.readPage(url).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));

        List<Thread> threadList = new ArrayList<>(10);
        for (Element row : doc.select(".threadsList tbody tr")) {
            try {
                String title = row.select(".threadTitle a").get(0).text().trim();
                String threadLink = row.select(".threadTitle a").get(0).absUrl("href");
                String authorName = row.select(".threadAuthor").get(0).text().trim();
                String numberOfSubThreadsString = row.select(".threadCount span").get(0).text().trim();
                String createDateTimeText = row.select(".createDate").get(0).text().trim();
                LocalDateTime createDateTime = LocalDateTime.parse(createDateTimeText, formatter);
                Thread thread = Thread.builder()
                        .title(title)
                        .URL(threadLink)
                        .authorName(authorName)
                        .createDateTime(createDateTime)
                        .hasBeenFetched(false)
                        .build();
                thread.setThreadIdFromURL();
                threadList.add(thread);
            } catch (Exception exception) {
            }
        }
        return threadList;
    }

    public List<Thread> extractThreadsFromThreadTree(String url) {
        Document doc = readPage.readPage(url).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));

        List<Thread> threadList = new ArrayList<>(10);
        for (Element row : doc.select("#boxThreadTree .boxContent .threadTree li")) {
            try {
                String title = row.select("a").text().trim();
                String threadLink = row.select("a").get(0).absUrl("href");
                String authorName = row.select(".author").get(0).text().split(":", 2)[1].trim();
                String createDateTimeText = row.select(".entry-date").get(0).text().trim();
                LocalDateTime createDateTime = LocalDateTime.parse(createDateTimeText, formatter);
                Thread thread = Thread.builder()
                        .title(title)
                        .URL(threadLink)
                        .authorName(authorName)
                        .createDateTime(createDateTime)
                        .hasBeenFetched(false)
                        .build();
                thread.setThreadIdFromURL();
                threadList.add(thread);
            } catch (Exception exception) {
            }
        }
        return threadList;
    }

    public List<String> extractPageUrls(String url) {
        Document doc = readPage.readPage(url).orElseThrow(() -> new FailedToReadPageException("Serwice was unable to read page!"));
        List<String> pageLinkList = new ArrayList<>(1);
        Elements paginationForum = doc.select(".paginationForum ");
        for (Element element : paginationForum.select(".numerals .outerCenter .innerCenter .numeral")) {
            try {
                String threadLink = element.select("a").get(0).absUrl("href");
                pageLinkList.add(threadLink);
            } catch (Exception exception) {
            }
        }
        return pageLinkList;
    }
}
