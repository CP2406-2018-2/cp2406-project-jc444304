// Author: Yvan Burrie

package SmartHome;

/**
 * An apparatus is any kind of thing that has a behavior in the household.
 */
interface Apparatus {

}

/**
 * Passively behaves as an input, for example, a motion sensor.
 */
interface DetectableApparatus extends Apparatus {

    /**
     * @return Determines whether the apparatus detected something.
     */
    boolean isDetected();

    /**
     * @throws UnsupportedOperationException For Scenario use only as you cannot force detection unless using Simulator.
     * @param detected Determines if the apparatus successfully set its passive state.
     */
    void setDetected(boolean detected) throws UnsupportedOperationException;
}

/**
 * An opaque apparatus has a binary state of behavior, for example, some lights can only be on or off.
 */
interface OpaqueApparatus extends Apparatus {

    /**
     * Retrieves whether the apparatus is on or off.
     */
    boolean getState();

    /**
     * Assigns whether the apparatus is on or off.
     */
    void setState(boolean state);
}

/**
 * A gradient apparatus has a ranging state of behaviors, for example, a fan could have 5 speeds.
 */
interface GradientApparatus extends OpaqueApparatus {

    /**
     * Retrieves the specific value of this apparatus.
     */
    double getRange();

    /**
     * Assigns the specific value to this apparatus.
     */
    void setRange(double range);
}

/**
 * A functional apparatus has various stages in which it behaves, for example, a clothes-washer might be soaking, washing, rinsing, or spinning.
 */
interface FunctionalApparatus<T> extends Apparatus {

    T getFunction();

    void setFunction(T function);
}

/**
 * Any apparatus equipped with a temperature sensor should implement this interface.
 */
interface TemperatureControlApparatus {

    int getControlledTemperature();

    void setControlledTemperature(int temperature);

    int increaseControlledTemperature();

    int decreaseControlledTemperature();
}

/**
 * Any apparatus equipped with a temperature sensor should implement this interface.
 */
interface TemperatureSensorApparatus {

    double getSensoredTemperature();
}

interface HumiditySensorApparatus extends Apparatus {

}

class ShadeDevice {

}

class SirenDevice {

}

class CoffeeMachineDevice {

}

class HeaterDevcie {

}

class SmokeDetectorDevice {

}

class GasValveDevice {

}

class MicrowaveDevice {

}

class CookingHoodDevice {

}

class ClothesDryerDevice {

}

class RainDetectorDevcie {

}

class SoilMoistureSensorDevice {

}

class CarbonMonoxideSensor {

}

class PowerPlugDevice {

}
