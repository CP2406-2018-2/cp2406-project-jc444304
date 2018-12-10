// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
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

    void jsonDeserialize(@NotNull JSONObject jsonBuffer) throws JsonDeserializedError;
}
