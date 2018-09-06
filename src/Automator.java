// Author: Yvan Burrie

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Automator implements JsonDeserializable, Synchronizable {

    /**
     * Points to the synchronization thread.
     */
    protected Synchronizer synchronizer;

    /**
     * Specifies the synchronization speed according to the configurations.
     * @see Synchronizer::speed
     */
    protected long syncSpeed = 1;

    /**
     * Specifies the synchronization limit according to the configurations.
     * @see Synchronizer::limit
     */
    protected long syncDuration = 1;

    /**
     * Specifies the synchronization loops-per-second according to the configurations.
     * @see Synchronizer::loopPerSecond
     */
    protected long syncLoopsPerSec = 1;

    /**
     *
     */
    protected ArrayList<Venue> venues = new ArrayList<>();

    /**
     * Fetches a venue by its ID.
     * @return Returns null if the venue could not be fetched.
     */
    public Venue getVenueById(String id) {
        for (int i = 0; i < this.venues.size(); i++) {
            Venue venue = this.venues.get(i);
            String venueId = venue.getId();
            if (venueId.equals(id)) {
                return venue;
            }
        }
        return null;
    }

    /**
     *
     */
    protected ArrayList<Device> devices = new ArrayList<>();

    /**
     * Fetches all devices belonging to a venue.
     */
    public ArrayList<Device> getDevicesInVenue(Venue venue) {
        ArrayList<Device> result = new ArrayList<>();
        for (int i = 0; i < this.devices.size(); i++) {
            Device device = this.devices.get(i);
            if (device.getVenue().equals(venue)) {
                result.add(device);
            }
        }
        return result;
    }

    /**
     *
     */
    protected ArrayList<Trigger> triggers = new ArrayList<>();

    /**
     * Determines whether the automation already launched.
     */
    private boolean launched = false;

    /**
     *
     */
    public Automator() {

        this.setupSynchronizer();
    }

    /**
     * Ensures a new synchronization session is available.
     */
    protected void setupSynchronizer() {

        if (this.synchronizer != null && this.synchronizer.isAlive()) {
            this.halt();
        }

        this.launched = false;
        this.synchronizer = new Synchronizer(this);
        this.synchronizer.setSpeed(this.syncSpeed);
        this.synchronizer.setLimit(this.syncDuration * Synchronizer.NANO_SECS_PER_SEC);
        this.synchronizer.setLoopsPerSecond(this.syncLoopsPerSec);
    }

    /**
     * Starts a synchronization session.
     */
    public void launch() {

        if (this.launched) {
            this.setupSynchronizer();
        }
        this.synchronizer.start();
        this.launched = true;
    }

    /**
     * Stops the synchronization session if existent.
     */
    public void halt() {

        if (this.synchronizer != null) {
            this.synchronizer.interrupt();
            this.synchronizer = null;
            this.launched = false;
        }
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object bufferObject;
        int index;

        /* Load sync attributes: */
        bufferObject = jsonObject.get("Synchronizer");
        if (bufferObject instanceof JSONObject) {
            JSONObject jsonSync = (JSONObject) bufferObject;
            bufferObject = jsonSync.get("Speed");
            if (bufferObject != null) {
                this.syncSpeed = (long) bufferObject;
            }
            bufferObject = jsonSync.get("Limit");
            if (bufferObject != null) {
                this.syncDuration = (long) bufferObject;
            }
            bufferObject = jsonSync.get("LoopsPerSecond");
            if (bufferObject != null) {
                this.syncLoopsPerSec = (long) bufferObject;
            }
            this.setupSynchronizer();
        }

        /* Load all venues: */
        bufferObject = jsonObject.get("Venues");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonVenues = (JSONArray) bufferObject;
            for (index = 0; index < jsonVenues.size(); index++) {
                bufferObject = jsonVenues.get(index);
                if (bufferObject instanceof JSONObject) {
                    JSONObject jsonVenue = (JSONObject) bufferObject;
                    bufferObject = jsonVenue.get("Type");
                    String jsonVenueType;
                    if (bufferObject instanceof String) {
                        jsonVenueType = (String) bufferObject;
                    } else {
                        jsonVenueType = Venue.TYPE;
                    }
                    Venue venue;
                    if (jsonVenueType.toUpperCase().equals(IndoorVenue.TYPE)) {
                        venue = new IndoorVenue(this);
                    } else if (jsonVenueType.toUpperCase().equals(OutdoorVenue.TYPE)) {
                        venue = new OutdoorVenue(this);
                    } else {
                        venue = new Venue(this);
                    }
                    venue.jsonDeserialize(jsonVenue);
                    this.venues.add(venue);
                }
            }
        }

        /* Load all devices: */
        bufferObject = jsonObject.get("Devices");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonDevices = (JSONArray) bufferObject;
            for (index = 0; index < jsonDevices.size(); index++) {
                bufferObject = jsonDevices.get(index);
                if (bufferObject instanceof JSONObject) {
                    JSONObject jsonDevice = (JSONObject) bufferObject;
                    bufferObject = jsonDevice.get("Type");
                    String jsonDeviceType;
                    if (bufferObject instanceof String) {
                        jsonDeviceType = (String) bufferObject;
                    } else {
                        jsonDeviceType = Device.TYPE;
                    }
                    bufferObject = jsonDevice.get("VenueId");
                    String jsonDeviceVenueId;
                    if (bufferObject instanceof String) {
                        jsonDeviceVenueId = (String) bufferObject;
                        Venue venue = this.getVenueById(jsonDeviceVenueId);
                        if (venue != null) {
                            Device device = null;
                            if (jsonDeviceType.equals(RefrigeratorDevice.TYPE)) {
                                device = new RefrigeratorDevice(venue);
                            } else if (jsonDeviceType.equals(LightDevice.TYPE)) {
                                device = new LightDevice(venue);
                            } else if (jsonDeviceType.equals(VentilatorDevice.TYPE)) {
                                device = new VentilatorDevice(venue);
                            } else if (jsonDeviceType.equals(AirConditionerDevice.TYPE)) {
                                device = new AirConditionerDevice(venue);
                            } else if (jsonDeviceType.equals(IrrigatorDevice.TYPE)) {
                                device = new IrrigatorDevice(venue);
                            } else if (jsonDeviceType.equals(DoorDevice.TYPE)) {
                                device = new DoorDevice(venue);
                            } else if (jsonDeviceType.equals(RollerDoorDevice.TYPE)) {
                                device = new RollerDoorDevice(venue);
                            } else if (jsonDeviceType.equals(OvenDevice.TYPE)) {
                                device = new OvenDevice(venue);
                            } else if (jsonDeviceType.equals(MotionSensorDevice.TYPE)) {
                                device = new MotionSensorDevice(venue);
                            } else if (jsonDeviceType.equals(VehicleDevice.TYPE)) {
                                device = new VehicleDevice(venue);
                            }
                            if (device != null) {
                                device.jsonDeserialize(jsonDevice);
                                this.devices.add(device);
                            }
                        }
                    }
                }
            }
        }

        /* Load all triggers: */
        bufferObject = jsonObject.get("Triggers");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonTriggers = (JSONArray) bufferObject;
            for (index = 0; index < jsonTriggers.size(); index++) {
                bufferObject = jsonTriggers.get(index);
                if (bufferObject instanceof JSONObject) {
                    JSONObject jsonTrigger = (JSONObject) bufferObject;
                    Trigger trigger = new Trigger(this);
                    trigger.jsonDeserialize(jsonTrigger);
                    this.triggers.add(trigger);
                }
            }
        }
    }

    /**
     * Synchronizes everything that is synchronizable under the automator.
     */
    public void synchronize(long loopsPerSecond) {

        /* Synchronize all venues: */
        for (int i = 0; i < this.venues.size(); i++) {
            Venue venue = this.venues.get(i);
            venue.synchronize(loopsPerSecond);
        }

        /* Synchronize all venues: */
        for (int i = 0; i < this.devices.size(); i++) {
            Device device = this.devices.get(i);
            device.synchronize(loopsPerSecond);
        }

        /* Synchronize all triggers: */
        for (int i = 0; i < this.triggers.size(); i++) {
            Trigger trigger = this.triggers.get(i);
            trigger.synchronize(loopsPerSecond);
        }
    }
}

/**
 *
 */
class Venue implements JsonDeserializable, JsonSerializable, Synchronizable {

    final public static String TYPE = "DEFAULT";

    protected Automator automator;

    protected String id = "ID";

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    protected String name = "Un-named Venue";

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    protected int level = 0;

    public int getLevel() { return this.level; }

    public void setLevel(int level) { this.level = level; }

    public Venue(Automator automator) {

        this.automator = automator;
    }

    public Venue(Automator automator, String id) {

        this.automator = automator;

        this.id = id;
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Type", TYPE);

        jsonObject.put("Id", this.id);

        jsonObject.put("Name", this.name);

        jsonObject.put("Level", this.level);

        return jsonObject;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object bufferObject;

        bufferObject = jsonObject.get("Id");
        if (bufferObject instanceof String) {
            this.id = (String) bufferObject;
        }

        bufferObject = jsonObject.get("Name");
        if (bufferObject instanceof String) {
            this.name = (String) bufferObject;
        }

        bufferObject = jsonObject.get("Level");
        if (bufferObject instanceof Integer) {
            this.level = (int) bufferObject;
        }
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

    final public static String TYPE = "OUTDOOR";

    public OutdoorVenue(Automator automator) {

        super(automator);
    }
}

/**
 *
 */
class IndoorVenue extends Venue {

    final public static String TYPE = "INDOOR";

    public IndoorVenue(Automator automator) {

        super(automator);
    }
}
