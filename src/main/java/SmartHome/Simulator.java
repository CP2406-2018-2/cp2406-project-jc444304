// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.json.simple.*;

/**
 * The simulation system hijacks the automation system to alter its state of reality.
 */
public class Simulator extends Automator {

    /**
     * Specifies the starting-date.
     */
    Clock periodStart;

    /**
     * Specifies the ending-date.
     */
    Clock periodEnd;

    /**
     *
     */
    ArrayList<Scenario> scenarios = new ArrayList<>();

    public Simulator() {

        super();

        setupDuration();
    }

    public Simulator(JSONObject jsonObject) {

        super();

        setupDuration();
        jsonDeserialize(jsonObject);
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
        synchronizer.setTime(periodStart.getTimeInMillis());
    }

    @Override
    public void jsonDeserialize(JSONObject simulatorBuffer) {

        super.jsonDeserialize(simulatorBuffer);

        Object objectBuffer;

        objectBuffer = simulatorBuffer.get("PeriodStart");
        if (objectBuffer instanceof JSONObject) {
            JSONObject clockBuffer = (JSONObject) objectBuffer;
            periodStart = new Clock(clockBuffer);
        }
        objectBuffer = simulatorBuffer.get("PeriodEnd");
        if (objectBuffer instanceof JSONObject) {
            JSONObject clockBuffer = (JSONObject) objectBuffer;
            periodEnd = new Clock(clockBuffer);
        }
        this.setupDuration();

        /* Load Scenarios: */
        objectBuffer = simulatorBuffer.get("Scenarios");
        if (objectBuffer instanceof JSONArray) {
            JSONArray jsonScenarios = (JSONArray) objectBuffer;
            for (Object elementBuffer : jsonScenarios) {
                if (elementBuffer instanceof JSONObject) {
                    JSONObject scenarioBuffer = (JSONObject) elementBuffer;
                    objectBuffer = scenarioBuffer.get("Type");
                    String scenarioTypeBuffer;
                    if (objectBuffer instanceof String) {
                        scenarioTypeBuffer = (String) objectBuffer;
                    } else {
                        throw new JsonDeserializeError(this, "Invalid Scenario-Type!");
                    }
                    Scenario scenario;
                    switch (scenarioTypeBuffer.toUpperCase()) {
                        case TemperatureScenario.TYPE:
                            scenario = new TemperatureScenario(this, scenarioBuffer);
                            break;
                        case RefrigeratorScenario.TYPE:
                            scenario = new RefrigeratorScenario(this, scenarioBuffer);
                            break;
                        default:
                            throw new JsonDeserializeError(this, "Unrecognized Scenario-Type!");
                    }
                    this.scenarios.add(scenario);
                }
            }
        }
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject simulatorBuffer = super.jsonSerialize();

        if (periodStart != null) {
            simulatorBuffer.put("PeriodStart", periodStart.jsonSerialize());
        }
        if (periodEnd != null) {
            simulatorBuffer.put("PeriodEnd", periodEnd.jsonSerialize());
        }

        JSONArray scenariosBuffer = new JSONArray();
        for (Scenario scenario : scenarios) {
            scenariosBuffer.add(scenario.jsonSerialize());
        }
        simulatorBuffer.put("Scenarios", scenariosBuffer);

        return simulatorBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        super.synchronize(loopsPerSecond);

        /* Synchronize scenarios: */
        for (Scenario scenario : scenarios) {
            scenario.synchronize(loopsPerSecond);
        }
    }
}

class Clock extends GregorianCalendar implements JsonDeserializable {

    Clock(JSONObject clockBuffer) {
        jsonDeserialize(clockBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject clockBuffer) {

        Object objectBuffer;

        objectBuffer = clockBuffer.get("Year");
        if (objectBuffer instanceof Integer) {
            set(Clock.YEAR, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Month");
        if (objectBuffer instanceof Integer) {
            set(MONTH, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Day");
        if (objectBuffer instanceof Integer) {
            set(DAY_OF_MONTH, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Hour");
        if (objectBuffer instanceof Integer) {
            set(HOUR_OF_DAY, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Minute");
        if (objectBuffer instanceof Integer) {
            set(MINUTE, (int) objectBuffer);
        }
        objectBuffer = clockBuffer.get("Second");
        if (objectBuffer instanceof Integer) {
            set(SECOND, (int) objectBuffer);
        }
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject clockBuffer = new JSONObject();

        clockBuffer.put("Year", get(YEAR));
        clockBuffer.put("Month", get(MONTH));
        clockBuffer.put("Day", get(DAY_OF_MONTH));
        clockBuffer.put("Hour", get(HOUR_OF_DAY));
        clockBuffer.put("Minute", get(MINUTE));
        clockBuffer.put("Second", get(SECOND));

        return clockBuffer;
    }
}
