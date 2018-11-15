/*
 * @author Yvan Burrie
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

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

    /**
     * Checks if the start and end dates are correct.
     */
    protected void setupDuration() {

        /* Differ the seconds from both dates: */
        this.syncDuration = this.date2.compareTo(this.date1);

        this.synchronizer.setClock(this.date1.getTimeInMillis());
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        super.jsonDeserialize(jsonObject);

        Object bufferObject;

        bufferObject = jsonObject.get("Period");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonPeriod = (JSONArray) bufferObject;
            JSONObject jsonDate;
            /* Load start-time: */
            if (jsonPeriod.size() >= 1) {
                bufferObject = jsonPeriod.get(0);
                if (bufferObject instanceof JSONObject) {
                    jsonDate = (JSONObject) bufferObject;
                    this.date1 = this.jsonDeserializeDate(jsonDate);
                }
            }
            /* Load end-time: */
            if (jsonPeriod.size() >= 2) {
                bufferObject = jsonPeriod.get(1);
                if (bufferObject instanceof JSONObject) {
                    jsonDate = (JSONObject) bufferObject;
                    this.date2 = this.jsonDeserializeDate(jsonDate);
                }
            }
        }
        this.setupDuration();

        bufferObject = jsonObject.get("Scenarios");
        if (bufferObject instanceof JSONArray) {
            JSONArray jsonScenarios = (JSONArray) bufferObject;
            for (Object jsonScenario1 : jsonScenarios) {
                bufferObject = jsonScenario1;
                if (bufferObject instanceof JSONObject) {
                    JSONObject jsonScenario = (JSONObject) bufferObject;
                    bufferObject = jsonScenario.get("Type");
                    String scenarioType;
                    if (bufferObject instanceof String) {
                        scenarioType = (String) bufferObject;
                    } else {
                        scenarioType = Scenario.TYPE;
                    }
                    Scenario scenario = null;
                    if (scenarioType.equals(TemperatureScenario.TYPE)) {
                        scenario = new TemperatureScenario(this);
                    } else if (scenarioType.equals(RefrigeratorScenario.TYPE)) {
                        scenario = new RefrigeratorScenario(this);
                    }
                    if (scenario != null) {
                        scenario.jsonDeserialize(jsonScenario);
                        this.scenarios.add(scenario);
                    }
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

        /* Synchronize all scenarios: */
        for (Scenario scenario : this.scenarios) {
            scenario.synchronize(loopsPerSecond);
        }
    }
}
