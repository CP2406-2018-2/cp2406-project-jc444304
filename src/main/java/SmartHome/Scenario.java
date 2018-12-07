// Author: Yvan Burrie

package SmartHome;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.json.simple.*;

/**
 * A scenario consists of customizable consequences injected into the automation system.
 */
public abstract class Scenario extends Entity {

    GregorianCalendar calendar = new GregorianCalendar();

    public Scenario(Simulator simulator) {
        super(simulator);
    }

    public Scenario(Simulator simulator, JSONObject scenarioBuffer) throws JsonDeserializedError {
        super(simulator, scenarioBuffer);
    }
}

/**
 *
 */
class TemperatureScenario extends Scenario {

    final static String JSON_TYPE = "TEMPERATURE";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

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

    public TemperatureScenario(Simulator simulator, JSONObject scenarioBuffer) throws JsonDeserializedError {
        super(simulator, scenarioBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject scenarioBuffer) throws JsonDeserializedError {

        Object objectBuffer;

        objectBuffer = scenarioBuffer.get("StartDegrees");
        if (objectBuffer instanceof Double) {
            startDegrees = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("StartHour");
        if (objectBuffer instanceof Long) {
            startHour = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("EndDegrees");
        if (objectBuffer instanceof Double) {
            endDegrees = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("EndHour");
        if (objectBuffer instanceof Long) {
            endHour = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("PeekDegrees");
        if (objectBuffer instanceof Double) {
            peekDegrees = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("PeekHour");
        if (objectBuffer instanceof Long) {
            peekHour = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("ShadeDegrees");
        if (objectBuffer instanceof Double) {
            shadeDegrees = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("ShadeHour");
        if (objectBuffer instanceof Integer) {
            shadeHour = (int) objectBuffer;
        }
    }

    @Override
    public JSONObject jsonSerialize() {

        JSONObject scenarioBuffer = new JSONObject();

        scenarioBuffer.put("StartDegrees", startDegrees);
        scenarioBuffer.put("StartHour", startHour);
        scenarioBuffer.put("EndDegrees", endDegrees);
        scenarioBuffer.put("EndHour", endHour);
        scenarioBuffer.put("PeekDegrees", peekDegrees);
        scenarioBuffer.put("PeekHour", peekHour);
        scenarioBuffer.put("ShadeDegrees", shadeDegrees);
        scenarioBuffer.put("ShadeHour", shadeHour);

        return scenarioBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        this.calendar.setTimeInMillis(this.automator.synchronizer.getTime());
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

    final static String JSON_TYPE = "REFRIGERATOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    /**
     * Points to the target refrigeration device which this scenario imposes on.
     */
    RefrigeratorDevice device;

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

    private double coolingRate = 0.0;

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

    RefrigeratorScenario(Simulator simulator) {
        super(simulator);
    }

    RefrigeratorScenario(Simulator simulator, JSONObject scenarioBuffer) throws JsonDeserializedError {
        super(simulator, scenarioBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject scenarioBuffer) throws JsonDeserializedError {

        Object objectBuffer;

        objectBuffer = scenarioBuffer.get("DeviceId");
        if (objectBuffer instanceof String) {
            String deviceId = (String) objectBuffer;
            Device targetDevice = automator.getDeviceById(deviceId);
            if (targetDevice instanceof RefrigeratorDevice) {
                device = (RefrigeratorDevice) targetDevice;
            } else {
                throw new JsonDeserializedError("Could not load Refrigerator-Scenario into Simulator!", this);
            }
        } else {
            throw new JsonDeserializedError("", this);
        }
        objectBuffer = scenarioBuffer.get("DefrostIntervals");
        if (objectBuffer instanceof Number) {
            defrostIntervals = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DefrostHours");
        if (objectBuffer instanceof Double) {
            defrostHours = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DefrostWatts");
        if (objectBuffer instanceof Double) {
            defrostWatts = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("CoolingIntervals");
        if (objectBuffer instanceof Number) {
            coolingIntervals = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("CoolingHours");
        if (objectBuffer instanceof Double) {
            coolingHours = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("CoolingWatts");
        if (objectBuffer instanceof Double) {
            coolingWatts = (double) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DoorOpenIntervalsMin");
        if (objectBuffer instanceof Number) {
            doorOpenIntervalsMin = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DoorOpenIntervalsMax");
        if (objectBuffer instanceof Number) {
            doorOpenIntervalsMax = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DoorOpenSecondsMin");
        if (objectBuffer instanceof Number) {
            doorOpenSecondsMin = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DoorOpenSecondsMax");
        if (objectBuffer instanceof Number) {
            doorOpenSecondsMax = (long) objectBuffer;
        }
        objectBuffer = scenarioBuffer.get("DoorOpenWattsIncrease");
        if (objectBuffer instanceof Double) {
            doorOpenWattsIncrease = (double) objectBuffer;
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject scenarioBuffer = super.jsonSerialize();

        // TODO
        scenarioBuffer.put("DoorOpenWattsIncrease", doorOpenWattsIncrease);

        return scenarioBuffer;
    }

    protected long coolingCyclesCount = 0;
    protected long coolingCyclesHours = 0;
    protected long coolingCycleTimeStart = 0;

    protected long coolingCycleTimeEnd = 0;

    @Override
    public void synchronize(long loopsPerSecond) {

        /*
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(this.automator.synchronizer.getClock());
        System.out.println(calendar);
        */
    }
}
