package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.excel.ExcelGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/threads/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ExcelGenerator excelGenerator;

    @PostMapping("/xlsx")
    private ResponseEntity<byte[]> generateXlsxReport() throws IOException {
        byte[] report = excelGenerator.generateXlsxReport();
        return createResponseEntity(report, "report.xlsx");
    }


    @PostMapping("/xls")
    private ResponseEntity<byte[]> generateXlsReport() throws IOException {
        byte[] report = excelGenerator.generateXlsReport();
//        Report newXlsReport = excelGenerator.createNewXlsReport();
//        newXlsReport.writeRow(0,0,0, Arrays.asList("Buenos Aires", "Córdoba", "La Plata"), Collections.emptyList());
//        byte[] bytes = excelGenerator.generateXlsxReport(newXlsReport);
        return createResponseEntity(report, "report.xls");
    }

    private ResponseEntity<byte[]> createResponseEntity(byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(report);

    }

}
