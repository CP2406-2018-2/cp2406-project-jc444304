// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 * A device is any apparatus that is connected to the automation of the household.
 */
public abstract class Device extends Entity implements Apparatus {

    /**
     * Specifies the Venue where the Device is located.
     */
    Venue venue;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(@NotNull Venue venue) {
        this.venue = venue;
    }

    public Device(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator);
        this.venue = venue;
    }

    public Device(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator);
        this.venue = venue;
        jsonDeserialize(deviceBuffer);
    }

    @Override
    public void jsonDeserialize(@NotNull JSONObject deviceBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(deviceBuffer);
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject deviceBuffer = super.jsonSerialize();

        if (venue == null) {
            throw new JsonSerializedError("Cannot encode Device!", this);
        }

        deviceBuffer.put("VenueId", venue.id);

        return deviceBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

    }
}
