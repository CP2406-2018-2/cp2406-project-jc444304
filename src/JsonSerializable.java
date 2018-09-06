// Author: Yvan Burrie

import org.json.simple.JSONObject;

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
interface JsonDeserializable extends JsonSerialization {

    /**
     *
     */
    void jsonDeserialize(JSONObject jsonObject);
}
