// Author: Yvan Burrie

package SmartHome;

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

    public BenchFixture(Automator automator) {
        super(automator);
    }

    public BenchFixture(Automator automator, JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }
}
