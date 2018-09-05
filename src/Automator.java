// Author: Yvan Burrie

import java.util.ArrayList;

/**
 * 
 */
public class Automator implements Synchronyzable {

    /**
     *
     */
    abstract class Venue {

        protected String name = "Un-named Venue";

        public String getName() { return this.name; }

        public void setName(String name) { this.name = name; }
    }

    /**
     *
     */
    class OutdoorVenue extends Venue {

    }

    /**
     *
     */
    class IndoorVenue extends Venue {

    }

    /**
     *
     */
    ArrayList<Venue> venues;

    /**
     *
     */
    ArrayList<Apparatus> apparatuses;

    /**
     * Points to the synchronization thread.
     */
    Synchronizer synchronizer;

    public Synchronizer getSynchronizer() { return synchronizer; }

    public void synchronize(long loopsPerSecond) {

        return;
    }
}
