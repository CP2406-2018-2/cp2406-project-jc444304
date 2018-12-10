// Author: Yvan Burrie

package SmartHome;

import org.json.simple.JSONObject;

/**
 *
 */
public class ThermostatDevice extends Device {

    final static String JSON_TYPE = "THERMOSTAT";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public ThermostatDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    public ThermostatDevice(Automator automator, Venue venue, JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue, deviceBuffer);
    }
}
