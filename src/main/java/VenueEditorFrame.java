// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
import java.awt.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
class VenueEditorFrame extends EntityEditorFrame {

    private NavigationPanel navigationPanel;

    private Venue venue;

    private JCheckBox outdoorCheck = new JCheckBox("Outdoor");

    VenueEditorFrame(@NotNull NavigationPanel navigationPanel, @NotNull Venue venue) {

        super(venue);

        this.navigationPanel = navigationPanel;
        this.venue = venue;
    }

    @Override
    void setupComponents() {

        super.setupComponents();

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(outdoorCheck);
        add(panel);

        pack();
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

        navigationPanel.handleAppliedVenueEditor();

        return super.handleApply();
    }
}
