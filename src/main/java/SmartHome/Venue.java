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

    String pyCallback;

    String pyCallbackName;

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

        objectBuffer = venueBuffer.get("PyCallback");
        if (objectBuffer instanceof String) {
            pyCallback = "\n" + objectBuffer;
            pyCallbackName = "venue_" + (id + "_" + name).replace(" ", "_");
            String pyExec = "def " + pyCallbackName + "():\n" + pyCallback.replace("\n", "\n\t");
            automator.pyInterpreter.exec(pyExec);
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject venueBuffer = super.jsonSerialize();

        if (outdoor) {
            venueBuffer.put("Outdoor", true);
        }
        if (pyCallback != null) {
            venueBuffer.put("PyCallback", pyCallback.replace("\n\t", "\n"));
        }

        return venueBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        if (pyCallback != null) {
            automator.pyInterpreter.exec(pyCallbackName + "()");
        }
    }

    /**
     * Calculates the average temperature according to each temperature sensor within the Venue.
     * @return Returns null if there are no temperature sensors found.
     */
    public Double getAverageTemperature() {

        long devicesCount = 0;
        double temperatureSum = 0.0;

        ArrayList<Device> devices = automator.getDevicesInVenue(this);
        for (Device device : devices) {
            if (device instanceof TemperatureSensorApparatus) {
                TemperatureSensorApparatus temperatureSensor = (TemperatureSensorApparatus) device;
                devicesCount++;
                temperatureSum += temperatureSensor.getSensoredTemperature();
            }
        }
        return devicesCount > 0 ? temperatureSum / devicesCount : null;
    }
}
