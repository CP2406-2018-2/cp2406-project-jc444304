// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 *
 */
public class WallFixture extends Fixture {

    final static String JSON_TYPE = "WALL";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public WallFixture(Automator automator) {
        super(automator);
    }

    public WallFixture(Automator automator, JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }
}
