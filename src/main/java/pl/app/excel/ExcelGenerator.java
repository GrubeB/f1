package pl.app.excel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelGenerator {
    private final StyleGenerator styleGenerator;

    public byte[] generateXlsxReport()  throws IOException {
        Workbook workbook = new XSSFWorkbook();
        return generateReport(workbook);
    }

    public byte[] generateXlsReport()  throws IOException {
        Workbook workbook = new HSSFWorkbook();
        return generateReport(workbook);
    }

    private byte[] generateReport(Workbook workbook) throws IOException {
        Map<CellStyleType, CellStyle> styles = styleGenerator.cellStyleMap(workbook);
        Sheet sheet = workbook.createSheet();
        setColumnsWidth(sheet);
        createHeaderRow(sheet, styles);
        Sheet sheet2 = workbook.createSheet();
        setColumnsWidth(sheet2);
        createHeaderRow(sheet2, styles);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);

        byteArrayOutputStream.close();
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void createHeaderRow(Sheet sheet, Map<CellStyleType, CellStyle> styles) {
        Row row = sheet.createRow(0);

        for (int columnNumber = 0; columnNumber < 20; columnNumber++) {
            Cell cell = row.createCell(columnNumber);

            cell.setCellValue("Column" + columnNumber);
            cell.setCellStyle(styles.get(CellStyleType.GREY_CENTERED_BOLD_ARIAL_WITH_BORDER));
        }
    }

    private void setColumnsWidth(Sheet sheet) {
        for (int columnNumber = 0; columnNumber < 20; columnNumber++) {
            sheet.setColumnWidth(columnNumber, 256 * 20);
        }
    }
}

