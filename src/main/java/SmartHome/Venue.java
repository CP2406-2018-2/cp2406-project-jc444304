// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

import java.util.ArrayList;

/**
 *
 */
public class Venue extends Entity {

    /**
     * Determines whether this Venue is affected by external factors such as wind, rain, etc.
     */
    private boolean outdoor = false;

    public boolean isOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }

    public Venue(@NotNull Automator automator) {
        super(automator);
    }

    public Venue(@NotNull Automator automator, @NotNull JSONObject venueBuffer) throws JsonDeserializedError {
        super(automator);
        jsonDeserialize(venueBuffer);
    }

    @Override
    public void jsonDeserialize(@NotNull JSONObject venueBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(venueBuffer);

        Object objectBuffer;

        objectBuffer = venueBuffer.get("Outdoor");
        if (objectBuffer instanceof Boolean) {
            outdoor = (boolean) objectBuffer;
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject venueBuffer = super.jsonSerialize();

        if (outdoor) {
            venueBuffer.put("Outdoor", true);
        }

        return venueBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        // TODO
    }
}
