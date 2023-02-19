package pl.app.report.application.port.in;

import pl.app.report.domain.Report;
import pl.app.report.domain.ReportType;

public interface CreateNewReport {
    Report createNewReport(ReportType type);
}
