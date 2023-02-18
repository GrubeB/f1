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
class DelegateThreadToFetchService implements
        DelegateThreadListToFetchToKafka,
        DelegateThreadToFetchToKafka,
        DelegateThreadWithListToFetchToKafka
{
    private final Logger logger = LoggerFactory.getLogger(DelegateThreadToFetchService.class);
    private final DelegateThreadListToFetchKafka delegateThreadListToFetchKafka;
    private final DelegateThreadToFetchKafka delegateThreadToFetchKafka;
    private final DelegateThreadWithListToFetchKafka delegateThreadWithListToFetchKafka;

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
}
