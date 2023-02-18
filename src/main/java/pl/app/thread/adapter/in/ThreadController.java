package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.thread.application.port.out.persistance.FetchAllPort;
import pl.app.thread.application.port.out.persistance.FetchByIdPort;
import pl.app.thread.domain.Thread;

import java.util.List;

@RestController
@RequestMapping("/api/threads")
@RequiredArgsConstructor
public class ThreadController {
    private final FetchByIdPort fetchByIdPort;
    private final FetchAllPort fetchAllPort;

    @GetMapping
    private ResponseEntity<List<Thread>> fetchAll() {
        List<Thread> threadList = fetchAllPort.fetchAll();
        return ResponseEntity.ok(threadList);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Thread> fetchById(@PathVariable("id") Long id) {
        Thread thread = fetchByIdPort.fetchById(id);
        return ResponseEntity.ok(thread);
    }
}
