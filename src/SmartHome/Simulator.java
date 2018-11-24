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
    protected GregorianCalendar date1 = new GregorianCalendar(2000, 1, 1, 5, 0);

    /**
     * Specifies the ending-date.
     */
    protected GregorianCalendar date2 = new GregorianCalendar(2000, 1, 2, 4, 59, 59);

    /**
     *
     */
    protected ArrayList<Scenario> scenarios = new ArrayList<>();

    public Simulator() {

        super();

        this.setupDuration();
    }

    public Simulator(JSONObject jsonObject) {

        super();

        this.setupDuration();
        this.jsonDeserializeDate(jsonObject);
    }

    /**
     * Checks if the start and end dates are correct.
     */
    protected void setupDuration() {

        /* Differ the seconds from both dates: */
        this.syncDuration = this.date2.compareTo(this.date1);

        this.synchronizer.setClock(this.date1.getTimeInMillis());
    }

    @Override
    public void jsonDeserialize(JSONObject simulatorBuffer) {

        super.jsonDeserialize(simulatorBuffer);

        Object objectBuffer;

        objectBuffer = simulatorBuffer.get("Period");
        if (objectBuffer instanceof JSONArray) {
            JSONArray jsonPeriod = (JSONArray) objectBuffer;
            JSONObject jsonDate;
            /* Load start-time: */
            if (jsonPeriod.size() >= 1) {
                objectBuffer = jsonPeriod.get(0);
                if (objectBuffer instanceof JSONObject) {
                    jsonDate = (JSONObject) objectBuffer;
                    this.date1 = this.jsonDeserializeDate(jsonDate);
                }
            }
            /* Load end-time: */
            if (jsonPeriod.size() >= 2) {
                objectBuffer = jsonPeriod.get(1);
                if (objectBuffer instanceof JSONObject) {
                    jsonDate = (JSONObject) objectBuffer;
                    this.date2 = this.jsonDeserializeDate(jsonDate);
                }
            }
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

    public GregorianCalendar jsonDeserializeDate(JSONObject jsonObject) {

        Object bufferObject;

        long dateYear = 2000;
        bufferObject = jsonObject.get("Year");
        if (bufferObject instanceof Long) {
            dateYear = (long) bufferObject;
        }

        long dateMonth = 1;
        bufferObject = jsonObject.get("Month");
        if (bufferObject instanceof Long) {
            dateMonth = (long) bufferObject;
        }

        long dateDay = 1;
        bufferObject = jsonObject.get("Day");
        if (bufferObject instanceof Long) {
            dateDay = (long) bufferObject;
        }

        long dateHour = 0;
        bufferObject = jsonObject.get("Hour");
        if (bufferObject instanceof Long) {
            dateHour = (long) bufferObject;
        }

        long dateMin = 0;
        bufferObject = jsonObject.get("Minute");
        if (bufferObject instanceof Long) {
            dateMin = (long) bufferObject;
        }

        long dateSec = 0;
        bufferObject = jsonObject.get("Second");
        if (bufferObject instanceof Long) {
            dateSec = (long) bufferObject;
        }

        return new GregorianCalendar((int) dateYear, (int) dateMonth, (int) dateDay, (int) dateHour, (int) dateMin, (int) dateSec);
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
