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
class LightDevice extends Device {

    final public static String TYPE = "LIGHT";

    public LightDevice(Venue venue) {

        super(venue);
    }
}

/**
 *
 */
class RefrigeratorDevice extends Device {

    final public static String TYPE = "REFRIGERATOR";

    public RefrigeratorDevice(Venue venue) {

        super(venue);
    }
}
