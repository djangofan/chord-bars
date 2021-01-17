package ninja.fakebook;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Helps calculate where text will be placed within a triangle area on a 2D canvas.
 * Orientation matters when determining Point where text will begin.
 *  https://www.inchcalculator.com/45-45-90-triangle-calculator/
 */
public class QuarterSection {

    private final Part part;
    private final Point left;
    private final Point right;
    private final Point top;

    // variables for solving 'sectionContains'
    private final double x3, y3;
    private final double y23, x32, y31, x13;
    private final double det, minD, maxD;

    QuarterSection(Part part, Point left1, Point right3, Point top2) {
        this.part = Objects.requireNonNull(part);
        this.left = Objects.requireNonNull(left1);
        this.right = Objects.requireNonNull(right3);
        this.top = Objects.requireNonNull(top2);
        if (!checkIsIsosceles()) {
            throw new IllegalArgumentException("Coordinates are not a valid isosceles triangle.");
        }
        if (!verifyThisOrientation()) {
            throw new IllegalArgumentException("Isosceles triangle is not oriented correctly.");
        }
        this.x3 = right3.x;
        this.y3 = right3.y;
        y23 = top2.y - y3;
        x32 = x3 - top2.x;
        y31 = y3 - left1.y;
        x13 = left1.x - x3;
        det = y23 * x13 - x32 * y31;
        minD = Math.min(det, 0);
        maxD = Math.max(det, 0);
    }

    boolean sectionContains(Point point) {
        double x = point.getX();
        double y = point.getY();
        double dx = x - x3;
        double dy = y - y3;
        double a = y23 * dx + x32 * dy;
        if (a < minD || a > maxD) {
            return false;
        }
        double b = y31 * dx + x13 * dy;
        if (b < minD || b > maxD) {
            return false;
        }
        double c = det - a - b;
        if (c < minD || c > maxD) {
            return false;
        }
        return true;
    }

    public int calculateDistance(Point a, Point b) {
        return (int) Point2D.distance(a.x, a.y, b.x, b.y);
    }

    public int calculateAllowedTextWidth(int scale) {
        switch(part) {
            case THREE:
            case TWO:
                return horizontalWidthUpOrDownOrientation() -scale*12;
            case ONE:
            case FOUR:
                return horizontalHeightRightOrLeftOrientation() -scale*3;
            default:
                return 1;
        }
    }

    private int horizontalWidthUpOrDownOrientation() {
        return (int)(calculateDistance(left, right) * 0.7); // just an estimate for now
    }

    private int horizontalHeightRightOrLeftOrientation() {
        Point midBasePointY = getTriangleMidBasePoint(left, right);
        return calculateDistance(midBasePointY, top);
    }

    private Point getTriangleMidBasePoint(Point left, Point right) {
        switch(part) {
            case THREE:
            case TWO:
                return getSideHorizontalMidpoint(left, right);
            case ONE:
            case FOUR:
                return getSideVerticalMidpoint(left, right);
        }
        return null;
    }

    /* Assumes isosceles triangle. */
    private Point getSideHorizontalMidpoint(Point left, Point right) {
        switch (part) {
            case THREE:
                int baseLengthU = right.x - left.x;
                return new Point(left.x + baseLengthU/2, left.y);
            case TWO:
                int baseLengthD = left.x - right.x;
                return new Point(right.x + baseLengthD/2, left.y);
            default:
                throw new IllegalStateException("Unexpected U/D orientation: " + part);
        }
    }

    /* Assumes isosceles triangle. */
    private Point getSideVerticalMidpoint(Point left, Point right) {
        //TODO might not know difference between being below or above y-axis !!!!!
        switch (part) {
            case ONE:
                int baseLengthR = left.y - right.y;
                return new Point(left.x, right.y + baseLengthR/2);
            case FOUR:
                int baseLengthL = right.y - left.y;
                return new Point(left.x, left.y + baseLengthL/2);
            default:
                throw new IllegalStateException("Unexpected R/L orientation: " + part);
        }
    }

    public boolean verifyThisOrientation() {
        switch(part) {
            case ONE:
                return left.y < top.y && right.y > top.y && left.x < top.x && right.x < top.x;
            case TWO:
                return left.y < top.y && right.y < top.y && left.x > top.x && right.x < top.x;
            case THREE:
                return left.y > top.y && right.y > top.y && left.x < top.x && right.x > top.x;
            case FOUR:
                return left.y > top.y && right.y < top.y && left.x > top.x && right.x > top.x;
            default:
                return false;
        }
    }

    public boolean checkIsIsosceles() {
        return calculateDistance(left, top) == calculateDistance(right, top);
    }

    public Part getPart() {
        return part;
    }

    public Point getLeft() {
        return left;
    }

    public Point getRight() {
        return right;
    }

    public Point getTop() {
        return top;
    }

}
