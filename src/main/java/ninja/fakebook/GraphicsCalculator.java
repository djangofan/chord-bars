package ninja.fakebook;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GraphicsCalculator {

    private final Graphics2D graphics;
    private final Dimension dimension;
    private final int scale;
    private static final double PADDING_ADJUST = 0.86;
    private static final Rectangle2D INVALID_RECTANGLE = new Rectangle2D.Double(10, 10, 10, 10);
    private Font font;

    GraphicsCalculator(Graphics2D graphics, Dimension dimension, Font font) {
        this.graphics = graphics;
        this.dimension = dimension;
        this.scale = this.dimension.height/BarGenerator.HEIGHT_INCREMENT;
        this.font = font;
    }

    public Dimension getDimensions() {
        return this.dimension;
    }

    public Graphics2D getGraphics() {
        return this.graphics;
    }

    public Point getCenteredTextPositionOnCanvas(String text) {
        FontMetrics fm = this.graphics.getFontMetrics();
        int x = (dimension.width - fm.stringWidth(text)) / 2;
        int y = (fm.getAscent() + (dimension.height - (fm.getAscent() + fm.getDescent())) / 2);
        return new Point(x, y);
    }

    public FittedFont getFittedTextOnBarQuarter(String text, QuarterSection quarterSection) {
        int trix[] = { quarterSection.getLeft().x, quarterSection.getRight().x, quarterSection.getTop().x };
        int triy[] = { quarterSection.getLeft().y, quarterSection.getRight().y, quarterSection.getTop().y };
        Polygon triangle = new Polygon(trix, triy, 3);
        Rectangle2D rectangle = INVALID_RECTANGLE;
        int fontMaxSize = 72;
        int fontMinSize = 6;
        FontMetrics metrics = null;
        int currentFontSize = fontMaxSize;
        while (currentFontSize >= fontMinSize && !triangle.contains(rectangle)) {
            font = new Font(font.getFontName(), font.getStyle(), currentFontSize);
            metrics = createFontMetrics(font);
            int fontHeight = metrics.getHeight();
            int stringWidth = metrics.stringWidth(text) + scale;
            int outlineYPosition = quarterSection.getTop().y - fontHeight/2;
            switch (quarterSection.getPart()) {
                case ONE:
                    rectangle = getFontBounds(scale, outlineYPosition , stringWidth, fontHeight);
                    break;
                case TWO:
                    rectangle = getFontBounds(quarterSection.getTop().x -(stringWidth/2)+1, 1, stringWidth, fontHeight);
                    break;
                case THREE:
                    rectangle = getFontBounds(quarterSection.getTop().x -(stringWidth/2)+1, quarterSection.getLeft().y-fontHeight-(scale*2), stringWidth, fontHeight);
                    break;
                case FOUR:
                    rectangle = getFontBounds(quarterSection.getTop().x + (int)(fontHeight*PADDING_ADJUST), outlineYPosition , stringWidth, fontHeight);
                    break;
                default:
                    rectangle = INVALID_RECTANGLE;
            }
            currentFontSize--;
        }
        Objects.requireNonNull(metrics);
        //graphics.drawRect((int)rectangle.getX(), (int)rectangle.getY(), metrics.stringWidth(text), metrics.getHeight());
        Point fontStartPoint = getFontLocationFromBounds(rectangle, metrics);
        return new FittedFont(font, fontStartPoint, metrics);
    }

    public FittedFont getFittedTextOnBarHalf(String text, HalfSection halfSection) {
        int trix[] = { halfSection.getLeft().x, halfSection.getRight().x, halfSection.getTop().x };
        int triy[] = { halfSection.getLeft().y, halfSection.getRight().y, halfSection.getTop().y };
        Polygon triangle = new Polygon(trix, triy, 3);
        Rectangle2D rectangle = INVALID_RECTANGLE;
        int fontMaxSize = 72;
        int fontMinSize = 6;
        FontMetrics metrics = null;
        int currentFontSize = fontMaxSize;
        while (currentFontSize >= fontMinSize && !triangle.contains(rectangle)) {
            font = new Font(font.getFontName(), font.getStyle(), currentFontSize);
            metrics = createFontMetrics(font);
            int fontHeight = metrics.getHeight();
            int stringWidth = metrics.stringWidth(text) + scale;
            int outlineYPosition = halfSection.getTextYPosition(scale) + fontHeight; // font y-position starts at bottom of square
            switch (halfSection.getPart()) {
                case ONE:
                    System.out.println("1s" + scale + "y" + outlineYPosition + "w" + stringWidth + "h" + fontHeight);
                    rectangle = getFontBounds(scale, outlineYPosition, stringWidth, fontHeight);
                    break;
                case TWO:
                    System.out.println("2s" + scale + "y" + outlineYPosition + "w" + stringWidth + "h" + fontHeight);
                    rectangle = getFontBounds(halfSection.getTextMaxXPosition(scale) - (stringWidth + scale), outlineYPosition, stringWidth, fontHeight);
                    break;
                default:
                    rectangle = INVALID_RECTANGLE;
            }
            currentFontSize--;
        }
        Objects.requireNonNull(metrics);
        graphics.drawRect((int)rectangle.getX(), (int)rectangle.getY(), metrics.stringWidth(text), metrics.getHeight());
        Point fontStartPoint = getFontLocationFromBounds(rectangle, metrics);
        return new FittedFont(font, fontStartPoint, metrics);
    }

    private Rectangle2D getFontBounds(int x, int y, int width, int height) {
        return new Rectangle2D.Double(x, y, width, height);
    }

    private Point getFontLocationFromBounds(Rectangle2D rectangle2D, FontMetrics fontMetrics) {
        return new Point(rectangle2D.getBounds().x, rectangle2D.getBounds().y + (int)(fontMetrics.getHeight()*PADDING_ADJUST));
    }

    public QuarterSection getSectionOneQuarterTriangle() {
        Point triLeft = new Point(0, 0);
        Point triRight = new Point(0, this.dimension.height);
        Point triTop = new Point(this.dimension.width/2, this.dimension.height/2);
        return new QuarterSection(Part.ONE, triLeft, triRight, triTop);
    }

    public QuarterSection getSectionTwoQuarterTriangle() {
        Point triLeft = new Point(this.dimension.width, 0);
        Point triRight = new Point(0, 0);
        Point triTop = new Point(this.dimension.width/2, this.dimension.height/2);
        return new QuarterSection(Part.TWO, triLeft, triRight, triTop);
    }

    public QuarterSection getSectionThreeQuarterTriangle() {
        Point triLeft = new Point(0, this.dimension.height);
        Point triRight = new Point(this.dimension.width, this.dimension.height);
        Point triTop = new Point(this.dimension.width/2, this.dimension.height/2);
        return new QuarterSection(Part.THREE, triLeft, triRight, triTop);
    }

    public QuarterSection getSectionFourQuarterTriangle() {
        Point triLeft = new Point(this.dimension.width, this.dimension.height);
        Point triRight = new Point(this.dimension.width, 0);
        Point triTop = new Point(this.dimension.width/2, this.dimension.height/2);
        return new QuarterSection(Part.FOUR, triLeft, triRight, triTop);
    }

    public HalfSection getLeftHalfTriangle() {
        Point triTop = new Point(0, 0);
        Point triLeft = new Point(0, this.dimension.height);
        Point triRight = new Point(this.dimension.width, 0);
        return new HalfSection(Part.ONE, triLeft, triRight, triTop);
    }

    public HalfSection getRightHalfTriangle() {
        Point triTop = new Point(this.dimension.width, this.dimension.height);
        Point triLeft = new Point(this.dimension.width, 0);
        Point triRight = new Point(0, this.dimension.height);
        return new HalfSection(Part.TWO, triLeft, triRight, triTop);
    }

    /**
     * Create FontMetrics without needed access to display hardware.
     * @param font contains font size, style, and type
     * @return FontMetrics instance
     */
    private static FontMetrics createFontMetrics(Font font) {
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        FontMetrics metrics = g.getFontMetrics(font);
        g.dispose();
        bi = null;
        return metrics;
    }


}
