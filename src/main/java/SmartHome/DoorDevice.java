// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class DoorDevice extends Device implements DetectableApparatus {

    final static String JSON_TYPE = "DOOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    boolean opened = false;

    /**
     * @return Returns true if the door is opened.
     */
    @Override
    public boolean isDetected() {
        return opened;
    }

    /**
     * For Scenario use only.
     * @param detected Either opens or closes the door if true or false respectively.
     */
    @Override
    public void setDetected(boolean detected) {
        opened = detected;
    }

    public DoorDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public DoorDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue, deviceBuffer);
    }
}
