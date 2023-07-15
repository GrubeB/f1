package pl.app.report.domain;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import pl.app.report.application.service.CellStyleGenerator;

import java.util.List;

@Getter
public class Report {
    private final Workbook workbook;

    public Report(Workbook workbook) {
        this.workbook = workbook;
        createAnotherSheet();
    }

    public void createAnotherSheet() {
        workbook.createSheet();
    }

    public void writeRow(int sheetIndex, int rowIndex, int cellIndex, List<String> data) {
        writeRow(sheetIndex, rowIndex, cellIndex, data, workbook.createCellStyle());
    }

    public void writeRow(int sheetIndex, int rowIndex, int cellIndex, List<String> data, CellStyleType... styles) {
        CellStyle cellStyle = CellStyleGenerator.generateCellStyle(workbook, styles);
        writeRow(sheetIndex, rowIndex, cellIndex, data, cellStyle);
    }

    public void writeRow(int sheetIndex, int rowIndex, int cellIndex, List<String> data, CellStyle cellStyle) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Row row = sheet.createRow(rowIndex);
        for (int columnNumber = cellIndex; columnNumber < (cellIndex + data.size()); columnNumber++) {
            Cell cell = row.createCell(columnNumber);
            if(data.get(columnNumber - cellIndex).length()<32767){
                cell.setCellValue(data.get(columnNumber - cellIndex));
            }else {
                cell.setCellValue(data.get(columnNumber - cellIndex).substring(0,32766));
            }
            cell.setCellStyle(cellStyle);
        }
    }

    public void setColumnsWidth(int sheetIndex, int cellIndex, int numberOfCell, int size) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        for (int columnNumber = cellIndex; columnNumber < (cellIndex + numberOfCell); columnNumber++) {
            sheet.setColumnWidth(columnNumber, size);
        }
    }

    public void setRowsHeight(int sheetIndex, int size) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        sheet.setDefaultRowHeight((short) size);
    }

    public CellStyle generateCellStyle(CellStyleType... styleType) {
        return CellStyleGenerator.generateCellStyle(workbook, styleType);
    }
}