// Author: Yvan Burrie

import org.json.simple.JSONObject;

/**
 * Use JSON for the configurations instead of re-inventing the wheel.
 */
interface JsonSerializable {

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
