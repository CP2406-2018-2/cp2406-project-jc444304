// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class RollerDoorDevice extends DoorDevice implements GradientApparatus {

    final static String JSON_TYPE = "ROLLER_DOOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    /**
     * Specifies the maximum height of the roller in metres.
     */
    double maximumHeight = 0;

    public double getMaximumHeight() {
        return maximumHeight;
    }

    public void setMaximumHeight(double maximumHeight) {
        this.maximumHeight = maximumHeight;
    }

    /**
     * Specifies the current height of the roller in metres.
     */
    double currentHeight = 0;

    @Override
    public double getRange() {
        return currentHeight;
    }

    @Override
    public void setRange(double range) {
        currentHeight = range > maximumHeight ? maximumHeight : range < 0 ? 0 : range;
    }

    /**
     * @return Returns true if the roller is at the maximum height.
     */
    @Override
    public boolean getState() {
        return opened = currentHeight >= maximumHeight;
    }

    /**
     * @param state Rolls upward to maximum height if true, otherwise rolls downwards.
     */
    @Override
    public void setState(boolean state) {
        setRange((opened = state) ? 0 : maximumHeight);
    }

    public RollerDoorDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public RollerDoorDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject deviceBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(deviceBuffer);

        Object objectBuffer;

        objectBuffer = deviceBuffer.get("MaximumHeight");
        if (objectBuffer instanceof Number) {
            maximumHeight = (double) objectBuffer;
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject deviceBuffer = super.jsonSerialize();

        deviceBuffer.put("MaximumHeight", maximumHeight);

        return deviceBuffer;
    }
}
