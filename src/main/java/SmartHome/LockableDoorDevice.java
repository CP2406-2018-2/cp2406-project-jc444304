// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class LockableDoorDevice extends DoorDevice implements OpaqueApparatus {

    final static String JSON_TYPE = "LOCKABLE_DOOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    boolean locked = false;

    /**
     * @return Returns true if the door is locked, otherwise false.
     */
    @Override
    public boolean getState() {
        return locked;
    }

    /**
     * @param state Determines if the door should be locked. Note that if the door is opened, then locking it will cease it from being closed.
     */
    @Override
    public void setState(boolean state) {
        locked = state;
    }

    /**
     * For Scenario use only.
     * @param detected Closes or opens the door unless locked.
     */
    @Override
    public void setDetected(boolean detected) throws UnsupportedOperationException {
        if (!(automator instanceof Simulator)) {
            throw new UnsupportedOperationException();
        }
        if (!locked) {
            super.setDetected(detected);
        }
    }

    public LockableDoorDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public LockableDoorDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue, deviceBuffer);
    }
}
