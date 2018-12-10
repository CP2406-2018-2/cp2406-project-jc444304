// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;
import org.json.simple.*;

import java.util.ArrayList;

/**
 *
 */
public class RefrigeratorDevice extends Device implements FunctionalApparatus<RefrigeratorDevice.Function> {

    final static String JSON_TYPE = "REFRIGERATOR";

    @Override
    public String getJsonType() {
        return JSON_TYPE;
    }

    enum Function {
        IDLE,
        COOLING,
        DEFROSTING
    }

    Function function = Function.IDLE;

    @Override
    public Function getFunction() {
        return function;
    }

    @Override
    public void setFunction(Function function) {
        this.function = function;
    }

    /**
     * A storage section of the refrigerator such as the freezer.
     */
    class Compartment extends Asset {

        /**
         * Specifies the number of cubic litres this refrigerator holds.
         */
        long capacity = 0;

        double maximumTemperature = 0.0;

        double currentTemperature = 0.0;

        /**
         * Determines whether the compressor is working to cool.
         */
        boolean cooling = false;

        /**
         * Determines whether the compressor is heating the internal layers.
         */
        boolean defrosting = false;

        /**
         * Specifies in radians the angle of the door opening.
         */
        double opened = 0.0;

        public boolean isOpen() {
            return opened <= 0;
        }

        /**
         * Specifies the last time the door was opened.
         */
        long lastOpened = 0;

        Compartment() {
            super(RefrigeratorDevice.this.automator);
        }

        public Compartment(JSONObject compartmentBuffer) {
            super(RefrigeratorDevice.this.automator);
            jsonDeserialize(compartmentBuffer);
        }

        @Override
        public void jsonDeserialize(JSONObject jsonObject) {

            Object objectBuffer;

            objectBuffer = jsonObject.get("Capacity");
            if (objectBuffer instanceof Integer) {
                capacity = (int) objectBuffer;
            }
            objectBuffer = jsonObject.get("MaximumTemperature");
            if (objectBuffer instanceof Double) {
                maximumTemperature = (double) objectBuffer;
            }
        }

        @Override
        public JSONObject jsonSerialize() {
            return null;
        }

        @Override
        public void synchronize(long loopsPerSecond) {

            /* The compressor should only be cooling if the temperature  */
            cooling = currentTemperature < maximumTemperature;
        }
    }

    private ArrayList<Compartment> compartments = new ArrayList<>();

    public RefrigeratorDevice(@NotNull Automator automator, @NotNull Venue venue) {
        super(automator, venue);
    }

    public RefrigeratorDevice(@NotNull Automator automator, @NotNull Venue venue, @NotNull JSONObject deviceBuffer) throws JsonDeserializedError {
        super(automator, venue);
        jsonDeserialize(deviceBuffer);
    }

    @Override
    public void jsonDeserialize(@NotNull JSONObject deviceBuffer) throws JsonDeserializedError {

        super.jsonDeserialize(deviceBuffer);

        Object objectBuffer;

        /* Deserialize Compartments: */
        objectBuffer = deviceBuffer.get("Compartments");
        if (objectBuffer instanceof JSONArray) {
            JSONArray compartmentsBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : compartmentsBuffer) {
                objectBuffer = elementBuffer;
                if (objectBuffer instanceof JSONObject) {
                    JSONObject compartmentBuffer = (JSONObject) objectBuffer;
                    Compartment compartment = new Compartment(compartmentBuffer);
                    compartments.add(compartment);
                }
            }
        }
    }

    @Override
    public void synchronize(long loopsPerSecond) {

        super.synchronize(loopsPerSecond);

        /* Synchronize Compartments: */
        for (Compartment compartment : compartments) {
            compartment.synchronize(loopsPerSecond);
        }
    }
}
