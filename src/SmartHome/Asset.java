// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

abstract class Asset implements JsonDeserializable, Synchronizable {

    protected Automator automator;

    final static String TYPE = "DEFAULT";

    String id;

    String name;

    String description;

    Asset(Automator automator) {
        this.automator = automator;
    }

    @Override
    public void jsonDeserialize(JSONObject assetBuffer) {

        Object objectBuffer;

        objectBuffer = assetBuffer.get("Id");
        if (objectBuffer instanceof String)
            id = (String) objectBuffer;

        objectBuffer = assetBuffer.get("Name");
        if (objectBuffer instanceof String)
            name = (String) objectBuffer;

        objectBuffer = assetBuffer.get("Description");
        if (objectBuffer instanceof String)
            description = (String) objectBuffer;
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject assetBuffer = new JSONObject();

        assetBuffer.put("Type", TYPE);

        if(id != null)
            assetBuffer.put("Id", id);

        if(name != null)
            assetBuffer.put("Name", name);

        if(description != null)
            assetBuffer.put("Description", description);

        return assetBuffer;
    }
}
