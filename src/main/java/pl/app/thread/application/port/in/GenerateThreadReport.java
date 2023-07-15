package pl.app.thread.application.port.in;

import pl.app.report.domain.ReportType;

import java.io.IOException;
import java.time.LocalDate;

public interface GenerateThreadReport {
    byte[] generate(ReportType type, LocalDate from, LocalDate to) throws IOException;
    byte[] generateZip(ReportType type) throws IOException;
}
