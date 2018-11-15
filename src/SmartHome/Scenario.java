// Author: Yvan Burrie

import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A scenario consists of customizable consequences injected into the automation system.
 */
public abstract class Scenario implements JsonDeserializable, Synchronizable {

    protected Simulator simulator;

    public Simulator getSimulator() { return simulator; }

    final public static String TYPE = "DEFAULT";

    protected String name = "Un-named Scenario";

    public String getName() { return name; }

    protected String description = "No description provided";

    public String getDescription() { return description; }

    protected GregorianCalendar calendar = new GregorianCalendar();

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

        this.calendar.setTimeInMillis(this.simulator.synchronizer.getClock());
        int clockHour = this.calendar.get(Calendar.HOUR);
        int temperature;

        /* Process day phases: */
        if (clockHour >= this.startHour && clockHour < this.endHour) {
            /* Process morning phase: */
            if (clockHour < this.peekHour) {
                temperature = (int) ((Math.random() * ((this.endDegrees - this.startDegrees / 2) + 1)) + this.startDegrees);
            }
            /* Process afternoon phase: */
            else {
                temperature = (int) ((Math.random() * ((this.endDegrees / 2 - this.startDegrees) + 1)) + this.startDegrees);
            }
        }
        /* Process night phases: */
        else {
            /* Process late night phase: */
            if (clockHour < this.shadeHour) {
                temperature = (int) ((Math.random() * ((this.shadeDegrees - this.endDegrees / 2) + 1)) + this.endDegrees);;
            }
            /* Process early morning phase: */
            else {
                temperature = (int) ((Math.random() * ((this.shadeDegrees / 2 - this.endDegrees) + 1)) + this.endDegrees);
            }
        }
        System.out.println("Temperature at " + this.calendar.get(Calendar.HOUR) + ":" + this.calendar.get(Calendar.MINUTE) + ":" + this.calendar.get(Calendar.SECOND) + ": " + temperature + " deg. C.");
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
