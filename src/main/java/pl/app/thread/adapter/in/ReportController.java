package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.report.domain.ReportType;
import pl.app.thread.application.port.in.GenerateThreadReport;
import pl.app.thread.application.port.in.dto.ThreadReportDto;

import java.io.IOException;

@RestController
@RequestMapping("/api/threads/reports")
@RequiredArgsConstructor
class ReportController {
    private final GenerateThreadReport generateThreadReport;

    @GetMapping("/xlsx")
    private ResponseEntity<byte[]> generateXlsxReport(@RequestBody ThreadReportDto dto) throws IOException {
        byte[] report = generateThreadReport.generate(ReportType.XLSX, dto.getFrom(), dto.getTo());
        return createResponseEntity(report, "report.xlsx");
    }

    @GetMapping("/xls")
    private ResponseEntity<byte[]> generateXlsReport(@RequestBody ThreadReportDto dto) throws IOException {
        byte[] report = generateThreadReport.generate(ReportType.XLS, dto.getFrom(), dto.getTo());
        return createResponseEntity(report, "report.xls");
    }

    private ResponseEntity<byte[]> createResponseEntity(byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(report);

    }

}
