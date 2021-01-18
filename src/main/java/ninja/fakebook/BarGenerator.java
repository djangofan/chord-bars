package ninja.fakebook;

import org.json.JSONArray;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BarGenerator {

    public static final int HEIGHT_INCREMENT = 40;
    public static final int WIDTH_INCREMENT = 40;
    public static final String IMAGE_TYPE = "png";
    public static final String IMAGE_NAME = "bar";

    private final BufferedImage bufferedImage;
    private final Graphics2D g2d;
    private final int scale;
    private final LineWidth lineWidth;
    private final BarText barText;
    private final GraphicsCalculator graphicsCalculator;
    private final Font defaultFont;

    BarGenerator(int desiredScale, String text, Font font) {
        this.defaultFont = font;
        this.scale = desiredScale;
        this.lineWidth = LineWidth.getWidth(this.scale);
        if (this.scale < 1 || this.scale > 5) {
            throw new IllegalArgumentException("Bar scale must be a value between 1 and 5");
        }
        Dimension imageDimensions = new Dimension(this.scale* WIDTH_INCREMENT, this.scale* HEIGHT_INCREMENT);
        bufferedImage = new BufferedImage(imageDimensions.width, imageDimensions.height, BufferedImage.TYPE_INT_ARGB);
        g2d = bufferedImage.createGraphics();
        this.graphicsCalculator = new GraphicsCalculator(g2d, imageDimensions, font);
        this.setLineWidth();
        JSONArray jsonArray = new JSONArray(text);
        this.barText = new BarText(jsonArray);
    }

    public void drawBar(DividedBy style) {
        useAntialiasing();
        switch (barText.getPartCount()) {
            case 4:
                if (style.name().equals(DividedBy.QUARTERS_HALF.name())) {
                  // part 1
                  QuarterSection triangleR = graphicsCalculator.getSectionOneQuarterTriangle();
                  FittedFont fittedFontR = graphicsCalculator.getFittedTextOnBarQuarter(barText.getPartOne(), triangleR);
                  setFont(fittedFontR.getFont());
                  this.g2d.drawString(barText.getPartOne(), fittedFontR.getX(), fittedFontR.getY());
                  // part 2
                  QuarterSection triangleD = graphicsCalculator.getSectionTwoQuarterTriangle();
                  FittedFont fittedFontD = graphicsCalculator.getFittedTextOnBarQuarter(barText.getSecondPart(), triangleD);
                  setFont(fittedFontD.getFont());
                  this.g2d.drawString(barText.getSecondPart(), fittedFontD.getX(), fittedFontD.getY());
                  // part 2nd half
                  String secondHalfText = barText.getThirdPart().isBlank() ? barText.getFourthPart() : barText.getThirdPart();
                  HalfSection triangleRight = graphicsCalculator.getRightHalfTriangle();
                  FittedFont fittedFont2 = graphicsCalculator.getFittedTextOnBarHalf(secondHalfText, triangleRight);
                  setFont(fittedFont2.getFont());
                  this.g2d.drawString(secondHalfText, fittedFont2.getX(), fittedFont2.getY());
                  break;
                } else if (style.name().equals(DividedBy.HALF_QUARTERS.name()))  {
                  // part 1st half
                  String firstHalfText = barText.getPartOne().isBlank() ? barText.getSecondPart() : barText.getPartOne();
                  HalfSection triangleLeft = graphicsCalculator.getLeftHalfTriangle();
                  FittedFont fittedFont1 = graphicsCalculator.getFittedTextOnBarHalf(firstHalfText, triangleLeft);
                  setFont(fittedFont1.getFont());
                  this.g2d.drawString(firstHalfText, fittedFont1.getX(), fittedFont1.getY());
                  // part 3
                  QuarterSection triangleU = graphicsCalculator.getSectionThreeQuarterTriangle();
                  FittedFont fittedFontU = graphicsCalculator.getFittedTextOnBarQuarter(barText.getThirdPart(), triangleU);
                  setFont(fittedFontU.getFont());
                  this.g2d.drawString(barText.getThirdPart(), fittedFontU.getX(), fittedFontU.getY());
                  // part 4
                  QuarterSection triangleL = graphicsCalculator.getSectionFourQuarterTriangle();
                  FittedFont fittedFontL = graphicsCalculator.getFittedTextOnBarQuarter(barText.getFourthPart(), triangleL);
                  setFont(fittedFontL.getFont());
                  this.g2d.drawString(barText.getFourthPart(), fittedFontL.getX(), fittedFontL.getY());
                  break;
                }
                // part 1
                QuarterSection triangleR = graphicsCalculator.getSectionOneQuarterTriangle();
                FittedFont fittedFontR = graphicsCalculator.getFittedTextOnBarQuarter(barText.getPartOne(), triangleR);
                setFont(fittedFontR.getFont());
                this.g2d.drawString(barText.getPartOne(), fittedFontR.getX(), fittedFontR.getY());
                // part 2
                QuarterSection triangleD = graphicsCalculator.getSectionTwoQuarterTriangle();
                FittedFont fittedFontD = graphicsCalculator.getFittedTextOnBarQuarter(barText.getSecondPart(), triangleD);
                setFont(fittedFontD.getFont());
                this.g2d.drawString(barText.getSecondPart(), fittedFontD.getX(), fittedFontD.getY());
                // part 3
                QuarterSection triangleU = graphicsCalculator.getSectionThreeQuarterTriangle();
                FittedFont fittedFontU = graphicsCalculator.getFittedTextOnBarQuarter(barText.getThirdPart(), triangleU);
                setFont(fittedFontU.getFont());
                this.g2d.drawString(barText.getThirdPart(), fittedFontU.getX(), fittedFontU.getY());
                // part 4
                QuarterSection triangleL = graphicsCalculator.getSectionFourQuarterTriangle();
                FittedFont fittedFontL = graphicsCalculator.getFittedTextOnBarQuarter(barText.getFourthPart(), triangleL);
                setFont(fittedFontL.getFont());
                this.g2d.drawString(barText.getFourthPart(), fittedFontL.getX(), fittedFontL.getY());
                break;
            case 2:
                // part 1
                HalfSection triangleLU = graphicsCalculator.getLeftHalfTriangle();
                FittedFont fittedFontLU = graphicsCalculator.getFittedTextOnBarHalf(barText.getPartOne(), triangleLU);
                setFont(fittedFontLU.getFont());
                this.g2d.drawString(barText.getPartOne(), fittedFontLU.getX(), fittedFontLU.getY());
                // part 2
                HalfSection triangleRL = graphicsCalculator.getRightHalfTriangle();
                FittedFont fittedFontRL = graphicsCalculator.getFittedTextOnBarHalf(barText.getSecondPart(), triangleRL);
                setFont(fittedFontRL.getFont());
                this.g2d.drawString(barText.getSecondPart(), fittedFontRL.getX(), fittedFontRL.getY());
                break;
            case 1:
            case 0:
            default:
                // draw 1 item only
        }
    }

    public void drawDividers(DividedBy style) {
        useAntialiasing();
        switch (style) {
            case DOUBLE:
                drawForwardDiagonal();
                break;
            case QUARTERS_HALF:
                drawForwardDiagonal();
                drawBackwardDiagonalTop();
                break;
            case HALF_QUARTERS:
                drawForwardDiagonal();
                drawBackwardDiagonalBottom();
                break;
            case ALL_QUARTERS:
                drawForwardDiagonal();
                drawBackwardDiagonalTop();
                drawBackwardDiagonalBottom();
                break;
            default:
        }
    }

    public void drawForwardDiagonal() {
        Dimension dimensions = this.graphicsCalculator.getDimensions();
        this.g2d.drawLine(dimensions.width, 0, 0, dimensions.height);
    }

    public void drawBackwardDiagonalTop() {
        Dimension dimensions = this.graphicsCalculator.getDimensions();
        this.g2d.drawLine(0, 0, dimensions.width/2, dimensions.height/2);
    }

    public void drawBackwardDiagonalBottom() {
        Dimension dimensions = this.graphicsCalculator.getDimensions();
        this.g2d.drawLine(dimensions.width/2, dimensions.height/2, dimensions.width, dimensions.height);
    }

    public void drawOutline() {
        Dimension dimensions = this.graphicsCalculator.getDimensions();
        int lw = this.lineWidth.ordinal();
        int adj = this.lineWidth.getAdjust(this.scale);
        g2d.drawLine(0+adj, 0, 0+adj, dimensions.height);
        g2d.drawLine(0, 0+adj, dimensions.width, 0+adj);
        g2d.drawLine(0, dimensions.height-lw+adj, dimensions.width, dimensions.height-lw+adj);
        g2d.drawLine(dimensions.width-lw+adj, dimensions.height, dimensions.width-lw+adj, 0);
    }

    public void drawBackground() {
        Dimension dimensions = this.graphicsCalculator.getDimensions();
        g2d.fillRect(0, 0, dimensions.width, dimensions.height);
    }

    public void setGraphicsColor(Color colour) {
        g2d.setColor(colour);
    }

    public void setFont(Font font) {
        this.g2d.setFont(font);
    }

    public void setLineWidth() {
        g2d.setStroke(new BasicStroke(this.lineWidth.ordinal()));
    }

    protected BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }

    private void useAntialiasing() {
        this.g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
    }

    public int getScale() {
        return this.scale;
    }

    public void drawError(String errorMessage) {
        setGraphicsColor(Color.RED);
        drawBackground();
        Font errorFont = new Font(this.defaultFont.getFontName(), Font.PLAIN, 10);
        setFont(errorFont);
        setGraphicsColor(Color.WHITE);
        this.g2d.drawString(errorMessage, 5, 14);
    }

}
