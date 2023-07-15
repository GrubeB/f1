package pl.app.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    XLS("xls"),
    XLSX("xlsx");
    private final String extension;
}