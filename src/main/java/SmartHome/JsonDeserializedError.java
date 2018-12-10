// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;

/**
 * An error caused when decoding JSON data.
 */
public class JsonDeserializedError extends Exception {

    JsonDeserializedError(String message, @NotNull JsonSerializable instance) {
        super(message);
    }
}
