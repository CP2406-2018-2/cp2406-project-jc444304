
/**
 * An apparatus is any kind of appliance or fixture that has a behavior in the household.
 */
interface Apparatus {

    /**
     * 
     */
    enum Status {
        MALFUNCTIONING,
        UNRESPONSIVE,
        WORKING,
    }

    /**
     * Determines whether the apparatus is currently working.
     * If not, then it may be malfunctioning, accidentally unplugged or uncharged, etc.
     */
    Status getStatus();
}

class Refrigerator implements Apparatus {}
