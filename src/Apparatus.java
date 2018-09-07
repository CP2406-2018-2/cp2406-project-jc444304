// Author: Yvan Burrie

import org.json.simple.JSONObject;

/**
 * An apparatus is any kind of appliance or fixture that has a behavior in the household.
 */
interface Apparatus {

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
    Status getStatus();
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
abstract class Device implements Apparatus, Synchronizable, JsonDeserializable {

    protected Status status;

    @Override
    public Status getStatus() { return this.status; }

    final public static String TYPE = "DEFAULT";

    protected String name = "Un-named Device";

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    protected String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    protected Venue venue;

    public Venue getVenue() { return this.venue; }

    public Device(Venue venue) {

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
            this.name = (String)jsonObjectName;
        }
    }
}

/**
 * 
 */
abstract class SensorDevice extends Device {

    final public static String TYPE = "SENSOR";

    public SensorDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class MotionSensorDevice extends SensorDevice {

    final public static String TYPE = "MOTION_SENSOR";

    public MotionSensorDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class LightDevice extends Device {

    final public static String TYPE = "LIGHT";

    public LightDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class VentilatorDevice extends Device {

    final public static String TYPE = "VENTILATOR";

    public VentilatorDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class RefrigeratorDevice extends Device {

    final public static String TYPE = "REFRIGERATOR";

    /**
     * Specifies the number of litres this refrigerator holds.
     */
    double capacity = 0.0;

    public double getCapacity() { return capacity; }

    public void setCapacity(double capacity) { this.capacity = capacity; }

    public RefrigeratorDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class AirConditionerDevice extends Device {

    final public static String TYPE = "AIR_CONDITIONER";

    public AirConditionerDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class IrrigatorDevice extends Device {

    final public static String TYPE = "IRRIGATOR";

    public IrrigatorDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class OvenDevice extends Device {

    final public static String TYPE = "OVEN";

    public OvenDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class VehicleDevice extends Device {

    final public static String TYPE = "VEHICLE";

    public VehicleDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
abstract class AccessDevice extends Device {

    final public static String TYPE = "ACCESS";

    public AccessDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class DoorDevice extends AccessDevice {

    final public static String TYPE = "DOOR";

    public DoorDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class RollerDoorDevice extends AccessDevice {

    final public static String TYPE = "ROLLER_DOOR";

    public RollerDoorDevice(Venue venue) {

        super(venue);
    }
}
