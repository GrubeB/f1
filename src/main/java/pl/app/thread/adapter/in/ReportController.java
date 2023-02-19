package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.excel.ReportType;
import pl.app.thread.application.port.out.GenerateThreadReport;
import pl.app.thread.application.port.out.persistance.FetchAllPort;

import java.io.IOException;

@RestController
@RequestMapping("/api/threads/reports")
@RequiredArgsConstructor
public class ReportController {

    private final GenerateThreadReport generateThreadReport;
    private final FetchAllPort fetchAllPort;

    @GetMapping("/xlsx")
    private ResponseEntity<byte[]> generateXlsxReport() throws IOException {
        byte[] report = generateThreadReport.generate(fetchAllPort.fetchAll(), ReportType.XLSX);
        return createResponseEntity(report, "report.xlsx");
    }

    @GetMapping("/xls")
    private ResponseEntity<byte[]> generateXlsReport() throws IOException {
        byte[] report = generateThreadReport.generate(fetchAllPort.fetchAll(), ReportType.XLS);
        return createResponseEntity(report, "report.xls");
    }

    private ResponseEntity<byte[]> createResponseEntity(byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(report);

    }

}
