package pl.app.thread.application.port.in;

import pl.app.thread.domain.Thread;

public interface FetchThread {
    Thread fetchThread(String url);
}
