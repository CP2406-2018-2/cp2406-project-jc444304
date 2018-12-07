// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;

import org.json.simple.*;

/**
 * The automation system merely reflects the reality of a domestic environment.
 */
public class Automator implements JsonDeserializable, Synchronizable {

    final static String JSON_TYPE = "AUTOMATOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    /**
     * Specifies the width and height of the overview.
     */
    long sizeX, sizeY;

    public int getSizeX() {
        return (int) sizeX;
    }

    void setSizeX(long sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return (int) sizeY;
    }

    void setSizeY(long sizeY) {
        this.sizeY = sizeY;
    }

    /**
     * Points to the synchronization thread.
     */
    Synchronizer synchronizer;

    /**
     * Specifies the synchronization speed according to the configurations.
     * @see Synchronizer::speed
     */
    long syncSpeed = 1;

    /**
     * Specifies the synchronization limit according to the configurations.
     * @see Synchronizer::limit
     */
    long syncDuration = 1;

    /**
     * Specifies the synchronization loops-per-second according to the configurations.
     * @see Synchronizer::loopPerSecond
     */
    long syncLoopsPerSec = 1;

    /**
     *
     */
    private ArrayList<Fixture> fixtures = new ArrayList<>();

    public ArrayList<Fixture> getFixtures() {
        return fixtures;
    }

    /**
     * Fetches a Fixture by its ID.
     * @return Returns null if the Fixture could not be fetched.
     */
    public Fixture getFixtureById(String id) {
        for (Fixture fixture : fixtures) {
            if (fixture.id != null && fixture.id.equals(id)) {
                return fixture;
            }
        }
        return null;
    }

    /**
     * Contains a list of Venue instances.
     */
    private ArrayList<Venue> venues = new ArrayList<>();

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    /**

    /**
     * Deletes a Venue by its ID.
     * @param venueId
     * @return Returns the deleted Venue or null if the Venue could not be fetched.
     */
    public Venue removeVenueById(String venueId) {
        for (Venue venue : venues) {
            if (venue.id != null && venue.id.equals(venueId)) {
                venues.remove(venue);
                return venue;
            }
        }
        return null;
    }
     * Fetches a Venue by its ID.
     * @return Returns null if the Venue could not be fetched.
     */
    public Venue getVenueById(String id) {
        for (Venue venue : venues) {
            if (venue.id != null && venue.id.equals(id)) {
                return venue;
            }
        }
        return null;
    }

    /**
     * Contains the list of Device instances.
     */
    private ArrayList<Device> devices = new ArrayList<>();

    public ArrayList<Device> getDevices() {
        return devices;
    }

    /**
     * Fetches a Device by its ID.
     * @return Returns null if the Device could not be fetched.
     */
    public Device getDeviceById(String deviceId) {
        for (Device device : devices) {
            if (device.id != null && device.id.equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Fetches all devices belonging to a venue.
     */
    public ArrayList<Device> getDevicesInVenue(Venue venue) {
        ArrayList<Device> result = new ArrayList<>();
        for (Device device : devices) {
            if (device.venue.equals(venue)) {
                result.add(device);
            }
        }
        return result;
    }

    /**
     * Contains a list of Trigger instances.
     */
    private ArrayList<Trigger> triggers = new ArrayList<>();

    public ArrayList<Trigger> getTriggers() {
        return triggers;
    }

    /**
     * Fetches a Trigger by its ID.
     * @return Returns null if the Trigger could not be fetched.
     */
    public Trigger getTriggerById(String triggerId) {
        for (Trigger trigger : triggers) {
            if (trigger.id != null && trigger.id.equals(triggerId)) {
                return trigger;
            }
        }
        return null;
    }

    /**
     * Determines whether the automation already launched.
     */
    private boolean launched = false;

    /**
     *
     */
    public Automator() {

        setupSynchronizer();
    }

    /**
     *
     */
    public Automator(JSONObject automatorBuffer) throws JsonDeserializedError {

        setupSynchronizer();
        jsonDeserialize(automatorBuffer);
    }

    /**
     * Ensures a new synchronization session is available.
     */
    protected void setupSynchronizer() {

        if (synchronizer != null && synchronizer.isAlive()) {
            halt();
        }

        launched = false;
        synchronizer = new Synchronizer(this);
        synchronizer.setSpeed(syncSpeed);
        synchronizer.setLimit(syncDuration * Synchronizer.NANO_SECS_PER_SEC);
        synchronizer.setLoopsPerSecond(syncLoopsPerSec);
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

    public void pause() {
        synchronizer.paused = !synchronizer.paused;
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

    /**
     * Loads everything that must be initialized with the configuration file.
     */
    @Override
    public void jsonDeserialize(JSONObject automatorBuffer) throws JsonDeserializedError {

        Object objectBuffer;

        /* Deserialize Synchronizer: */
        objectBuffer = automatorBuffer.get("Synchronizer");
        if (objectBuffer instanceof JSONObject) {
            JSONObject synchronizerBuffer = (JSONObject) objectBuffer;
            objectBuffer = synchronizerBuffer.get("Speed");
            if (objectBuffer instanceof Long) {
                syncSpeed = (long) objectBuffer;
            }
            objectBuffer = synchronizerBuffer.get("Limit");
            if (objectBuffer instanceof Long) {
                syncDuration = (long) objectBuffer;
            }
            objectBuffer = synchronizerBuffer.get("LoopsPerSecond");
            if (objectBuffer instanceof Long) {
                syncLoopsPerSec = (long) objectBuffer;
            }
            this.setupSynchronizer();
        }

        /* Deserialize Venues: */
        objectBuffer = automatorBuffer.get("Venues");
        if (objectBuffer instanceof JSONArray) {
            JSONArray venuesBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : venuesBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject venueBuffer = (JSONObject) objectBuffer;
                    Venue venue = new Venue(this, venueBuffer);
                    venues.add(venue);
                }
            }
        }

        /* Deserialize Fixtures: */
        objectBuffer = automatorBuffer.get("Fixtures");
        if (objectBuffer instanceof JSONArray) {
            JSONArray fixturesBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : fixturesBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject fixtureBuffer = (JSONObject) objectBuffer;
                    objectBuffer = fixtureBuffer.get("Type");
                    if (objectBuffer == null) {
                        throw new JsonDeserializedError("Unspecified Fixture-Type!", this);
                    }
                    String fixtureTypeBuffer;
                    if (objectBuffer instanceof String) {
                        fixtureTypeBuffer = (String) objectBuffer;
                    } else {
                        throw new JsonDeserializedError("Invalid Fixture-Type!", this);
                    }
                    Fixture fixture;
                    switch (fixtureTypeBuffer.toUpperCase()) {
                        case WallFixture.JSON_TYPE:
                            fixture = new WallFixture(this, fixtureBuffer);
                            break;
                        case BenchFixture.JSON_TYPE:
                            fixture = new WallFixture(this, fixtureBuffer);
                            break;
                        default:
                            throw new JsonDeserializedError("Unknown Fixture-Type!", this);
                    }
                    fixtures.add(fixture);
                }
            }
        }

        /* Deserialize Devices: */
        objectBuffer = automatorBuffer.get("Devices");
        if (objectBuffer instanceof JSONArray) {
            JSONArray devicesBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : devicesBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject deviceBuffer = (JSONObject) objectBuffer;
                    objectBuffer = deviceBuffer.get("Type");
                    if (objectBuffer == null) {
                        throw new JsonDeserializedError("Unspecified Device-Type!", this);
                    }
                    String deviceTypeBuffer;
                    if (objectBuffer instanceof String) {
                        deviceTypeBuffer = (String) objectBuffer;
                    } else {
                        throw new JsonDeserializedError("Invalid Device-Type!", this);
                    }
                    objectBuffer = deviceBuffer.get("VenueId");
                    if (objectBuffer == null) {
                        throw new JsonDeserializedError("Unspecified Venue-ID from Device!", this);
                    }
                    if (objectBuffer instanceof String) {
                        String venueIdBuffer = (String) objectBuffer;
                        Venue venue = this.getVenueById(venueIdBuffer);
                        Device device;
                        switch (deviceTypeBuffer.toUpperCase()) {
                            case RefrigeratorDevice.JSON_TYPE:
                                device = new RefrigeratorDevice(this, venue, deviceBuffer);
                                break;
                            case LightDevice.JSON_TYPE:
                                device = new LightDevice(this, venue, deviceBuffer);
                                break;
                            case VentilatorDevice.JSON_TYPE:
                                device = new VentilatorDevice(this, venue, deviceBuffer);
                                break;
                            case AirConditionerDevice.JSON_TYPE:
                                device = new AirConditionerDevice(this, venue, deviceBuffer);
                                break;
                            case IrrigatorDevice.JSON_TYPE:
                                device = new IrrigatorDevice(this, venue, deviceBuffer);
                                break;
                            case DoorDevice.JSON_TYPE:
                                device = new DoorDevice(this, venue, deviceBuffer);
                                break;
                            case RollerDoorDevice.JSON_TYPE:
                                device = new RollerDoorDevice(this, venue, deviceBuffer);
                                break;
                            case OvenDevice.JSON_TYPE:
                                device = new OvenDevice(this, venue, deviceBuffer);
                                break;
                            case MotionSensorDevice.JSON_TYPE:
                                device = new MotionSensorDevice(this, venue, deviceBuffer);
                                break;
                            case VehicleDevice.JSON_TYPE:
                                device = new VehicleDevice(this, venue, deviceBuffer);
                                break;
                            case WindowDevice.JSON_TYPE:
                                device = new WindowDevice(this, venue, deviceBuffer);
                                break;
                            default:
                                throw new JsonDeserializedError("Unknown Device-Type!", this);
                        }
                        devices.add(device);
                    }
                }
            }
        }

        /* Deserialize Triggers: */
        objectBuffer = automatorBuffer.get("Triggers");
        if (objectBuffer instanceof JSONArray) {
            JSONArray triggersBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : triggersBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject triggerBuffer = (JSONObject) objectBuffer;
                    Trigger trigger = new Trigger(this, triggerBuffer);
                    triggers.add(trigger);
                }
            }
        }
    }

    /**
     * Saves everything that must be dumped in the configuration file.
     */
    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject automatorBuffer = new JSONObject();

        /* Serialize Synchronizer: */
        JSONObject synchronizerBuffer = new JSONObject();
        synchronizerBuffer.put("Speed", syncSpeed);
        synchronizerBuffer.put("LoopsPerSecond", syncLoopsPerSec);
        automatorBuffer.put("Synchronizer", synchronizerBuffer);

        /* Serialize Venues: */
        JSONArray venuesBuffer = new JSONArray();
        for (Venue venue : venues) {
            JSONObject venueBuffer = venue.jsonSerialize();
            venuesBuffer.add(venueBuffer);
        }
        automatorBuffer.put("Venues", venuesBuffer);

        /* Serialize Fixtures: */
        JSONArray fixturesBuffer = new JSONArray();
        for (Fixture fixture : fixtures) {
            JSONObject fixtureBuffer = fixture.jsonSerialize();
            venuesBuffer.add(fixtureBuffer);
        }
        automatorBuffer.put("Fixtures", fixturesBuffer);

        /* Serialize Devices: */
        JSONArray devicesBuffer = new JSONArray();
        for (Device device : devices) {
            JSONObject deviceBuffer = device.jsonSerialize();
            devicesBuffer.add(deviceBuffer);
        }
        automatorBuffer.put("Devices", devicesBuffer);

        /* Serialize Triggers: */
        JSONArray triggersBuffer = new JSONArray();
        for (Trigger trigger : triggers) {
            JSONObject triggerBuffer = trigger.jsonSerialize();
            triggersBuffer.add(triggerBuffer);
        }
        automatorBuffer.put("Triggers", triggersBuffer);

        return automatorBuffer;
    }

    /**
     * Synchronizes everything that is synchronizable under the automator.
     */
    public void synchronize(long loopsPerSecond) {

        /* Synchronize Venues: */
        for (Venue venue : venues) {
            venue.synchronize(loopsPerSecond);
        }

        /* Synchronize Devices: */
        for (Device device : devices) {
            device.synchronize(loopsPerSecond);
        }

        /* Synchronize Triggers: */
        for (Trigger trigger : triggers) {
            trigger.synchronize(loopsPerSecond);
        }
    }
}
