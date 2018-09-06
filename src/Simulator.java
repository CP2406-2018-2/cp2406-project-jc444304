/*
 * @author Yvan Burrie
 */

import org.json.simple.JSONObject;

/**
 * The simulation system hijacks the automation system to alter its state of reality.
 */
final public class Simulator extends Automator {

    /**
     *
     */
    protected Scenario[] scenarios;

    /**
     *
     */
    public int numScenarios() {
        return this.scenarios.length;
    }

    public Scenario getScenario(int scenarioId) {
        return this.scenarios[scenarioId];
    }

    public void setScenario(int scenarioId, Scenario scenario) {
        this.scenarios[scenarioId] = scenario;
    }

    public Simulator() {

    }

    @Override
    public void jsonDeserialize(JSONObject jsonObject) {

        super.jsonDeserialize(jsonObject);

        Object bufferObject;


    }
}

/**
 * A scenario consists of customizable consequences injected into the automation system.
 */
abstract class Scenario {

}
