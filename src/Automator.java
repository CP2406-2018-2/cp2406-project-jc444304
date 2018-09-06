// Author: Yvan Burrie

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Automator implements Synchronyzable, JsonDeserializable {

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

    /**
     * Points to the synchronization thread.
     */
    Synchronizer synchronizer;

    public void synchronize(long loopsPerSecond) {

        // TODO
    }

    @Override
    public JSONObject jsonSerialize() {
        return null;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {
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

        return jsonObject;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {
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
