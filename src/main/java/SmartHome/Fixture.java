// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 *
 */
public abstract class Fixture extends Entity {

    public Fixture(@NotNull Automator automator) {
        super(automator);
    }

    public Fixture(@NotNull Automator automator, @NotNull JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
    }

    @Override
    public void synchronize(long loopsPerSecond) {

    }
}
