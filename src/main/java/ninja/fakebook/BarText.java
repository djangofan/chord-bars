package ninja.fakebook;

import java.util.Objects;

import org.json.JSONArray;

public class BarText implements IBarText {

    // https://www.w3.org/2019/03/smufl13/tables/chord-symbols.html

    private final JSONArray text;

    BarText(JSONArray text) {
        this.text = text;
    }

    @Override
    public int getPartCount() {
        return this.text.length();
    }

    @Override
    public String getPartOne() {
        return Objects.requireNonNull(text.getString(Part.ONE.ordinal()));
    }

    @Override
    public String getSecondPart() {
        return Objects.requireNonNull(text.getString(Part.TWO.ordinal()));
    }

    @Override
    public String getThirdPart() {
        return Objects.requireNonNull(text.getString(Part.THREE.ordinal()));
    }

    @Override
    public String getFourthPart() {
        return Objects.requireNonNull(text.getString(Part.FOUR.ordinal()));
    }

}
