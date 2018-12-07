// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

public abstract class Asset implements JsonDeserializable, Synchronizable {

    @Override
    public String getJsonType() {
        return null;
    }

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

        String jsonType = getJsonType();
        if (jsonType != null) {
            assetBuffer.put("Type", jsonType);
        }

        return assetBuffer;
    }
}
