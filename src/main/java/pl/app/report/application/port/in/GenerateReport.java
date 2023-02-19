package pl.app.report.application.port.in;

import pl.app.report.domain.Report;

import java.io.IOException;

public interface GenerateReport {
    byte[] generateReport(Report report) throws IOException;
}
