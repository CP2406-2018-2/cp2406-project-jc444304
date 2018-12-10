// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;

/**
 * An error caused when encoding JSON data.
 */
public class JsonSerializedError extends Exception {

    JsonSerializedError(String message, @NotNull JsonSerializable instance) {
        super(message);
    }
}
