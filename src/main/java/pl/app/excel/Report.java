package pl.app.excel;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Map;

@Getter
public class Report {
    private final Workbook workbook;
    private final Map<CellStyleType, CellStyle> cellStyleMap;

    public Report(Workbook workbook, Map<CellStyleType, CellStyle> cellStyleMap) {
        this.workbook = workbook;
        this.cellStyleMap = cellStyleMap;
        createAnotherSheet();
    }

    public void createAnotherSheet() {
        workbook.createSheet();
    }

    public void writeRow(int sheetIndex, int rowIndex, int cellIndex, List<String> data){
        writeRow(sheetIndex,rowIndex,cellIndex,data, CellStyleType.DEFAULT);
    }
    public void writeRow(int sheetIndex, int rowIndex, int cellIndex, List<String> data, CellStyleType styleType) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        CellStyle cellStyle = cellStyleMap.get(styleType);
        Row row = sheet.createRow(rowIndex);
        for (int columnNumber = cellIndex; columnNumber < (cellIndex + data.size()); columnNumber++) {
            Cell cell = row.createCell(columnNumber);
            cell.setCellValue(data.get(columnNumber - cellIndex));
            cell.setCellStyle(cellStyle);
        }
    }

    public void setColumnsWidth(int sheetIndex, int cellIndex, int numberOfCell, int size) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        for (int columnNumber = cellIndex; columnNumber < (cellIndex + numberOfCell); columnNumber++) {
            sheet.setColumnWidth(columnNumber, size);
        }
    }
}