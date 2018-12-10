// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class MotionSensorDevice extends Device implements DetectableApparatus {

    final static String JSON_TYPE = "MOTION_SENSOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    /**
     * Determines whether there was some movement detected in the venue.
     */
    boolean movement = false;

    @Override
    public boolean isDetected() {
        return movement;
    }

    @Override
    public void setDetected(boolean detected) {
        movement = detected;
    }

    public MotionSensorDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public MotionSensorDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
