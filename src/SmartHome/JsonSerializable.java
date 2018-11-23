// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 * Use JSON for the configurations instead of re-inventing the wheel.
 */
interface JsonSerialization {

}

/**
 *
 */
interface JsonSerializable extends JsonSerialization {

    /**
     *
     */
    JSONObject jsonSerialize();
}

/**
 *
 */
interface JsonDeserializable extends JsonSerializable {

    /**
     *
     */
    void jsonDeserialize(JSONObject jsonObject);
}

/**
 * An error caused when using the above JSON interfaces.
 */
abstract class JsonSerializationError extends RuntimeException {

    protected JsonSerialization jsonObject;

    protected String erorrMessage;

    JsonSerializationError (JsonSerializable object, String message) {
        this.jsonObject = object;
        this.erorrMessage = message;
    }
}

/**
 * An error caused when writing JSON data.
 */
class JsonSerializeError extends JsonSerializationError {

    JsonSerializeError(JsonSerializable object, String message) {
        super(object, message);
    }
}

/**
 * An error caused when reading JSON data.
 */
class JsonDeserializeError extends JsonSerializationError {

    JsonDeserializeError(JsonSerializable object, String message) {
        super(object, message);
    }
}
