// Author: Yvan Burrie

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Automator implements JsonDeserializable, Synchronizable {

    /**
     *
     */
    ArrayList<Venue> venues;

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
    ArrayList<Apparatus> devices;

    /**
     *
     */
    ArrayList<Trigger> triggers;

    @Override
    public JSONObject jsonSerialize() {
        return null;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object bufferObject;
        int index;

        bufferObject = jsonObject.get("Venues");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonVenues = (JSONArray) bufferObject;
            for (index = 0; index < jsonVenues.size(); index++) {
                bufferObject = jsonVenues.get(index);
                if (bufferObject instanceof JSONObject) {
                    JSONObject jsonVenue = (JSONObject) bufferObject;
                    bufferObject = jsonVenue.get("Type");
                    if (bufferObject instanceof String) {
                        String jsonVenueType = (String) bufferObject;
                        Venue venue;
                        if (jsonVenueType.toUpperCase().equals(IndoorVenue.TYPE)) {
                            venue = new IndoorVenue(this);
                        } else if (jsonVenueType.toUpperCase().equals(OutdoorVenue.TYPE)) {
                            venue = new OutdoorVenue(this);
                        }else {
                            venue = new Venue(this);
                        }
                        venue.jsonDeserialize(jsonVenue);
                        this.venues.add(venue);
                    }
                }
            }
        }

        bufferObject = jsonObject.get("Devices");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonDevices = (JSONArray) bufferObject;
            for (index = 0; index < jsonDevices.size(); index++) {
                bufferObject = jsonDevices.get(index);
                if (bufferObject instanceof JSONObject) {
                    JSONObject jsonDevice = (JSONObject) bufferObject;
                    bufferObject = jsonDevice.get("Type");
                    if (bufferObject instanceof String) {
                        String jsonDeviceType = (String) bufferObject;
                        Device device;
                        // TODO
                    }
                }
            }
        }

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

    public void synchronize(long loopsPerSecond) {

        // TODO
    }
}

/**
 *
 */
class Venue implements JsonDeserializable {

    final public static String TYPE = "DEFAULT";

    protected Automator automator;

    protected String id = "ID";

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    protected String name = "Un-named Venue";

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

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

        jsonObject.put("Name", this.name);

        return jsonObject;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object bufferObject;

        bufferObject = jsonObject.get("Name");
        if (bufferObject instanceof String) {
            this.name = (String) bufferObject;
        }
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
