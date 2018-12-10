// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class AirConditionerDevice extends Device implements OpaqueApparatus {

    final static String JSON_TYPE = "AIR_CONDITIONER";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    boolean running = false;

    @Override
    public boolean getState() {
        return running;
    }

    @Override
    public void setState(boolean state) {
        running = state;
    }

    public AirConditionerDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public AirConditionerDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
