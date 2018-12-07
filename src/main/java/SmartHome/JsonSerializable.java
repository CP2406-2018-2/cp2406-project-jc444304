// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 * Forces encoded JSON representation.
 */
interface JsonSerializable {

    JSONObject jsonSerialize() throws JsonSerializedError;

    String getJsonType();
}

/**
 * Forces decoded JSON representation.
 */
interface JsonDeserializable extends JsonSerializable {

    void jsonDeserialize(JSONObject jsonBuffer) throws JsonDeserializedError;
}
