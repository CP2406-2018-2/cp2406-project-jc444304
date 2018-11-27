// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 *
 */
abstract class Venue extends Asset {

    Venue(Automator automator) {
        super(automator);
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject venueBuffer = super.jsonSerialize();

        // TODO

        return venueBuffer;
    }

    @Override
    public void jsonDeserialize(JSONObject venueBuffer) {

        super.jsonDeserialize(venueBuffer);

        Object objectBuffer = new JSONObject();

        // TODO
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        // TODO: any code for refreshing the venue in the thread must be placed here.
    }
}

/**
 *
 */
class OutdoorVenue extends Venue {

    final static String TYPE = "OUTDOOR";

    OutdoorVenue(Automator automator) {
        super(automator);
    }

    OutdoorVenue(Automator automator, JSONObject venueBuffer) {
        super(automator);
        jsonDeserialize(venueBuffer);
    }
}

/**
 *
 */
class IndoorVenue extends Venue {

    final static String TYPE = "INDOOR";

    IndoorVenue(Automator automator) {
        super(automator);
    }

    IndoorVenue(Automator automator, JSONObject venueBuffer) {
        super(automator);
        jsonDeserialize(venueBuffer);
    }
}
