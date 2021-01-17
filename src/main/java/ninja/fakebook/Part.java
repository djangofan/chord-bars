package ninja.fakebook;

public enum Part {
    ONE,
    TWO,
    THREE,
    FOUR;

    public int getPart() {
        return this.ordinal()+1;
    }
}
