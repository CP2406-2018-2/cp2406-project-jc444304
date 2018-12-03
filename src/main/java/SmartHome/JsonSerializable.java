// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 * Forces encoded JSON representation.
 */
interface JsonSerializable {

    /**
     *
     */
    JSONObject jsonSerialize() throws JsonSerializedError;
}

/**
 * Forces decoded JSON representation.
 */
interface JsonDeserializable extends JsonSerializable {

    /**
     *
     */
    void jsonDeserialize(JSONObject jsonObject) throws JsonDeserializedError;
}

/**
 * An error caused when encoding JSON data.
 */
class JsonSerializedError extends Exception {

    JsonSerializedError(String message, JsonSerializable object) {
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

interface JsonTypeable {

    String JSON_TYPE = "DEFAULT";
}
