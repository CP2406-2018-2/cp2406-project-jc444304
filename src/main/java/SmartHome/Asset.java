// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

abstract class Asset implements JsonDeserializable, JsonTypeable, Synchronizable {

    Automator automator;

    public Asset(Automator automator) {
        this.automator = automator;
    }

    public Asset(Automator automator, JSONObject assetBuffer) throws JsonDeserializedError {
        this.automator = automator;
        jsonDeserialize(assetBuffer);
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject assetBuffer = new JSONObject();

        assetBuffer.put("Type", JSON_TYPE);

        return assetBuffer;
    }
}

abstract class Entity extends Asset {

    String id;

    String name;

    String description;

    public Entity(Automator automator) {
        super(automator);
    }

    public Entity(Automator automator, JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject entityBuffer) throws JsonDeserializedError {

        Object objectBuffer;

        objectBuffer = entityBuffer.get("Id");
        if (objectBuffer instanceof String) {
            id = (String) objectBuffer;
        }
        objectBuffer = entityBuffer.get("Name");
        if (objectBuffer instanceof String) {
            name = (String) objectBuffer;
        }
        objectBuffer = entityBuffer.get("Description");
        if (objectBuffer instanceof String) {
            description = (String) objectBuffer;
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject entityBuffer = super.jsonSerialize();

        if (id != null) {
            entityBuffer.put("Id", id);
        }
        if (name != null) {
            entityBuffer.put("Name", name);
        }
        if (description != null) {
            entityBuffer.put("Description", description);
        }
        return entityBuffer;
    }
}
