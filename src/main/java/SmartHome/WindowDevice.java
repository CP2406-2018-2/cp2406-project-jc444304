// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 *
 */
public class WindowDevice extends Device implements DetectableApparatus {

    final static String JSON_TYPE = "WINDOW_DETECTOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    boolean opened = false;

    /**
     * @return Returns true if the window is opened, otherwise false.
     */
    @Override
    public boolean isDetected() {
        return opened;
    }

    /**
     * For Scenario use only.
     * @param detected Determines whether the window should be opened.
     */
    @Override
    public void setDetected(boolean detected) {
        this.opened = detected;
    }

    /**
     * Specifies the Fixture to which this Device belongs.
     */
    WindowFixture fixture;

    public WindowFixture getFixture() {
        return fixture;
    }

    public WindowDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public WindowDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject deviceBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(deviceBuffer);

        Object objectBuffer;

        objectBuffer = deviceBuffer.get("FixtureId");
        if (objectBuffer instanceof String) {
            String fixtureIdBuffer = (String) objectBuffer;
            Fixture fixtureFetched = automator.getFixtureById(fixtureIdBuffer);
            if (fixtureFetched instanceof WindowFixture) {
                fixture = (WindowFixture) fixtureFetched;
            }
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject deviceBuffer = super.jsonSerialize();

        if (fixture != null) {
            deviceBuffer.put("FixtureId", fixture.getId());
        }

        return deviceBuffer;
    }
}