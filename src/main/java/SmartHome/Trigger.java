// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import org.json.simple.*;

/**
 * Each trigger acts like a logical-gateway where events are tried on a thread and fire actions if successful.
 */
public class Trigger extends Entity {

    /**
     * Determines if the trigger will start at the very beginning after being loaded.
     */
    private boolean starting = false;

    public boolean isStarting() {
        return starting;
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
    }

    /**
     * When a trigger is looping, this means it will not be disabled after it is successful and the thread will try it again.
     */
    private boolean looping = false;

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private ArrayList<Event> events = new ArrayList<>();

    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Events in each trigger are tried for checking whether the trigger can fire actions.
     */
    public abstract class Event extends Entity {

        /**
         * All events within a trigger can be AND'ED or OR'ED.
         */
        boolean orPrevious = false;

        public boolean isOrPrevious() {
            return orPrevious;
        }

        public void setOrPrevious(boolean orPrevious) {
            this.orPrevious = orPrevious;
        }

        /**
         * Determines whether the event was successful upon trying.
         */
        boolean successful = false;

        public Event() {

            super(Trigger.this.automator);
        }

        public Event(@NotNull JSONObject eventBuffer) throws JsonDeserializedError {

            super(Trigger.this.automator);
            jsonDeserialize(eventBuffer);
        }

        @Override
        public void jsonDeserialize(JSONObject eventBuffer) throws JsonDeserializedError {

            super.jsonDeserialize(eventBuffer);

            Object objectBuffer;

            objectBuffer = eventBuffer.get("OrPrevious");
            if (objectBuffer instanceof Boolean) {
                orPrevious = (boolean) objectBuffer;
            }
        }

        @Override
        public JSONObject jsonSerialize() throws JsonSerializedError {

            JSONObject eventBuffer = super.jsonSerialize();

            eventBuffer.put("OrPrevious", orPrevious);

            return eventBuffer;
        }

        void initialize() {

        }
    }

    private ArrayList<Action> actions = new ArrayList<>();

    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     *
     */
    public abstract class Action extends Entity {

        public Action() {

            super(Trigger.this.automator);
        }

        public Action(@NotNull JSONObject actionBuffer) throws JsonDeserializedError {

            super(Trigger.this.automator, actionBuffer);
        }

        void initialize() {

        }
    }

    public Trigger(@NotNull Automator automator) {

        super(automator);
    }

    public Trigger(@NotNull Automator automator, @NotNull JSONObject triggerBuffer) throws JsonDeserializedError {

        super(automator, triggerBuffer);
    }

    @Override
    public void jsonDeserialize(@NotNull JSONObject triggerBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(triggerBuffer);

        Object objectBuffer;

        objectBuffer = triggerBuffer.get("Starting");
        if (objectBuffer instanceof Boolean) {
            starting = (boolean) objectBuffer;
        }
        objectBuffer = triggerBuffer.get("Looping");
        if (objectBuffer instanceof Boolean) {
            looping = (boolean) objectBuffer;
        }
        objectBuffer = triggerBuffer.get("Message");
        if (objectBuffer instanceof String) {
            message = (String) objectBuffer;
        }

        /* Deserialize Events: */
        objectBuffer = triggerBuffer.get("Events");
        if (objectBuffer instanceof JSONArray) {
            JSONArray eventsBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : eventsBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject eventBuffer = (JSONObject) objectBuffer;
                    objectBuffer = eventBuffer.get("Type");
                    if (objectBuffer == null) {
                        throw new JsonDeserializedError("Undefined Trigger Event type", this);
                    }
                    if (objectBuffer instanceof String) {
                        String eventTypeBuffer = (String) objectBuffer;
                        Event event;
                        switch (eventTypeBuffer.toUpperCase()) {
                            case ApparatusDetectionEvent.JSON_TYPE:
                                event = new ApparatusDetectionEvent(eventBuffer);
                                break;
                            // TODO: more events
                            default:
                                throw new JsonDeserializedError("", this);
                        }
                        events.add(event);
                    } else {
                        throw new JsonDeserializedError("", this);
                    }
                }
            }
        }

        /* Deserialize Actions: */
        objectBuffer = triggerBuffer.get("Actions");
        if (objectBuffer instanceof JSONArray) {
            JSONArray actionsBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : actionsBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject actionBuffer = (JSONObject) objectBuffer;
                    objectBuffer = actionBuffer.get("Type");
                    if (objectBuffer == null) {
                        throw new JsonDeserializedError("", this);
                    }
                    if (objectBuffer instanceof String) {
                        String actionTypeBuffer = (String) objectBuffer;
                        Action action;
                        switch (actionTypeBuffer.toUpperCase()) {
                            case ChangeTriggerStartingAction.JSON_TYPE:
                                action = new ChangeTriggerStartingAction(actionBuffer);
                                break;
                            // TODO: more actions
                            default:
                                throw new JsonDeserializedError("", this);
                        }
                        actions.add(action);
                    } else {
                        throw new JsonDeserializedError("", this);
                    }
                }
            }
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject triggerBuffer = super.jsonSerialize();

        triggerBuffer.put("Starting", starting);
        triggerBuffer.put("Looping", looping);
        if (message != null) {
            triggerBuffer.put("Message", message);
        }

        /* Serialize Events: */
        JSONArray eventsBuffer = new JSONArray();
        for (Event event : events) {
            JSONObject eventBuffer = event.jsonSerialize();
            eventsBuffer.add(eventBuffer);
        }
        triggerBuffer.put("Events", eventsBuffer);

        /* Serialize Actions: */
        JSONArray actionsBuffer = new JSONArray();
        for (Action action : actions) {
            JSONObject actionBuffer = action.jsonSerialize();
            actionsBuffer.add(actionBuffer);
        }
        triggerBuffer.put("Actions", actionsBuffer);

        return triggerBuffer;
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        /* Check if this trigger should run: */
        if (!starting) return;

        /* If the trigger does not have events, the actions will be fired immediately: */
        boolean fireActions = events.isEmpty();
        if (!fireActions) {

            /* If the first event is AND'ED with its previous, then the initial result must be TRUE, vice versa: */
            boolean eventsSuccessful = events.get(0).orPrevious;

            /* Try out each event: */
            for (Event event : events) {
                event.synchronize(loopsPerSecond);

                /* Either AND or OR the result with the previous event: */
                if (event.orPrevious) {
                    eventsSuccessful |= event.successful;
                } else {
                    eventsSuccessful &= event.successful;
                }

                /* Make sure event success is always false for next thread cycle: */
                event.successful = false;

                /* As soon as the result succeeds, let actions fire: */
                if (eventsSuccessful) {
                    fireActions = true;
                    break;
                }
            }
        }

        /* Fire all actions: */
        if (fireActions){
            for (Action action : actions) {
                action.synchronize(loopsPerSecond);
            }
        }

        /* If the trigger is looping, then it must start again on next thread cycle. */
        starting = looping;

        /* Send message now that trigger has succeeded: */
        automator.output(message);
    }

    void initialize() {

        for (Event event : events) {
            event.initialize();
        }
        for (Action action : actions) {
            action.initialize();
        }
    }

    /**
     * Event is successful when its apparatus is detecting something.
     */
    public class ApparatusDetectionEvent extends Event {

        final static String JSON_TYPE = "APPARATUS_DETECTION";

        @Override
        public String getJsonType() {
            return JSON_TYPE;
        }

        private DetectableApparatus detectableDevice;

        public ApparatusDetectionEvent() {

            super();
        }

        public ApparatusDetectionEvent(@NotNull DetectableApparatus detectableDevice) {

            super();
            this.detectableDevice = detectableDevice;
        }

        public ApparatusDetectionEvent(@NotNull JSONObject eventBuffer) throws JsonDeserializedError {

            super.jsonDeserialize(eventBuffer);

            Object objectBuffer;

            objectBuffer = eventBuffer.get("DeviceId");
            if (objectBuffer instanceof String) {
                String deviceId = (String) objectBuffer;
                Device device = Trigger.this.automator.getDeviceById(deviceId);
                if (device instanceof DetectableApparatus) {
                    this.detectableDevice = (DetectableApparatus) device;
                } else {
                    throw new JsonDeserializedError("", this);
                }
            }
        }

        @Override
        public void synchronize(long loopsPerSecond) {

            if (detectableDevice != null) {
                successful = detectableDevice.isDetected();
            }
        }
    }

    /**
     * This action will force a trigger to either start or not start.
     */
    public class ChangeTriggerStartingAction extends Action {

        final static String JSON_TYPE = "CHANGE_TRIGGER_STATUS";

        @Override
        public String getJsonType() {
            return JSON_TYPE;
        }

        Trigger targetTrigger;

        String targetTriggerId;

        boolean targetTriggerStarting = false;

        public ChangeTriggerStartingAction() {

        }

        public ChangeTriggerStartingAction(@NotNull JSONObject actionBuffer) throws JsonDeserializedError {

            jsonDeserialize(actionBuffer);
        }

        @Override
        public void jsonDeserialize(@NotNull JSONObject actionBuffer) throws JsonDeserializedError {

            super.jsonDeserialize(actionBuffer);

            Object bufferObject;

            bufferObject = actionBuffer.get("TargetTriggerId");
            if (bufferObject instanceof String) {
                targetTriggerId = (String) bufferObject;
            }
            bufferObject = actionBuffer.get("TargetTriggerState");
            if (bufferObject instanceof Boolean) {
                targetTriggerStarting = (boolean) bufferObject;
            }
        }

        @Override
        public JSONObject jsonSerialize() throws JsonSerializedError {

            JSONObject actionBuffer = super.jsonSerialize();

            actionBuffer.put("TargetTriggerId", targetTriggerId);
            actionBuffer.put("TargetTriggerState", targetTriggerStarting);

            return actionBuffer;
        }

        @Override
        public void synchronize(long loopsPerSec) {

            if (targetTrigger != null) {
                targetTrigger.starting = targetTriggerStarting;
            }
        }

        @Override
        void initialize() {

            targetTrigger = Trigger.this.automator.getTriggerById(targetTriggerId);
        }
    }

    /**
     * This action will force a trigger to be either READY or ASLEEP.
    class OpaqueApparatusAction extends Action {

        final static String TYPE = "OPAQUE_APPARATUS";

        String deviceTag = "TRG";

        boolean state = false;

        OpaqueApparatusAction() {

            super();
        }

        @Override
        public void jsonDeserialize(@NotNull JSONObject jsonObject) {

        }

        @Override
        public JSONObject jsonSerialize() {
            return null;
        }

        @Override
        public void synchronize(long loopsPerSec) {

            Device targetDevice = Trigger.this.automator.getDeviceById(this.deviceTag);

            if (targetDevice instanceof OpaqueApparatus) {
                OpaqueApparatus opaqueApparatus = (OpaqueApparatus) targetDevice;
                //opaqueApparatus.setState(this.);
            }
        }
    }
     */
}
