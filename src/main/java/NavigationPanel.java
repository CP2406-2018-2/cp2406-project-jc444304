// Author: Yvan Burrie

import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public class NavigationPanel extends JPanel implements ActionListener {

    private MainFrame mainFrame;

    private Automator automator;

    private EntitySelectionList<Venue> venuesSelector = new EntitySelectionList<>();

    private JButton venueCreateButton = new JButton("+");
    private JButton venueEditButton = new JButton(">");
    private JButton venueRemoveButton = new JButton("X");

    private EntitySelectionList<Device> deviceSelector = new EntitySelectionList<>();

    private JButton deviceCreateButton = new JButton("+");
    private JButton deviceEditButton = new JButton(">");
    private JButton deviceRemoveButton = new JButton("X");

    private EntitySelectionList<Fixture> fixturesSelector = new EntitySelectionList<>();

    private JButton fixtureCreateButton = new JButton("+");
    private JButton fixtureEditButton = new JButton(">");
    private JButton fixtureRemoveButton = new JButton("X");

    private EntitySelectionList<Trigger> triggersSelector = new EntitySelectionList<>();

    private JButton triggerCreateButton = new JButton("+");
    private JButton triggerEditButton = new JButton(">");
    private JButton triggerRemoveButton = new JButton("X");

    public NavigationPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        venueCreateButton.setToolTipText("Create a new Venue.");
        venueCreateButton.addActionListener(this);

        venueEditButton.setToolTipText("Edit the selected Venue.");
        venueEditButton.addActionListener(this);

        venueRemoveButton.setToolTipText("Remove any selected Venue(s).");
        venueRemoveButton.addActionListener(this);

        deviceCreateButton.setToolTipText("Create a new Device.");
        deviceCreateButton.addActionListener(this);

        deviceEditButton.setToolTipText("Edit the selected Device.");
        deviceEditButton.addActionListener(this);

        deviceRemoveButton.setToolTipText("Remove any selected Device(s).");
        deviceRemoveButton.addActionListener(this);

        fixtureCreateButton.setToolTipText("Create a new Fixture.");
        fixtureCreateButton.addActionListener(this);

        fixtureEditButton.setToolTipText("Edit the selected Fixture.");
        fixtureEditButton.addActionListener(this);

        fixtureRemoveButton.setToolTipText("Remove any selected Fixture(s).");
        fixtureRemoveButton.addActionListener(this);

        triggerCreateButton.setToolTipText("Create a new Trigger.");
        triggerCreateButton.addActionListener(this);

        triggerEditButton.setToolTipText("Edit the selected Trigger.");
        triggerEditButton.addActionListener(this);

        triggerRemoveButton.setToolTipText("Remove any selected Trigger(s).");
        triggerRemoveButton.addActionListener(this);

        setupComponents();
    }

    @Override
    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);

        venuesSelector.setEnabled(enabled);
        venueCreateButton.setEnabled(enabled);
        venueEditButton.setEnabled(enabled);
        venueRemoveButton.setEnabled(enabled);

        deviceSelector.setEnabled(enabled);
        deviceCreateButton.setEnabled(enabled);
        deviceEditButton.setEnabled(enabled);
        deviceRemoveButton.setEnabled(enabled);

        triggersSelector.setEnabled(enabled);
        triggerCreateButton.setEnabled(enabled);
        triggerEditButton.setEnabled(enabled);
        triggerRemoveButton.setEnabled(enabled);

        fixturesSelector.setEnabled(enabled);
        fixtureCreateButton.setEnabled(enabled);
        fixtureEditButton.setEnabled(enabled);
        fixtureRemoveButton.setEnabled(enabled);
    }

    void setupComponents() {

        add(new JLabel("Venues:"));
        add(venuesSelector);
        add(venueCreateButton);
        add(venueEditButton);
        add(venueRemoveButton);

        add(new JLabel("Devices:"));
        add(deviceSelector);
        add(deviceCreateButton);
        add(deviceEditButton);
        add(deviceRemoveButton);

        add(new JLabel("Triggers:"));
        add(triggersSelector);
        add(triggerCreateButton);
        add(triggerEditButton);
        add(triggerRemoveButton);

        add(new JLabel("Fixtures:"));
        add(fixturesSelector);
        add(fixtureCreateButton);
        add(fixtureEditButton);
        add(fixtureRemoveButton);
    }

    public void initialize(Automator automator) {

        this.automator = automator;

        updateVenuesSelector();
        updateDeviceSelector();
        updateFixturesSelector();
        updateTriggersSelector();
    }

    private void updateVenuesSelector() {

        if (automator != null) {
            venuesSelector.initialize(automator.getVenues());
        }
    }

    private void updateDeviceSelector() {

        if (automator != null) {
            deviceSelector.initialize(automator.getDevices());
        }
    }

    private void updateFixturesSelector() {

        if (automator != null) {
            fixturesSelector.initialize(automator.getFixtures());
        }
    }

    private void updateTriggersSelector() {

        if (automator != null) {
            triggersSelector.initialize(automator.getTriggers());
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        Object actionEventSource = actionEvent.getSource();

        if (actionEventSource == venueCreateButton) {
            handleCreateVenue();
            return;
        }
        if (actionEventSource == venueEditButton) {
            handleEditVenue();
            return;
        }
        if (actionEventSource == venueRemoveButton) {
            handleRemoveVenue();
            return;
        }
        if (actionEventSource == deviceCreateButton) {
            handleCreateDevice();
            return;
        }
        if (actionEventSource == deviceEditButton) {
            handleEditDevice();
            return;
        }
        if (actionEventSource == deviceRemoveButton) {
            handleRemoveDevice();
            return;
        }
        if (actionEventSource == fixtureCreateButton) {
            handleCreateFixture();
            return;
        }
        if (actionEventSource == fixtureEditButton) {
            handleEditFixture();
            return;
        }
        if (actionEventSource == fixtureRemoveButton) {
            handleRemoveFixture();
            return;
        }
        if (actionEventSource == triggerCreateButton) {
            handleCreateTrigger();
            return;
        }
        if (actionEventSource == triggerEditButton) {
            handleEditTrigger();
            return;
        }
        if (actionEventSource == triggerRemoveButton) {
            handleRemoveTrigger();
            return;
        }
    }

    boolean handleCreateVenue() {

        return true;
    }

    boolean handleEditVenue() {

        return true;
    }

    boolean handleRemoveVenue() {

        return true;
    }

    boolean handleCreateDevice() {

        return true;
    }

    boolean handleEditDevice() {

        return true;
    }

    boolean handleRemoveDevice() {

        return true;
    }

    boolean handleCreateFixture() {

        return true;
    }

    boolean handleEditFixture() {

        return true;
    }

    boolean handleRemoveFixture() {

        return true;
    }

    boolean handleCreateTrigger() {

        return true;
    }

    boolean handleEditTrigger() {

        return true;
    }

    boolean handleShowTriggerEditor(Trigger trigger) {

        return true;
    }

    boolean handleAppliedTriggerEditor() {

        return true;
    }

    boolean handleRemoveTrigger() {

        return true;
    }
}
