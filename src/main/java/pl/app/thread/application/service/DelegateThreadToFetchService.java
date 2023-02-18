package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.app.thread.application.port.in.DelegateThreadListToFetchToKafka;
import pl.app.thread.application.port.in.DelegateThreadToFetchToKafka;
import pl.app.thread.application.port.in.DelegateThreadWithListToFetchToKafka;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;
import pl.app.thread.application.port.out.DelegateThreadListToFetchKafka;
import pl.app.thread.application.port.out.DelegateThreadToFetchKafka;
import pl.app.thread.application.port.out.DelegateThreadWithListToFetchKafka;

@Service
@RequiredArgsConstructor
class DelegateThreadToFetchService implements
        DelegateThreadListToFetchToKafka,
        DelegateThreadToFetchToKafka,
        DelegateThreadWithListToFetchToKafka {
    private final Logger logger = LoggerFactory.getLogger(DelegateThreadToFetchService.class);
    private final DelegateThreadListToFetchKafka delegateThreadListToFetchKafka;
    private final DelegateThreadToFetchKafka delegateThreadToFetchKafka;
    private final DelegateThreadWithListToFetchKafka delegateThreadWithListToFetchKafka;

    @Override
    public void delegateThreadListToFetchToKafka(ThreadListToFetchMessage message) {
        delegateThreadListToFetchKafka.sendToKafka(message);
    }

    @Override
    public void delegateThreadToFetchToKafka(ThreadToFetchMessage message) {
        delegateThreadToFetchKafka.sendToKafka(message);
    }

    @Override
    public void delegateThreadWithListToFetchToKafka(ThreadWithListToFetchMessage message) {
        delegateThreadWithListToFetchKafka.sendToKafka(message);
    }
}
