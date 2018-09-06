/*
 * @author Yvan Burrie
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * The simulation system hijacks the automation system to alter its state of reality.
 */
public class Simulator extends Automator {

    /**
     * Specifies the starting-date.
     */
    protected Date date1 = new Date(2000, 1, 1, 5, 0);;

    /**
     * Specifies the ending-date.
     */
    protected Date date2 = new Date(2000, 1, 2, 4, 59, 59);

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
        this.syncDuration = this.date2.getTime() - this.date1.getTime();

        this.synchronizer.setClock(this.date1.getTime());
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
                    }
                    if (scenario != null) {
                        this.scenarios.add(scenario);
                    }
                }
            }
        }
    }

    public Date jsonDeserializeDate(JSONObject jsonObject) {

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

        return new Date((int) dateYear, (int) dateMonth, (int) dateDay, (int) dateHour, (int) dateMin, (int) dateSec);
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

/**
 * A scenario consists of customizable consequences injected into the automation system.
 */
abstract class Scenario implements JsonDeserializable, Synchronizable {

    protected Simulator simulator;

    public Simulator getSimulator() { return simulator; }

    final public static String TYPE = "DEFAULT";

    public Scenario(Simulator simulator) {

        this.simulator = simulator;
    }
}

/**
 *
 */
class TemperatureScenario extends Scenario {

    final public static String TYPE = "TEMPERATURE";

    private double startDegrees;

    private long startHour;

    private double endDegrees;

    private long endHour;

    private double peekDegrees;

    private long peekHour;

    private double shadeDegrees;

    private long shadeHour;

    public TemperatureScenario(Simulator simulator) {

        super(simulator);
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object bufferObject;

        bufferObject = jsonObject.get("StartDegrees");
        if (bufferObject instanceof Double) {
            this.startDegrees = (double) bufferObject;
        }

        bufferObject = jsonObject.get("StartHour");
        if (bufferObject instanceof Long) {
            this.startHour = (long) bufferObject;
        }

        bufferObject = jsonObject.get("EndDegrees");
        if (bufferObject instanceof Double) {
            this.endDegrees = (double) bufferObject;
        }

        bufferObject = jsonObject.get("EndHour");
        if (bufferObject instanceof Long) {
            this.endHour = (long) bufferObject;
        }

        bufferObject = jsonObject.get("PeekDegrees");
        if (bufferObject instanceof Double) {
            this.peekDegrees = (double) bufferObject;
        }

        bufferObject = jsonObject.get("PeekHour");
        if (bufferObject instanceof Long) {
            this.peekHour = (long) bufferObject;
        }

        bufferObject = jsonObject.get("ShadeDegrees");
        if (bufferObject instanceof Double) {
            this.shadeDegrees = (double) bufferObject;
        }

        bufferObject = jsonObject.get("ShadeHour");
        if (bufferObject instanceof Long) {
            this.shadeHour = (long) bufferObject;
        }
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        // TODO: add code for temperature control here.
    }
}
