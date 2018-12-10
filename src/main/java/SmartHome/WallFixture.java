// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
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

    public WallFixture(@NotNull Automator automator) {
        super(automator);
    }

    public WallFixture(@NotNull Automator automator, @NotNull JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }
}
