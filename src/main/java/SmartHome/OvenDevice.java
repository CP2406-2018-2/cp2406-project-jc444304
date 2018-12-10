// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class OvenDevice extends Device {

    final static String JSON_TYPE = "OVEN";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public OvenDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public OvenDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
