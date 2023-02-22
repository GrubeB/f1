package pl.app.report.domain;

public enum CellStyleType {

    FONT10(true),
    FONT12(true),
    FONT14(true),
    FONT16(true),
    FONT18(true),
    FONT20(true),
    BOLD(true),
    ARIAL(true),
    RED(true),
    RIGHT_ALIGNMENT(false),
    BACKGROUND_PRIMARY_LIGHT(false),
    BACKGROUND_PRIMARY(false),
    BACKGROUND_PRIMARY_DARK(false),
    CENTER_ALIGNMENT(false),
    BORDER(false);

    private final boolean isFontStyle;

    CellStyleType(boolean isFontStyle) {
        this.isFontStyle = isFontStyle;
    }

    public boolean isFontStyle() {
        return isFontStyle;
    }
}
