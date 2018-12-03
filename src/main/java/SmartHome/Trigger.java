// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;
import org.json.simple.*;

/**
 * Each trigger acts like a logical-gateway where events are tried on a thread and fire actions if successful.
 */
class Trigger extends Entity {

    /**
     * The user should add a description to the trigger to remember its behavior.
     */
    /**
     * When a trigger is looping, this means it will not be disabled after it is successful and the thread will try it again.
     */
    private boolean looping = false;

    enum Status {
        ASLEEP,
        READY,
        RUNNING,
        TRIED,
        DONE,
    }

    /**
     * When each trigger is tried by the thread, it should have a change in status.
     */
    private Status status = Status.ASLEEP;

    /**
     * Events in each trigger are tried for checking whether the trigger can fire actions.
     */
    abstract class Event extends Entity {

        /**
         * All events within a trigger can be AND'ED or OR'ED.
         */
        boolean orPrevious = false;

        /**
         * Determines whether the event was successful upon trying.
         */
        boolean successful = false;

        public Event(Automator automator) {
            super(automator);
        }

        public Event(Automator automator, JSONObject eventBuffer) throws JsonDeserializedError {
            super(automator);
            jsonDeserialize(eventBuffer);
        }

        @Override
            return null;
        public void jsonDeserialize(JSONObject eventBuffer) throws JsonDeserializedError {
        }

        @Override
        public JSONObject jsonSerialize() throws JsonSerializedError {

        }
    }

    /*
    protected class ApparatusDetectionEvent extends Event {

        final public static String JSON_MACRO = "APPARATUS_DETECTION";

        private Apparatus apparatus;

        private boolean detected = false;

        public boolean isDetected() { return this.detected; }

        public void setDetected(boolean detected) { this.detected = detected; }

        public void ApparatusDetectionEvent(Trigger trigger, Apparatus apparatus) {

            //super::Event(trigger);

            this.apparatus = apparatus;
        }

        public void ApparatusDetectionEvent(Trigger trigger, long apparatusId) {

            Apparatus apparatus = this.trigger.getAutomator().getApparatus(apparatusId);

            this.ApparatusDetectionEvent(trigger, apparatus);
        }
    }
    */

    private ArrayList<Event> events = new ArrayList<>();

    private ArrayList<Action> actions = new ArrayList<>();

    abstract class Action extends Entity {

        public Action(Automator automator) {
            super(automator);
        }
    }

    /**
     * This action will force a trigger to be either READY or ASLEEP.
     */
    class ChangeTriggerStatusAction extends Action {

        final static String TYPE = "CHANGE_TRIGGER_STATUS";

        String targetTriggerId;

        boolean targetTriggerState = false;

        public ChangeTriggerStatusAction() {
            super(Trigger.this.automator);
        }

        public ChangeTriggerStatusAction(JSONObject actionBuffer) throws JsonDeserializedError {
            super(Trigger.this.automator);
            jsonDeserialize(actionBuffer);
        }

        @Override
        public void jsonDeserialize(JSONObject actionBuffer) throws JsonDeserializedError {

            super.jsonDeserialize(actionBuffer);

            Object bufferObject;

            bufferObject = actionBuffer.get("TargetTriggerId");
            if (bufferObject instanceof String) {
                targetTriggerId = (String) bufferObject;
            }
            bufferObject = actionBuffer.get("TargetTriggerState");
            if (bufferObject instanceof Boolean) {
                targetTriggerState = (boolean) bufferObject;
            }
        }

        @Override
        public JSONObject jsonSerialize() throws JsonSerializedError {

            JSONObject actionBuffer = super.jsonSerialize();

            actionBuffer.put("TargetTriggerId", targetTriggerId);
            actionBuffer.put("TargetTriggerState", targetTriggerState);

            return actionBuffer;
        }

        @Override
        public void synchronize(long loopsPerSec) {

            Trigger targetTrigger = automator.getTriggerById(targetTriggerId);

            if (targetTrigger != null) {
                targetTrigger.status = targetTriggerState ? Status.READY : Status.ASLEEP;
            }
        }
    }

    /**
     * This action will force a trigger to be either READY or ASLEEP.
     */
    class OpaqueApparatusAction extends Action {

        final static String TYPE = "OPAQUE_APPARATUS";

        String deviceTag = "TRG";

        boolean state = false;

        OpaqueApparatusAction() {
            super(Trigger.this.automator);
        }

        @Override
        public void jsonDeserialize(JSONObject jsonObject) {

        }

        @Override
        public JSONObject jsonSerialize() {
            return null;
        }

        @Override
        public void synchronize(long loopsPerSec) {

            Device targetDevice = automator.getDeviceById(this.deviceTag);

            if (targetDevice instanceof OpaqueStateApparatus) {
                OpaqueStateApparatus opaqueApparatus = (OpaqueStateApparatus) targetDevice;
                //opaqueApparatus.setState(this.);
            }
        }
    }

    public Trigger(Automator automator) {
        super(automator);
    }

    public Trigger(Automator automator, JSONObject triggerBuffer) throws JsonDeserializedError {
        super(automator, triggerBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject triggerBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(triggerBuffer);
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject triggerBuffer = super.jsonSerialize();

        return triggerBuffer;
    }

    public void synchronize(long loopsPerSecond) {

        /* Check if this trigger is eligible to run: */
        if (this.status != Status.READY) return;

        /* This trigger is now considered running: */
        this.status = Status.RUNNING;

        /* If the trigger does not have events, the actions will be fired immediately: */
        if (!events.isEmpty()) {

            /* If the first event is AND'ED with its previous, then the initial result must be TRUE, vice versa: */
            boolean eventsResult = events.get(0).orWithPrevious;

            /* Try out each event: */
            for (Event event : this.events) {
                event.synchronize(loopsPerSecond);

                /* Either AND or OR the result with the previous event: */
                if (event.orWithPrevious) {
                    eventsResult |= event.successful;
                } else {
                    eventsResult &= event.successful;
                }

                /* As soon as the result fails, prevent actions from firing: */
                if (!eventsResult) {
                    this.status = Status.TRIED;
                    return;
                }
            }
        }

        /* Fire all actions: */
        for (Action action : this.actions) {
            action.synchronize(loopsPerSecond);
        }

        this.status = Status.DONE;
    }
}
