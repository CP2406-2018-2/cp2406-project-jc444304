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

/**
 * An error caused when encoding JSON data.
 */
class JsonSerializedError extends Exception {

    JsonSerializedError(String message, JsonSerializable instance) {
        super(message);
    }
}

/**
 * An error caused when decoding JSON data.
 */
class JsonDeserializedError extends Exception {

    JsonDeserializedError(String message, JsonSerializable object) {
        super(message);
    }
}
