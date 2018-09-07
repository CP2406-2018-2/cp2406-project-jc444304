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
    protected GregorianCalendar date1 = new GregorianCalendar(2000, 1, 1, 5, 0);;

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

/**
 * A scenario consists of customizable consequences injected into the automation system.
 */
abstract class Scenario implements JsonDeserializable, Synchronizable {

    protected Simulator simulator;

    public Simulator getSimulator() { return simulator; }

    final public static String TYPE = "DEFAULT";

    protected String name = "Un-named Scenario";

    public String getName() { return name; }

    protected String description = "No description provided";

    public String getDescription() { return description; }

    public Scenario(Simulator simulator) {

        this.simulator = simulator;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        Object bufferObject;

        bufferObject = jsonObject.get("Name");
        if (bufferObject instanceof String) {
            this.name = (String) bufferObject;
        }

        bufferObject = jsonObject.get("Description");
        if (bufferObject instanceof String) {
            this.description = (String) bufferObject;
        }
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

        super.jsonDeserialize(jsonObject);

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

/**
 *
 */
class RefrigeratorScenario extends Scenario {

    final public static String TYPE = "REFRIGERATOR";

    /**
     * Points to the target refrigeration device which this scenario imposes on.
     */
    protected RefrigeratorDevice device;

    /**
     * Specifies the daily number of defrosting cycles.
     */
    private long defrostIntervals = 0;

    /**
     * Specifies the total number of lasting minutes of defrosting cycles.
     */
    private double defrostHours = 0;

    /**
     * Specifies the amount of watts-per-hour during defrosting cycle for each cubic metre.
     */
    private double defrostWatts = 0;

    public double getDefrostWatts() { return this.defrostWatts; }

    /**
     * Specifies the number of daily cooling cycles.
     */
    private long coolingIntervals = 0;

    /**
     * Specifies the total number of hours
     */
    private double coolingHours = 0;

    /**
     * Specifies the watts-per-hour usage on cooling cycle for each cubic metre.
     */
    private double coolingWatts = 0;

    /**
     * Specifies the minimum number of times the fridge door is opened daily.
     */
    private long doorOpenIntervalsMin = 0;

    private long doorOpenIntervalsMax = 0;

    /**
     * Specifies the minimum time in seconds the fridge door lasts when opened.
     */
    private long doorOpenSecondsMin = 0;

    private long doorOpenSecondsMax = 0;

    /**
     * Specifies the amount of watts-per-hour increase when the door is opened for each cubic metre.
     */
    private double doorOpenWattsIncrease = 0;

    public RefrigeratorScenario(Simulator simulator) {

        super(simulator);
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        super.jsonDeserialize(jsonObject);

        Object bufferObject;

        Device device;
        bufferObject = jsonObject.get("DeviceId");
        if (bufferObject instanceof String) {
            String deviceId = (String) bufferObject;
            device = this.simulator.getDeviceById(deviceId);
            if (device instanceof RefrigeratorDevice) {
                this.device = (RefrigeratorDevice) device;
            }
        }
        if (this.device == null) {
            this.device = new RefrigeratorDevice(new Venue(this.simulator));
        }

        bufferObject = jsonObject.get("DefrostIntervals");
        if (bufferObject instanceof Number) {
            this.defrostIntervals = (long) bufferObject;
        }

        bufferObject = jsonObject.get("DefrostHours");
        if (bufferObject instanceof Double) {
            this.defrostHours = (double) bufferObject;
        }

        bufferObject = jsonObject.get("DefrostWatts");
        if (bufferObject instanceof Double) {
            this.defrostWatts = (double) bufferObject;
        }

        bufferObject = jsonObject.get("CoolingIntervals");
        if (bufferObject instanceof Number) {
            this.coolingIntervals = (long) bufferObject;
        }

        bufferObject = jsonObject.get("CoolingHours");
        if (bufferObject instanceof Double) {
            this.coolingHours = (double) bufferObject;
        }

        bufferObject = jsonObject.get("CoolingWatts");
        if (bufferObject instanceof Double) {
            this.coolingWatts = (double) bufferObject;
        }

        bufferObject = jsonObject.get("DoorOpenIntervalsMin");
        if (bufferObject instanceof Number) {
            this.doorOpenIntervalsMin = (long) bufferObject;
        }

        bufferObject = jsonObject.get("DoorOpenIntervalsMax");
        if (bufferObject instanceof Number) {
            this.doorOpenIntervalsMax = (long) bufferObject;
        }

        bufferObject = jsonObject.get("DoorOpenSecondsMin");
        if (bufferObject instanceof Number) {
            this.doorOpenSecondsMin = (long) bufferObject;
        }

        bufferObject = jsonObject.get("DoorOpenSecondsMax");
        if (bufferObject instanceof Number) {
            this.doorOpenSecondsMax = (long) bufferObject;
        }

        bufferObject = jsonObject.get("DoorOpenWattsIncrease");
        if (bufferObject instanceof Double) {
            this.doorOpenWattsIncrease = (double) bufferObject;
        }
    }

    protected long coolingCyclesCount = 0;
    protected long coolingCyclesHours = 0;
    protected long coolingCycleTimeStart = 0;
    protected long coolingCycleTimeEnd = 0;

    @Override
    public void synchronize(long loopsPerSecond) {

        /*
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(this.simulator.synchronizer.getClock());
        System.out.println(calendar);
        */
    }
}
