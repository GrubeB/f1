package pl.app.excel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StyleGenerator {

    public Map<CellStyleType, CellStyle> cellStyleMap(Workbook workbook) {
        Font boldArial = createBoldArialFont(workbook);
        Font redBoldArial = createRedBoldArialFont(workbook);

        CellStyle rightAlignedStyle = createRightAlignedStyle(workbook);
        CellStyle greyCenteredBoldArialWithBorderStyle = createGreyCenteredBoldArialWithBorderStyle(workbook);
        CellStyle redBoldArialWithBorderStyle = createRedBoldArialWithBorderStyle(workbook);
        CellStyle rightAlignedDateFormatStyle = createRightAlignedDateFormatStyle(workbook);

        return Map.of(
                CellStyleType.RIGHT_ALIGNED, rightAlignedStyle,
                CellStyleType.GREY_CENTERED_BOLD_ARIAL_WITH_BORDER, greyCenteredBoldArialWithBorderStyle,
                CellStyleType.RED_BOLD_ARIAL_WITH_BORDER, redBoldArialWithBorderStyle,
                CellStyleType.RIGHT_ALIGNED_DATE_FORMAT, rightAlignedDateFormatStyle
        );
    }

    private CellStyle createRightAlignedDateFormatStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setDataFormat((short) 14);
        return cellStyle;
    }

    private CellStyle createRedBoldArialWithBorderStyle(Workbook workbook) {
        CellStyle cellStyle = this.createBorderedStyle(workbook);
        cellStyle.setFont(createRedBoldArialFont(workbook));
        return cellStyle;
    }

    private CellStyle createGreyCenteredBoldArialWithBorderStyle(Workbook workbook) {
        CellStyle cellStyle = this.createBorderedStyle(workbook);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(createBoldArialFont(workbook));
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private CellStyle createBorderedStyle(Workbook workbook) {
        final BorderStyle thin = BorderStyle.THIN;
        final short blackColorIndex = IndexedColors.BLACK.getIndex();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderRight(thin);
        cellStyle.setBorderLeft(thin);
        cellStyle.setBorderTop(thin);
        cellStyle.setBorderBottom(thin);
        cellStyle.setRightBorderColor(blackColorIndex);
        cellStyle.setLeftBorderColor(blackColorIndex);
        cellStyle.setTopBorderColor(blackColorIndex);
        cellStyle.setBottomBorderColor(blackColorIndex);
        return cellStyle;
    }

    private CellStyle createRightAlignedStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return cellStyle;
    }
    private Font createBoldArialFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        return font;
    }

    private Font createRedBoldArialFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setColor(IndexedColors.RED.index);
        return font;
    }
}

