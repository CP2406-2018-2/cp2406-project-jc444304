// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;

import com.sun.istack.internal.NotNull;
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

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Points to the synchronization thread.
     */
    Synchronizer synchronizer;

    /**
     * Specifies the synchronization speed according to the configurations.
     * @see Synchronizer::speed
     */
    private long syncSpeed = 1;

    public long getSyncSpeed() {
        return syncSpeed;
    }

    public void setSyncSpeed(long syncSpeed) {
        this.syncSpeed = syncSpeed > 0 ? syncSpeed : 1;
        if (synchronizer != null) {
            synchronizer.setSpeed(this.syncSpeed);
        }
    }

    /**
     * Specifies the synchronization refresh rate for every fake second.
     * @see Synchronizer::loopPerSecond
     */
    private long syncLoopsPerSec = 1;

    public long getSyncLoopsPerSec() {
        return syncLoopsPerSec;
    }

    public void setSyncLoopsPerSec(long syncLoopsPerSec) {
        this.syncLoopsPerSec = syncLoopsPerSec;
        if (synchronizer != null) {
            synchronizer.setLoopsPerSecond(syncLoopsPerSec);
        }
    }

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
     * @return Returns the total number of Venues.
     */
    public long getVenuesCount() {
        return venues.size();
    }

    public Venue getVenue(int index) {
        return venues.get(index);
    }

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

    /**
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
     *
     */
    public Automator() {

    }

    /**
     *
     */
    public Automator(@NotNull JSONObject automatorBuffer) throws JsonDeserializedError {

        jsonDeserialize(automatorBuffer);
    }

    /**
     * Ensures a new synchronization session is available.
     */
    void setupSynchronizer() {

        stop();
        synchronizer = new Synchronizer(
                this,
                syncSpeed,
                syncLoopsPerSec);
    }

    public boolean isStarted() {

        return synchronizer != null && !synchronizer.isInterrupted();
    }

    /**
     * Starts a synchronization session.
     */
    public boolean start() {

        if (synchronizer == null) {
            setupSynchronizer();
        }
        synchronizer.start();
        return true;
    }

    public boolean restart() {

        if (!isStarted()) {
            return false;
        }
        stop();
        start();
        return true;
    }

    public boolean isPaused() {

        return synchronizer != null && synchronizer.paused;
    }

    public boolean pause() {

        if (synchronizer == null) {
            return false;
        }
        boolean wasPaused = synchronizer.paused;
        synchronizer.paused = true;
        return !wasPaused;
    }

    public boolean resume() {

        if (synchronizer == null) {
            return false;
        }
        boolean wasPaused = synchronizer.paused;
        synchronizer.paused = false;
        return wasPaused;
    }

    /**
     * Stops the synchronization session if existent.
     */
    public boolean stop() {

        if (synchronizer == null) {
            return false;
        }
        synchronizer.interrupt();
        synchronizer = null;
        return true;
    }

    /**
     * Loads everything that must be initialized with the configuration file.
     */
    @Override
    public void jsonDeserialize(@NotNull JSONObject automatorBuffer) throws JsonDeserializedError {

        Object objectBuffer;

        objectBuffer = automatorBuffer.get("Name");
        if (objectBuffer instanceof String) {
            name = (String) objectBuffer;
        }
        objectBuffer = automatorBuffer.get("Description");
        if (objectBuffer instanceof String) {
            description = (String) objectBuffer;
        }

        /* Deserialize Synchronizer: */
        objectBuffer = automatorBuffer.get("Synchronizer");
        if (objectBuffer instanceof JSONObject) {
            JSONObject synchronizerBuffer = (JSONObject) objectBuffer;
            objectBuffer = synchronizerBuffer.get("Speed");
            if (objectBuffer instanceof Long) {
                syncSpeed = (long) objectBuffer;
            }
            objectBuffer = synchronizerBuffer.get("LoopsPerSecond");
            if (objectBuffer instanceof Long) {
                syncLoopsPerSec = (long) objectBuffer;
            }
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
                        case WindowFixture.JSON_TYPE:
                            fixture = new WindowFixture(this, fixtureBuffer);
                            break;
                        case DoorFixture.JSON_TYPE:
                            fixture = new DoorFixture(this, fixtureBuffer);
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
                        Venue venue = getVenueById(venueIdBuffer);
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

        if (name != null) {
            automatorBuffer.put("Name", name);
        }
        if (description != null) {
            automatorBuffer.put("Description", description);
        }

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

    public interface OutputCaller {

        void setOutputText(String message);
    }

    private OutputCaller outputCaller;

    public void registerOutputCaller(OutputCaller outputCaller) {
        this.outputCaller = outputCaller;
    }

    void output(String message) {
        if (outputCaller != null) {
            outputCaller.setOutputText(message);
        }
    }
}
