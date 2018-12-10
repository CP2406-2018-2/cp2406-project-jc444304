// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class VentilatorDevice extends Device implements OpaqueApparatus {

    final static String JSON_TYPE = "VENTILATOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    boolean ventilating = false;

    /**
     * @return Returns true if currently ventilating, otherwise false.
     */
    @Override
    public boolean getState() {
        return ventilating;
    }

    /**
     * @param state Determines whether ventilator should be ventilating.
     */
    @Override
    public void setState(boolean state) {
        ventilating = state;
    }

    public VentilatorDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public VentilatorDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
