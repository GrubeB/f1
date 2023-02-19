package pl.app.report.application.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pl.app.report.application.port.in.CreateNewReport;
import pl.app.report.application.port.in.GenerateReport;
import pl.app.report.domain.Report;
import pl.app.report.domain.ReportType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
class ReportService implements
        CreateNewReport,
        GenerateReport {
    @Override
    public Report createNewReport(ReportType type) {
        Workbook workbook = switch (type) {
            case XLSX:
                yield new XSSFWorkbook();
            case XLS:
            default:
                yield new HSSFWorkbook();
        };
        return new Report(workbook);
    }

    @Override
    public byte[] generateReport(Report report) throws IOException {
        return generateReport(report.getWorkbook());
    }

    private byte[] generateReport(Workbook workbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byteArrayOutputStream.close();
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }
}

