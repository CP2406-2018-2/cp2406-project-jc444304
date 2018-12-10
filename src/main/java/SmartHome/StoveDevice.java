// Author: Yvan Burrie

package SmartHome;

import org.json.simple.JSONObject;

/**
 *
 */
public class StoveDevice extends Device {

    final static String JSON_TYPE = "STOVE";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public StoveDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    public StoveDevice(Automator automator, Venue venue, JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue, deviceBuffer);
    }
}
