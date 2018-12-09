// Author: Yvan Burrie

import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public class DeviceEditorFrame extends EntityEditorFrame<Device> {

    private Device device;

    private EntitySelectionList<Venue> venueSelector = new EntitySelectionList<>();

    public DeviceEditorFrame(Device device) {

        super(device);

        setSize(300, 500);
    }

    @Override
    void update() {

        super.update();

        device = (Device) entity;
    }

    @Override
    void setupComponents() {

        super.setupComponents();

        add(new JLabel("Venue:"));
        add(venueSelector);
    }

    @Override
    boolean handleApply() {

        Venue selectedVenue = venueSelector.getSelectedEntity();
        if (selectedVenue != null && selectedVenue != device.getVenue()) {
            device.setVenue(selectedVenue);
        }

        return super.handleApply();
    }
}
