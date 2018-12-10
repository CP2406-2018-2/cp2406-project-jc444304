
/**
 *
 */
class DoorDevice extends Device {

    final static String JSON_TYPE = "DOOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public DoorDevice(Automator automator, Venue venue) {
        super(automator, venue);
    }

    public DoorDevice(Automator automator, Venue venue, JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }
}
