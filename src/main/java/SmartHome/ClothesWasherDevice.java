// Author: Yvan Burrie

package SmartHome;

import org.json.simple.JSONObject;

/**
 *
 */
public class ClothesWasherDevice extends Device {

    final static String JSON_TYPE = "CLOTHES_WASHER";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public ClothesWasherDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    public ClothesWasherDevice(Automator automator, Venue venue, JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue, deviceBuffer);
    }
}
