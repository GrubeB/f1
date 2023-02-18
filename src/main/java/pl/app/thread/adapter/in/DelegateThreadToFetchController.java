package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.thread.application.port.in.DelegateThreadListToFetchToKafka;
import pl.app.thread.application.port.in.DelegateThreadWithListToFetchToKafka;

@RestController
@RequestMapping("/api/delegate-fetch-threads")
@RequiredArgsConstructor
public class DelegateThreadToFetchController {
    private final DelegateThreadListToFetchToKafka delegateThreadListToFetchToKafka;
    private final DelegateThreadWithListToFetchToKafka delegateThreadWithListToFetchToKafka;

    @PostMapping("/thread-list")
    private ResponseEntity<?> delegateThreadListToFetchToKafka(@RequestBody String link) {
        delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(link);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @PostMapping("/thread")
//    private ResponseEntity<?> delegateThreadToFetchToKafka(@RequestBody String link) {
//        threadService.delegateThreadToFetchToKafka(link,-1L);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @PostMapping("/thread-with-list")
    private ResponseEntity<?> delegateThreadWithListToFetchToKafka(@RequestBody String link) {
        delegateThreadWithListToFetchToKafka.delegateThreadWithListToFetchToKafka(link);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
