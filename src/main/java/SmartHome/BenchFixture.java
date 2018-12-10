// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 *
 */
public class BenchFixture extends Fixture {

    final static String JSON_TYPE = "BENCH";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    public BenchFixture(@NotNull Automator automator) {
        super(automator);
    }

    public BenchFixture(@NotNull Automator automator, @NotNull JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }
}
