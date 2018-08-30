/**
 * @author Yvan Burrie
 */

import org.json.simple.JSONObject;

/**
 * Use JSON for the configurations instead of re-inventing the wheel.
 */
interface JsonSerializable {

    /**
     *
     */
    JSONObject JsonSerialize();
}

/**
 *
 */
interface JsonDeserializable extends JsonSerializable {

    /**
     * 
     */
    void JsonDeserialize(JSONObject jsonObject);
}
