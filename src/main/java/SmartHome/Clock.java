// Author: Yvan Burrie

package SmartHome;
import java.util.GregorianCalendar;
import org.json.simple.JSONObject;

public class Clock extends GregorianCalendar implements JsonDeserializable {

    public Clock() {

    }

    public Clock(JSONObject clockBuffer) {
        jsonDeserialize(clockBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject clockBuffer) {

        Object objectBuffer;

        objectBuffer = clockBuffer.get("Year");
        if (objectBuffer instanceof Integer) {
            set(Clock.YEAR, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Month");
        if (objectBuffer instanceof Integer) {
            set(MONTH, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Day");
        if (objectBuffer instanceof Integer) {
            set(DAY_OF_MONTH, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Hour");
        if (objectBuffer instanceof Integer) {
            set(HOUR_OF_DAY, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Minute");
        if (objectBuffer instanceof Integer) {
            set(MINUTE, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Second");
        if (objectBuffer instanceof Integer) {
            set(SECOND, (int) objectBuffer);
        }
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject clockBuffer = new JSONObject();

        clockBuffer.put("Year", get(YEAR));
        clockBuffer.put("Month", get(MONTH));
        clockBuffer.put("Day", get(DAY_OF_MONTH));
        clockBuffer.put("Hour", get(HOUR_OF_DAY));
        clockBuffer.put("Minute", get(MINUTE));
        clockBuffer.put("Second", get(SECOND));

        return clockBuffer;
    }
}
