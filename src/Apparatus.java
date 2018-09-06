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

    @Override
    public Status getStatus() {
        return null;
    }

    final public static String JsonMacro = "DEFAULT";

    protected String name = "Un-named Device";

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public void synchronize(long loopsPerSecond) {
        return;
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Name", this.name);

        return jsonObject;
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
class Refrigerator extends Device {

}
