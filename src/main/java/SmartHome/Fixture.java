// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

/**
 *
 */
public abstract class Fixture extends Entity {

    public Fixture(Automator automator) {
        super(automator);
    }

    public Fixture(Automator automator, JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }

    @Override
    public void synchronize(long loopsPerSecond) {

    }
}

class WallFixture extends Fixture {

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

/**
 *
 */
class BenchFixture extends Fixture {

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
