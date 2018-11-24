// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;
import org.json.simple.*;

/**
 * An apparatus is any kind of appliance or fixture that has a behavior in the household.
 */
interface Apparatus {

}

/**
 * A passive apparatus behaves as an input for the household, for example, a motion sensor.
 */
interface DetectableApparatus extends Apparatus {

    /**
     * Handles the event when this apparatus detects something.
     * @return Returns TRUE if the apparatus successfully set its passive state, otherwise FALSE.
     */
    boolean handleDetection(boolean detection);
}

/**
 * An active apparatus behaves as an output for the household, for example, a light or fan.
 */
interface ContinuousApparatus extends Apparatus {

    /**
     * Handles the event when this apparatus projects something.
     * For example, when a light is turned on or off.
     * @return Returns TRUE if the apparatus successfully set its active state, otherwise FALSE.
     */
    boolean handleThroughput(boolean throughput);
}

/**
 * An opaque apparatus has a binary state of behavior, for example, some lights can only be on or off.
 */
interface OpaqueApparatus extends Apparatus {

    /**
     * Retrieves whether the apparatus is on or off.
     */
    boolean getValue();

    /**
     * Assigns whether the apparatus is on or off.
     */
    void setValue(boolean value);
}

/**
 * A gradient apparatus has a ranging state of behaviors, for example, a fan could have 5 speeds.
 */
interface GradientApparatus extends OpaqueApparatus {

    /**
     * Retrieves the specific value of this apparatus.
     */
    double getRange();

    /**
     * Assigns the specific value to this apparatus.
     */
    void setRange(double range);
}

/**
 * A device is any apparatus that is connected to the automation of the household.
 */
abstract class Device extends Asset implements Apparatus {

    /**
     *
     */
    enum Status {
        MALFUNCTIONING,
        UNRESPONSIVE,
        WORKING,
    }

    /**
     * Determines whether the apparatus is currently working.
     * If not, then it may be malfunctioning, accidentally unplugged or uncharged, etc.
     */
    protected Status status;

    public Status getStatus() {
        return this.status;
    }

    /**
     * Specifies where this device is located within a single venue.
     */
    Venue venue;

    Device(Automator automator, Venue venue) {
        super(automator);
        this.venue = venue;
    }

    @Override
    public void synchronize(long loopsPerSecond) {
        return;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object jsonObjectName = jsonObject.get("Name");
        if (jsonObjectName instanceof String) {
            this.name = (String) jsonObjectName;
        }
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Type", this.TYPE);
        jsonObject.put("Name", this.name);

        return jsonObject;
    }
}

/**
 * 
 */
abstract class SensorDevice extends Device {

    final static String TYPE = "SENSOR";

    SensorDevice(Automator automator, Venue venue) {

        super(automator, venue);
    }
}

/**
 *
 */
class MotionSensorDevice extends SensorDevice {

    final static String TYPE = "MOTION_SENSOR";

    MotionSensorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    MotionSensorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class LightDevice extends Device {

    final static String TYPE = "LIGHT";

    LightDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    LightDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class VentilatorDevice extends Device {

    final static String TYPE = "VENTILATOR";

    VentilatorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    VentilatorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

class DishWasherDevice extends Device {

    DishWasherDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    DishWasherDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class RefrigeratorDevice extends Device {

    final static String TYPE = "REFRIGERATOR";

    /**
     * A storage section of the refrigerator such as the freezer compared to the main storage.
     */
    class Compartment implements JsonDeserializable, Synchronizable {

        protected String name = "Unknown Compartment";

        /**
         * Specifies the number of cubic litres this refrigerator holds.
         */
        long capacity = 0;

        public long getCapacity() { return this.capacity; }

        public void setCapacity(long capacity) { this.capacity = capacity; }

        protected double maximumTemperature = 4.0;

        protected double currentTemperature = 0.0;

        @Override
        public void jsonDeserialize(JSONObject jsonObject) {

            Object bufferObject;

            bufferObject = jsonObject.get("MaximumTemperature");
            if (bufferObject instanceof Double) {
                this.maximumTemperature = (double) bufferObject;
            }
        }

        @Override
        public JSONObject jsonSerialize() {
            return null;
        }

        @Override
        public void synchronize(long loopsPerSecond) {

        }
    }

    protected ArrayList<Compartment> compartments = new ArrayList<>();

    RefrigeratorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    RefrigeratorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        super.jsonDeserialize(jsonObject);

        Object bufferObject;

        bufferObject = jsonObject.get("Compartments");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonCompartments = (JSONArray) bufferObject;
            for (Object jsonCompartment: jsonCompartments) {
                if (jsonCompartment instanceof JSONObject) {
                    Compartment compartment = new Compartment();
                    compartment.jsonDeserialize((JSONObject) jsonCompartment);
                    this.compartments.add(compartment);
                }
            }
        }
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        super.synchronize(loopsPerSecond);

        for (Compartment compartment: this.compartments) {
            compartment.synchronize(loopsPerSecond);
        }
    }
}

/**
 *
 */
class AirConditionerDevice extends Device {

    final static String TYPE = "AIR_CONDITIONER";

    AirConditionerDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    AirConditionerDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class IrrigatorDevice extends Device {

    final static String TYPE = "IRRIGATOR";

    IrrigatorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    IrrigatorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class OvenDevice extends Device {

    final static String TYPE = "OVEN";

    OvenDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    OvenDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class VehicleDevice extends Device {

    final static String TYPE = "VEHICLE";

    VehicleDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    VehicleDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
abstract class AccessDevice extends Device {

    final static String TYPE = "ACCESS";

    AccessDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    AccessDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class DoorDevice extends AccessDevice {

    final static String TYPE = "DOOR";

    DoorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    DoorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}

/**
 *
 */
class RollerDoorDevice extends AccessDevice {

    final static String TYPE = "ROLLER_DOOR";

    RollerDoorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    RollerDoorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
