package ninja.fakebook;

public enum LineWidth {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE;

    public static LineWidth getWidth(int scale) {
        switch(scale) {
            case 3:
            case 4:
            case 5:
                return TWO;
            case 6:
            case 7:
                return THREE;
            case 8:
                return FOUR;
            case 9:
                return FIVE;
            default:
                return ONE;
        }
    }

    public static int getAdjust(int scale) {
        switch(scale) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return ONE.ordinal();
            case 8:
            case 9:
                return TWO.ordinal();
            default:
                return ZERO.ordinal();
        }
    }

}
