// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;
import com.sun.istack.internal.NotNull;
import org.json.simple.*;

/**
 * The simulation system hijacks the automation system to alter its state of reality.
 */
public class Simulator extends Automator {

    final static String JSON_TYPE = "SIMULATOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    /**
     * Specifies the starting-date.
     */
    Clock periodStart = new Clock();

    public Clock getPeriodStart() {
        return periodStart;
    }

    /**
     * Specifies the ending-date.
     */
    Clock periodEnd = new Clock();

    public Clock getPeriodEnd() {
        return periodEnd;
    }

    /**
     *
     */
    private ArrayList<Scenario> scenarios = new ArrayList<>();

    public Simulator() {

        super();
        setupDuration();
    }

    public Simulator(@NotNull JSONObject simulatorBuffer) throws JsonDeserializedError {

        super();
        jsonDeserialize(simulatorBuffer);
        setupDuration();
    }

    @Override
    void setupSynchronizer() {

        super.setupSynchronizer();

        synchronizer.setTime(periodStart.getTimeInMillis());
    }

    /**
     * Checks if the start and end dates are correct and differs them.
     */
    private void setupDuration() {

        syncDuration = periodEnd.compareTo(periodStart);
        if (syncDuration < 0) {
            periodStart = periodEnd;
            periodEnd = periodStart;
        }
    }

    @Override
    public void jsonDeserialize(@NotNull JSONObject simulatorBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(simulatorBuffer);

        Object objectBuffer;

        /* Deserialize periods: */
        objectBuffer = simulatorBuffer.get("PeriodStart");
        if (objectBuffer instanceof JSONObject) {
            JSONObject clockBuffer = (JSONObject) objectBuffer;
            periodStart.jsonDeserialize(clockBuffer);
        }
        objectBuffer = simulatorBuffer.get("PeriodEnd");
        if (objectBuffer instanceof JSONObject) {
            JSONObject clockBuffer = (JSONObject) objectBuffer;
            periodEnd.jsonDeserialize(clockBuffer);
        }
        setupDuration();

        /* Deserialize Scenarios: */
        objectBuffer = simulatorBuffer.get("Scenarios");
        if (objectBuffer instanceof JSONArray) {
            JSONArray scenariosBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : scenariosBuffer) {
                if (elementBuffer instanceof JSONObject) {
                    JSONObject scenarioBuffer = (JSONObject) elementBuffer;
                    objectBuffer = scenarioBuffer.get("Type");
                    if (objectBuffer == null) {
                        throw new JsonDeserializedError("Unspecified Scenario-Type!", this);
                    }
                    String scenarioTypeBuffer;
                    if (objectBuffer instanceof String) {
                        scenarioTypeBuffer = (String) objectBuffer;
                    } else {
                        throw new JsonDeserializedError("Invalid Scenario-Type buffer!", this);
                    }
                    Scenario scenario;
                    switch (scenarioTypeBuffer.toUpperCase()) {
                        case TemperatureScenario.JSON_TYPE:
                            scenario = new TemperatureScenario(this, scenarioBuffer);
                            break;
                        case RefrigeratorScenario.JSON_TYPE:
                            scenario = new RefrigeratorScenario(this, scenarioBuffer);
                            break;
                        default:
                            throw new JsonDeserializedError("Unrecognized Scenario-Type!", this);
                    }
                    scenarios.add(scenario);
                } else {
                    throw new JsonDeserializedError("Invalid Scenario buffer!", this);
                }
            }
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject simulatorBuffer = super.jsonSerialize();

        /* Serialize Periods: */
        if (periodStart != null) {
            simulatorBuffer.put("PeriodStart", periodStart.jsonSerialize());
        }
        if (periodEnd != null) {
            simulatorBuffer.put("PeriodEnd", periodEnd.jsonSerialize());
        }

        /* Serialize Scenarios: */
        JSONArray scenariosBuffer = new JSONArray();
        for (Scenario scenario : scenarios) {
            JSONObject scenarioBuffer = scenario.jsonSerialize();
            scenariosBuffer.add(scenarioBuffer);
        }
        simulatorBuffer.put("Scenarios", scenariosBuffer);

        return simulatorBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        super.synchronize(loopsPerSecond);

        /* Synchronize Scenarios: */
        for (Scenario scenario : scenarios) {
            scenario.synchronize(loopsPerSecond);
        }
    }
}
