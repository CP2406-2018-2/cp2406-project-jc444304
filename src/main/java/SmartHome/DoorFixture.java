// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 *
 */
public class DoorFixture extends Fixture {

    final static String JSON_TYPE = "DOOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public DoorFixture(@NotNull Automator automator) {
        super(automator);
    }

    public DoorFixture(@NotNull Automator automator, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator);
        jsonDeserialize(deviceBuffer);
    }
}
