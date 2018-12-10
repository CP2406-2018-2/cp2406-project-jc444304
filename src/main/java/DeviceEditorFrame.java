// Author: Yvan Burrie

import java.awt.*;
import javax.swing.*;
import SmartHome.*;
import com.sun.istack.internal.NotNull;

/**
 *
 */
class DeviceEditorFrame extends EntityEditorFrame<Device> {

    private NavigationPanel navigationPanel;

    private Device device;

    private EntitySelectionList<Venue> venueSelector = new EntitySelectionList<>();

    DeviceEditorFrame(@NotNull NavigationPanel navigationPanel, @NotNull Device device) {

        super(device);

        this.navigationPanel = navigationPanel;
        this.device = device;
    }

    @Override
    void setupComponents() {

        super.setupComponents();

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Venue:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(venueSelector);
        add(panel);
    }

    @Override
    boolean handleApply() {

        Venue selectedVenue = venueSelector.getSelectedEntity();
        if (selectedVenue != null && selectedVenue != device.getVenue()) {
            device.setVenue(selectedVenue);
            navigationPanel.mainFrame.handleProjectChanged();
        }

        navigationPanel.handleAppliedDeviceEditor();

        return super.handleApply();
    }
}
