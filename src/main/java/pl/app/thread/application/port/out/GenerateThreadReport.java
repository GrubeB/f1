package pl.app.thread.application.port.out;

import pl.app.report.ReportType;
import pl.app.thread.domain.Thread;

import java.io.IOException;
import java.util.List;

public interface GenerateThreadReport {
    byte[] generate(List<Thread> threadList, ReportType type) throws IOException;
}
