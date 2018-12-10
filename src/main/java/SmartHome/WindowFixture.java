// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 *
 */
public class WindowFixture extends Fixture {

    final static String JSON_TYPE = "WINDOW";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public WindowFixture(@NotNull Automator automator) {
        super(automator);
    }

    public WindowFixture(@NotNull Automator automator, @NotNull JSONObject fixtureBuffer) throws JsonDeserializedError {
        super(automator);
        super.jsonDeserialize(fixtureBuffer);
    }
}
