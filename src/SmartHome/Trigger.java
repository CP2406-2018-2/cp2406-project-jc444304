// Author: Yvan Burrie

package SmartHome;

import org.json.simple.JSONObject;
import java.util.ArrayList;

/**
 * Each trigger acts like a logical-gateway where events are tried on a thread and fire actions if successful.
 */
public class Trigger implements JsonDeserializable, JsonSerializable, Synchronizable {

    private Automator automator;

    public Automator getAutomator() { return automator; }

    /**
     * The user should provide a name to each trigger to identify them.
     */
    private String name = "Un-named Trigger";

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    /**
     * The user should add a description to the trigger to remember its behavior.
     */
    private String description = "";

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public enum Status {
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
    abstract protected class Event implements JsonDeserializable, JsonSerializable, Synchronizable {

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

    abstract protected class Action implements Synchronizable {

    }

    private ArrayList<Action> actions = new ArrayList<>();

    public Trigger(Automator automator) {

        this.automator = automator;
    }

    @Override
    public JSONObject jsonSerialize() {
        return null;
    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

    }

    public void synchronize(long loopsPerSecond) {

        if (this.status != Status.READY) return;

        this.status = Status.RUNNING;

        boolean eventsResult = false;

        /* Try out each event: */
        for (int i = 0; i < this.events.size(); i++) {
            Event event = this.events.get(i);
            event.synchronize(loopsPerSecond);
            if (event.orWithPrevious) {
                eventsResult |= event.successful;
            } else {
                eventsResult &= event.successful;
            }
            if (!eventsResult) {
                this.status = Status.TRIED;
                return;
            }
        }

        /* Fire all actions: */
        for (int i = 0; i < this.actions.size(); i++) {
            Action action = this.actions.get(i);
            action.synchronize(loopsPerSecond);
        }

        this.status = Status.DONE;
    }
}
