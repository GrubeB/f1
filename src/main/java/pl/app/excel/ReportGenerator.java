package pl.app.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReportGenerator {
    private final StyleGenerator styleGenerator;
    public Report createNewReport(ReportType type) {
        Workbook workbook = switch (type){
            case XLSX: yield new XSSFWorkbook();
            case XLS:
            default: yield new HSSFWorkbook();
        };
        return new Report(workbook, styleGenerator.cellStyleMap(workbook));
    }
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

