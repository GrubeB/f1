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
import pl.app.thread.application.port.in.dto.*;

@RestController
@RequestMapping("/api/delegate-fetch-threads")
@RequiredArgsConstructor
class DelegateThreadToFetchController {
    private final DelegateThreadListToFetchToKafka delegateThreadListToFetchToKafka;
    private final DelegateThreadToFetchToKafka delegateThreadToFetchToKafka;
    private final DelegateThreadWithListToFetchToKafka delegateThreadWithListToFetchToKafka;

    @PostMapping("/thread-list")
    private ResponseEntity<?> delegateThreadListToFetchToKafka(@RequestBody ThreadListToFetchMessage message) {
        delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/thread-list/url-list")
    private ResponseEntity<?> delegateThreadListToFetchToKafka(@RequestBody ThreadListToFetchUrlListDto dto) {
        dto.getUrlList().forEach(url -> delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(new ThreadListToFetchMessage(url, dto.getIndustryName())));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/thread-list/number-of-urls")
    private ResponseEntity<?> delegateThreadListToFetchToKafka(@RequestBody ThreadListToFetchNumberOfUrlsDto dto) {
        String url = dto.getUrl();
        String[] split = url.split(".html", 2);
        String nextUrl;
        for (int i = dto.getStartUrl()+1; i <dto.getNumberOfUrls()+dto.getStartUrl()+1; i++) {
            nextUrl = split[0].concat(","+i).concat(".html");
            delegateThreadListToFetchToKafka.delegateThreadListToFetchToKafka(new ThreadListToFetchMessage(nextUrl, dto.getIndustryName()));
        }
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
