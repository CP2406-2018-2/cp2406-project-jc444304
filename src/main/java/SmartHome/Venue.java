// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 *
 */
class Venue extends Entity {

    /**
     * Determines whether this Venue is affected by external factors such as wind, rain, etc.
     */
    boolean outdoor = true;

    public Venue(Automator automator) {
        super(automator);
    }

    public Venue(Automator automator, JSONObject venueBuffer) throws JsonDeserializedError {
        super(automator);
        jsonDeserialize(venueBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject venueBuffer) throws JsonDeserializedError {

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

        venueBuffer.put("Outdoor", outdoor);

        return venueBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        // TODO
    }
}
