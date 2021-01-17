package ninja.fakebook;

import java.awt.*;

public class FittedFont {

    public final String halfDiminished = "\u00f8";
    public final String major = "\u0394";

    private final Font font;
    private final Point coordinates;
    private final FontMetrics fontMetrics;

    FittedFont(Font font, Point coordinates, FontMetrics fontMetrics) {
        this.font = font;
        this.coordinates = coordinates;
        this.fontMetrics = fontMetrics;
    }

    public Font getFont() {
        return this.font;
    }

    public Point getCoordinates() {
        return this.coordinates;
    }

    public int getX() {
        return getCoordinates().x;
    }

    public int getY() {
        return this.coordinates.y;
    }

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

}
