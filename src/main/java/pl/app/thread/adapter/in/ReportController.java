package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.app.excel.CellStyleType;
import pl.app.excel.ReportGenerator;
import pl.app.excel.Report;
import pl.app.excel.ReportType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/threads/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportGenerator reportGenerator;

//    @PostMapping("/xlsx")
//    private ResponseEntity<byte[]> generateXlsxReport() throws IOException {
//        byte[] report = excelGenerator.generateXlsxReport();
//        return createResponseEntity(report, "report.xlsx");
//    }


    @PostMapping("/xls")
    private ResponseEntity<byte[]> generateXlsReport() throws IOException {
        //byte[] report = excelGenerator.generateXlsReport();
        Report report = reportGenerator.createNewReport(ReportType.XLS);
        report.writeRow(0,0,0, Arrays.asList("Buenos Aires", "Córdoba", "La Plata"), CellStyleType.DEFAULT);
        report.writeRow(0,1,0, Arrays.asList("2Buenos Aires", "Córdoba", "La Plata"), CellStyleType.RIGHT_ALIGNED);
        report.writeRow(0,2,0, Arrays.asList("2Buenos Aires", "Córdoba", "La Plata"), CellStyleType.GREY_CENTERED_BOLD_ARIAL_WITH_BORDER);
        report.writeRow(0,3,0, Arrays.asList("2Buenos Aires", "Córdoba", "La Plata"), CellStyleType.RED_BOLD_ARIAL_WITH_BORDER);
        report.writeRow(0,4,0, Arrays.asList("2Buenos Aires", "Córdoba", "La Plata"), CellStyleType.RIGHT_ALIGNED_DATE_FORMAT);
        byte[] bytes = reportGenerator.generateReport(report);
        return createResponseEntity(bytes, "report.xls");
    }

    private ResponseEntity<byte[]> createResponseEntity(byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(report);

    }

}
