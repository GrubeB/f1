package pl.app.thread.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.app.report.domain.ReportType;

import java.io.IOException;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadReportDto {
    @JsonProperty("from")
    private LocalDate from;
    @JsonProperty("to")
    private LocalDate to;
}
