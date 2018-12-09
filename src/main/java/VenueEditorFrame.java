// Author: Yvan Burrie

import javax.swing.*;

import SmartHome.*;

/**
 *
 */
public class VenueEditorFrame extends EntityEditorFrame {

    private Venue venue;

    private JCheckBox outdoorCheck = new JCheckBox("Outdoor");

    public VenueEditorFrame(Venue venue) {

        super(venue);

        this.venue = venue;
    }

    @Override
    void setupComponents() {

        super.setupComponents();

        add(outdoorCheck);
    }

    @Override
    void update() {

        super.update();

        updateOutdoorCheck();
    }

    private void updateOutdoorCheck() {

        outdoorCheck.setSelected(venue.isOutdoor());
    }

    @Override
    boolean handleApply() {

        venue.setOutdoor(outdoorCheck.isSelected());
        updateOutdoorCheck();

        return super.handleApply();
    }
}
