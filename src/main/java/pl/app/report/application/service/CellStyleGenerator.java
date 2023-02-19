package pl.app.report.application.service;

import org.apache.poi.ss.usermodel.*;
import pl.app.report.domain.CellStyleType;

import java.util.Arrays;
import java.util.function.Predicate;

public class CellStyleGenerator {
    public static CellStyle generateCellStyle(Workbook workbook, CellStyleType... styleType) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        for (CellStyleType style : Arrays.stream(styleType).filter(CellStyleType::isFontStyle).toList()) {
            switch (style) {
                case FONT10 -> font.setFontHeight((short) (20 * 10));
                case FONT12 -> font.setFontHeight((short) (20 * 12));
                case FONT14 -> font.setFontHeight((short) (20 * 14));
                case FONT16 -> font.setFontHeight((short) (20 * 16));
                case FONT18 -> font.setFontHeight((short) (20 * 18));
                case FONT20 -> font.setFontHeight((short) (20 * 20));
                case BOLD -> font.setBold(true);
                case ARIAL -> font.setFontName("Arial");
                case RED -> font.setColor(IndexedColors.RED.index);
            }
        }
        cellStyle.setFont(font);
        for (CellStyleType style : Arrays.stream(styleType).filter(Predicate.not(CellStyleType::isFontStyle)).toList()) {
            switch (style) {
                case RIGHT_ALIGNMENT -> cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                case CENTER_ALIGNMENT -> cellStyle.setAlignment(HorizontalAlignment.CENTER);
                case BORDER -> CellStyleGenerator.setBorder(cellStyle);
                case BACKGROUND_PRIMARY_LIGHT -> {
                    cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }
                case BACKGROUND_PRIMARY -> {
                    cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }
                case BACKGROUND_PRIMARY_DARK -> {
                    cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }
            }
        }

        return cellStyle;
    }

    private static void setBorder(CellStyle cellStyle) {
        final BorderStyle thin = BorderStyle.THIN;
        final short blackColorIndex = IndexedColors.BLACK.getIndex();
        cellStyle.setBorderRight(thin);
        cellStyle.setBorderLeft(thin);
        cellStyle.setBorderTop(thin);
        cellStyle.setBorderBottom(thin);
        cellStyle.setRightBorderColor(blackColorIndex);
        cellStyle.setLeftBorderColor(blackColorIndex);
        cellStyle.setTopBorderColor(blackColorIndex);
        cellStyle.setBottomBorderColor(blackColorIndex);
    }
}

