// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.JSONObject;

/**
 *
 */
public class SprinklerDevice extends Device implements GradientApparatus {

    final static String JSON_TYPE = "SPRINKLER";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    /**
     * Determines the water flow intensity.
     */
    double intensity = 0;

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void setState(boolean state) {

    }

    @Override
    public double getRange() {
        return 0;
    }

    @Override
    public void setRange(double range) {

    }

    public SprinklerDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public SprinklerDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
