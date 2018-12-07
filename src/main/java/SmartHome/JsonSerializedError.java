// Author: Yvan Burrie

package SmartHome;

/**
 * An error caused when encoding JSON data.
 */
public class JsonSerializedError extends Exception {

    JsonSerializedError(String message, JsonSerializable instance) {
        super(message);
    }
}
