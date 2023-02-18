package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.thread.application.port.in.DelegateThreadListToFetchToKafka;
import pl.app.thread.application.port.in.DelegateThreadToFetchToKafka;
import pl.app.thread.application.port.in.DelegateThreadWithListToFetchToKafka;
import pl.app.thread.application.port.in.dto.ExtendThreadListToFetchDto;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;

@RestController
@RequestMapping("/api/delegate-fetch-threads")
@RequiredArgsConstructor
public class DelegateThreadToFetchController {
    private final DelegateThreadListToFetchToKafka delegateThreadListToFetchToKafka;
    private final DelegateThreadToFetchToKafka delegateThreadToFetchToKafka;
    private final DelegateThreadWithListToFetchToKafka delegateThreadWithListToFetchToKafka;

    @PostMapping("/thread-list")
    private ResponseEntity<?> delegateThreadListToFetchToKafka(@RequestBody ThreadListToFetchMessage message) {
        delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/extend-thread-list")
    private ResponseEntity<?> delegateThreadListToFetchToKafka(@RequestBody ExtendThreadListToFetchDto dto) {
        dto.getUrlList().forEach(url ->
                delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(new ThreadListToFetchMessage(url,dto.getIndustryName()))
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/thread")
    private ResponseEntity<?> delegateThreadToFetchToKafka(@RequestBody ThreadToFetchMessage message) {
        delegateThreadToFetchToKafka.delegateThreadToFetchToKafka(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/thread-with-list")
    private ResponseEntity<?> delegateThreadWithListToFetchToKafka(@RequestBody ThreadWithListToFetchMessage message) {
        delegateThreadWithListToFetchToKafka.delegateThreadWithListToFetchToKafka(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
