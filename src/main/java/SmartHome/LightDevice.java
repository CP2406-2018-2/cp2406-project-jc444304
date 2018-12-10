// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class LightDevice extends Device implements OpaqueApparatus {

    final static String JSON_TYPE = "LIGHT";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    boolean turnedOn = false;

    @Override
    public boolean getState() {
        return turnedOn;
    }

    @Override
    public void setState(boolean state) {
        turnedOn = state;
    }

    public LightDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public LightDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
