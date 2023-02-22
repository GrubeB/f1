package pl.app.thread.application.port.out;

import org.jsoup.nodes.Document;

import java.util.Optional;

public interface ReadPage {
    Optional<Document> readPage(String url);
}
