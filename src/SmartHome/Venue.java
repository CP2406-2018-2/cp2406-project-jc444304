// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 *
 */
abstract class Venue implements JsonDeserializable, Synchronizable {

    final public static String TYPE = "DEFAULT";

    protected Automator automator;

    protected String id = "ID";

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected String name = "Un-named Venue";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String description;

    public Venue(Automator automator) {

        this.automator = automator;
    }

    public Venue(Automator automator, String id) {

        this.automator = automator;

        this.id = id;
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject jsonVenue = new JSONObject();

        jsonVenue.put("Type", TYPE);
        jsonVenue.put("Id", this.id);
        jsonVenue.put("Name", this.name);

        return jsonVenue;
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
