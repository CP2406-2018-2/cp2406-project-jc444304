// Author: Yvan Burrie

package SmartHome;

import java.util.ArrayList;
import org.json.simple.*;

/**
 * Each trigger acts like a logical-gateway where events are tried on a thread and fire actions if successful.
 */
class Trigger extends Asset {

    /**
     * The user should add a description to the trigger to remember its behavior.
     */
    String description;

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

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    /**
     * When a trigger is looping, this means it will not be disabled after it is successful and the thread will try it again.
     */
    private boolean looping = false;

    public boolean isLooping() { return looping; }

    public void setLooping(boolean looping) { this.looping = looping; }

    /**
     * Events in each trigger are tried for checking whether the trigger can fire actions.
     */
    abstract protected class Event implements JsonDeserializable, Synchronizable {

        protected Trigger trigger;

        final static String TYPE = "DEFAULT";

        /**
         * Determines whether the event was successful upon trying.
         */
        protected boolean successful = false;

        /**
         * All events within a trigger can be AND'ED or OR'ED.
         */
        protected boolean orWithPrevious = false;

        public Event(Trigger trigger) {

            this.trigger = trigger;
        }

        @Override
        public JSONObject jsonSerialize() {
            return null;
        }

        @Override
        public void jsonDeserialize(JSONObject jsonObject) {

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

    abstract protected class Action implements JsonDeserializable, Synchronizable {

        final public static String TYPE = "DEFAULT";
    }



    }

    private ArrayList<Action> actions = new ArrayList<>();

    @Override
    public JSONObject jsonSerialize() {
        return null;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

    }

    public void synchronize(long loopsPerSecond) {

        /* Check if this trigger is eligable to run: */
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

    Trigger(Automator automator) {
        super(automator);
    }
}
