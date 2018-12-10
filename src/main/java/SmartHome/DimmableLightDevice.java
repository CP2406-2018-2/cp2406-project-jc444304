// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class DimmableLightDevice extends LightDevice implements GradientApparatus {

    final static String JSON_TYPE = "DIMMABLE_LIGHT";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    double dimmed = 0;

    @Override
    public double getRange() {
        return dimmed;
    }

    @Override
    public void setRange(double range) {
        turnedOn = 0 != (dimmed = range > 100 ? 100 : range < 0 ? 0 : range);
    }

    /**
     * Specifies the opaquely dim quantity.
     */
    double defaultDim = 50;

    public double getDefaultDim() {
        return defaultDim;
    }

    public void setDefaultDim(double defaultDim) {
        this.defaultDim = defaultDim < 0 ? 0 : defaultDim > 100 ? 100 : defaultDim;
    }

    boolean turnedOn = dimmed > 0;

    @Override
    public boolean getState() {
        return getRange() > 0;
    }

    @Override
    public void setState(boolean state) {
        setRange((turnedOn = state) ? defaultDim : 0);
    }

    public DimmableLightDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public DimmableLightDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject deviceBuffer = super.jsonSerialize();

        deviceBuffer.put("DefaultDim", defaultDim);

        return deviceBuffer;
    }

    @Override
    public void jsonDeserialize(JSONObject deviceBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(deviceBuffer);

        Object objectBuffer;

        objectBuffer = deviceBuffer.get("DefaultDim");
        if (objectBuffer instanceof Number) {
            setDefaultDim((double) objectBuffer);
        }
    }
}
