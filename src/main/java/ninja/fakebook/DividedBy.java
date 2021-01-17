package ninja.fakebook;

import org.json.JSONArray;
import org.junit.platform.commons.util.StringUtils;

public enum DividedBy {

    SINGLE,
    DOUBLE,
    QUARTERS_HALF,
    HALF_QUARTERS,
    ALL_QUARTERS;

    public static DividedBy getStyle(JSONArray parts) {
        switch ( parts.length() ) {
            case 4:
                if (StringUtils.isNotBlank(parts.getString(0)) &&
                        StringUtils.isNotBlank(parts.getString(1)) &&
                        StringUtils.isNotBlank(parts.getString(2)) &&
                        StringUtils.isNotBlank(parts.getString(3))
                ) {
                    return ALL_QUARTERS;
                } else if (StringUtils.isNotBlank(parts.getString(0)) &&
                        StringUtils.isNotBlank(parts.getString(1)) &&
                        (StringUtils.isBlank(parts.getString(2)) ||
                        StringUtils.isBlank(parts.getString(3))))  {
                    return QUARTERS_HALF;
                } else if ((StringUtils.isBlank(parts.getString(0)) ||
                        StringUtils.isBlank(parts.getString(1))) &&
                        StringUtils.isNotBlank(parts.getString(2)) &&
                        StringUtils.isNotBlank(parts.getString(3)))  {
                    return HALF_QUARTERS;
                }
            case 2:
                return DOUBLE;
            case 1:
            case 0:
                return SINGLE;
            default:
                throw new IllegalArgumentException("Number of parts must be 0, 1, 2, or 4.");
        }
    }

}
