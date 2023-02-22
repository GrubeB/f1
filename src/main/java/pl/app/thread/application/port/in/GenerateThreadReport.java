package pl.app.thread.application.port.in;

import pl.app.report.domain.ReportType;
import pl.app.thread.domain.Thread;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface GenerateThreadReport {
    byte[] generate(ReportType type, LocalDate from, LocalDate to) throws IOException;
}
