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
