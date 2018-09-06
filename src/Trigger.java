// Author: Yvan Burrie

import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Trigger implements JsonDeserializable {

    private Automator automator;

    public Automator getAutomator() { return automator; }

    private String name = "Un-named Trigger";

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    private String description = "";

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public enum Status {
        ASLEEP,
        READY,
        DONE,
    }

    private Status status = Status.ASLEEP;

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    private boolean looping = false;

    public boolean isLooping() { return looping; }

    public void setLooping(boolean looping) { this.looping = looping; }

    abstract protected class Event implements JsonDeserializable {

        protected Trigger trigger;

        final static String jsonMacro = "DEFAULT";

        protected boolean successful = false;

        public boolean isSuccessful() { return successful; }

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

    public Trigger(Automator automator) {
    
        this.automator = automator;
    }

    private ArrayList<Event> events;

    class Action {

    }

    private ArrayList<Action> actions;

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
}
