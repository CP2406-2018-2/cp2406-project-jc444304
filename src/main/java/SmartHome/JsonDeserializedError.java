// Author: Yvan Burrie

package SmartHome;

/**
 * An error caused when decoding JSON data.
 */
public class JsonDeserializedError extends Exception {

    JsonDeserializedError(String message, JsonSerializable instance) {
        super(message);
    }
}
