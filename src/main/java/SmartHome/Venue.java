// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 *
 */
class Venue extends Entity {

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

        Object objectBuffer = new JSONObject();

        // TODO
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
