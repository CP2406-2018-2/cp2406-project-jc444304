// Author: Yvan Burrie

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import SmartHome.*;
import com.sun.istack.internal.NotNull;

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

    private EntitySelectionList<Device> devicesSelector = new EntitySelectionList<>();

    private ComboBoxWrapper<String> deviceTypesSelector = new ComboBoxWrapper<>();

    private JButton deviceCreateButton = new JButton("+");
    private JButton deviceEditButton = new JButton(">");
    private JButton deviceRemoveButton = new JButton("X");

    private EntitySelectionList<Fixture> fixturesSelector = new EntitySelectionList<>();

    private ComboBoxWrapper<String> fixtureTypesSelector = new ComboBoxWrapper<>();

    private JButton fixtureCreateButton = new JButton("+");
    private JButton fixtureEditButton = new JButton(">");
    private JButton fixtureRemoveButton = new JButton("X");

    private EntitySelectionList<Trigger> triggersSelector = new EntitySelectionList<>();

    private JButton triggerCreateButton = new JButton("+");
    private JButton triggerEditButton = new JButton(">");
    private JButton triggerRemoveButton = new JButton("X");

    NavigationPanel(@NotNull MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        venueCreateButton.setToolTipText("Create a new Venue.");
        venueCreateButton.addActionListener(this);

        venueEditButton.setToolTipText("Edit the selected Venue.");
        venueEditButton.addActionListener(this);

        venueRemoveButton.setToolTipText("Remove any selected Venue(s).");
        venueRemoveButton.addActionListener(this);

        deviceTypesSelector.setToolTipText("Choose the type of Device to create.");
        deviceTypesSelector.addItem(RefrigeratorDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(LightDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(VentilatorDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(AirConditionerDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(SprinklerDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(DoorDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(RollerDoorDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(OvenDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(MotionSensorDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(WindowDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(ClothesWasherDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(DishWasherDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(StoveDevice.class.toString(), MainFrame.getReadableTypes());
        deviceTypesSelector.addItem(ThermostatDevice.class.toString(), MainFrame.getReadableTypes());

        deviceCreateButton.setToolTipText("Create a new Device.");
        deviceCreateButton.addActionListener(this);

        deviceEditButton.setToolTipText("Edit the selected Device.");
        deviceEditButton.addActionListener(this);

        deviceRemoveButton.setToolTipText("Remove any selected Device(s).");
        deviceRemoveButton.addActionListener(this);

        fixtureTypesSelector.setToolTipText("Choose the type of Fixture to create.");
        fixtureTypesSelector.addItem(WallFixture.class.toString(), MainFrame.getReadableTypes());
        fixtureTypesSelector.addItem(BenchFixture.class.toString(), MainFrame.getReadableTypes());
        fixtureTypesSelector.addItem(WindowFixture.class.toString(), MainFrame.getReadableTypes());
        fixtureTypesSelector.addItem(DoorFixture.class.toString(), MainFrame.getReadableTypes());

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

        devicesSelector.setEnabled(enabled);
        deviceTypesSelector.setEnabled(enabled);
        deviceCreateButton.setEnabled(enabled);
        deviceEditButton.setEnabled(enabled);
        deviceRemoveButton.setEnabled(enabled);

        triggersSelector.setEnabled(enabled);
        triggerCreateButton.setEnabled(enabled);
        triggerEditButton.setEnabled(enabled);
        triggerRemoveButton.setEnabled(enabled);

        fixturesSelector.setEnabled(enabled);
        fixtureTypesSelector.setEnabled(enabled);
        fixtureCreateButton.setEnabled(enabled);
        fixtureEditButton.setEnabled(enabled);
        fixtureRemoveButton.setEnabled(enabled);
    }

    void setupComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Venues:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JScrollPane(venuesSelector));
        venuesSelector.setPreferredSize(new Dimension(150, 200));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(venueCreateButton);
        panel.add(venueEditButton);
        panel.add(venueRemoveButton);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Devices:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JScrollPane(devicesSelector));
        devicesSelector.setPreferredSize(new Dimension(150, 200));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(deviceTypesSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(deviceCreateButton);
        panel.add(deviceEditButton);
        panel.add(deviceRemoveButton);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Triggers:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JScrollPane(triggersSelector));
        triggersSelector.setPreferredSize(new Dimension(150, 200));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(triggerCreateButton);
        panel.add(triggerEditButton);
        panel.add(triggerRemoveButton);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Fixtures:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JScrollPane(fixturesSelector));
        fixturesSelector.setPreferredSize(new Dimension(150, 200));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(fixtureTypesSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(fixtureCreateButton);
        panel.add(fixtureEditButton);
        panel.add(fixtureRemoveButton);
        add(panel);
    }

    void initialize(@NotNull Automator automator) {

        this.automator = automator;

        updateVenuesSelector();
        updateDevicesSelector();
        updateFixturesSelector();
        updateTriggersSelector();
    }

    private void updateVenuesSelector() {

        if (automator != null) {
            venuesSelector.initialize(automator.getVenues());
        }
    }

    private void updateDevicesSelector() {

        if (automator != null) {
            devicesSelector.initialize(automator.getDevices());
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

    private boolean handleCreateVenue() {

        return true;
    }

    private boolean handleEditVenue() {

        return true;
    }

    private boolean handleRemoveVenue() {

        return true;
    }

    private boolean handleCreateDevice() {

        if (automator == null) {
            return false;
        }
        String selectedDeviceType = deviceTypesSelector.getSelectedKey();
        return false;
    }

    private boolean handleCreateDevice(Device device) {

        device.setId("DEVICE_" + automator.getFixtures().size());
        device.setName("New Device");
        automator.getDevices().add(devicesSelector.getSelectedIndex() + 1, device);
        updateDevicesSelector();
        handleShowDeviceEditor(device);

        return true;
    }

    private boolean handleEditDevice() {

        handleShowDeviceEditor(devicesSelector.getSelectedEntity());

        return true;
    }

    private boolean handleShowDeviceEditor(Device device) {

        DeviceEditorFrame deviceEditor = new DeviceEditorFrame(this, device);
        deviceEditor.initialize();
        deviceEditor.setVisible(true);

        return true;
    }

    void handleAppliedDeviceEditor() {

        updateDevicesSelector();
        mainFrame.handleProjectChanged();
        mainFrame.setStatusText("Device changed...");
    }

    private boolean handleRemoveDevice() {

        if (automator == null) {
            return false;
        }
        ArrayList<Device> selectedDevices = devicesSelector.getSelectedEntities();
        if (selectedDevices.size() > 0) {
            int dialogResult = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove (" + selectedDevices.size() + ") Device(s).",
                    "Remove Points",
                    JOptionPane.YES_NO_OPTION);
            switch (dialogResult) {
                case JOptionPane.YES_OPTION:
                    for (Device device : devicesSelector.getSelectedEntities()) {
                        automator.getDevices().remove(device);
                    }
                    updateDevicesSelector();
                    return true;
            }
        }
        return false;
    }

    private boolean handleCreateFixture() {

        if (automator == null) {
            return false;
        }
        String selectedFixtureType = fixtureTypesSelector.getSelectedKey();

        if (selectedFixtureType.equals(WallFixture.class.getName())) {
            return handleCreateFixture(new WallFixture(automator));
        }
        if (selectedFixtureType.equals(BenchFixture.class.getName())) {
            return handleCreateFixture(new BenchFixture(automator));
        }
        return false;
    }

    private boolean handleCreateFixture(@NotNull Fixture fixture) {

        fixture.setId("FIXTURE_" + automator.getFixtures().size());
        fixture.setName("New Fixture");
        automator.getFixtures().add(fixturesSelector.getSelectedIndex() + 1, fixture);
        updateFixturesSelector();
        handleShowFixtureEditor(fixture);

        return true;
    }

    private boolean handleEditFixture() {

        handleShowFixtureEditor(fixturesSelector.getSelectedEntity());

        return true;
    }

    private boolean handleShowFixtureEditor(Fixture fixture) {

        if (fixture == null) {
            return false;
        }
        FixtureEditorFrame fixtureEditor = new FixtureEditorFrame(this, fixture);
        fixtureEditor.initialize();
        fixtureEditor.setVisible(true);
        return true;
    }

    private boolean handleRemoveFixture() {

        if (automator == null) {
            return false;
        }
        ArrayList<Fixture> selectedFixtures = fixturesSelector.getSelectedEntities();
        if (selectedFixtures.size() <= 0) {
            return false;
        }
        int dialogResult = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove (" + selectedFixtures.size() + ") Fixture(s).",
                "Remove Points",
                JOptionPane.YES_NO_OPTION);
        switch (dialogResult) {
            case JOptionPane.YES_OPTION:
                for (Fixture fixture : fixturesSelector.getSelectedEntities()) {
                    automator.getFixtures().remove(fixture);
                }
                updateFixturesSelector();
                return true;
            default:
                return false;
        }
    }

    private boolean handleCreateTrigger() {

        if (automator == null) {
            return false;
        }
        Trigger trigger = new Trigger(automator);
        trigger.setId("TRIGGER_" + automator.getTriggers().size());
        trigger.setName("New Trigger");
        automator.getTriggers().add(triggersSelector.getSelectedIndex() + 1, trigger);
        updateTriggersSelector();
        handleShowTriggerEditor(trigger);

        return true;
    }

    private boolean handleEditTrigger() {

        handleShowTriggerEditor(triggersSelector.getSelectedEntity());

        return true;
    }

    private boolean handleShowTriggerEditor(Trigger trigger) {

        if (trigger != null) {
            TriggerEditorFrame triggerEditor = new TriggerEditorFrame(this, trigger);
            triggerEditor.initialize();
            triggerEditor.setVisible(true);
        }
        return true;
    }

    void handleAppliedTriggerEditor() {

        updateTriggersSelector();
        mainFrame.handleProjectChanged();
        mainFrame.setStatusText("Trigger edited...");
    }

    private boolean handleRemoveTrigger() {

        if (automator == null) {
            return false;
        }
        ArrayList<Trigger> selectedTriggers = triggersSelector.getSelectedEntities();
        if (selectedTriggers.size() <= 0) {
            return false;
        }
        int dialogResult = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove (" + selectedTriggers.size() + ") Trigger(s).",
                "Remove Points",
                JOptionPane.YES_NO_OPTION);
        switch (dialogResult) {
            case JOptionPane.YES_OPTION:
                for (Trigger trigger : triggersSelector.getSelectedEntities()) {
                    automator.getTriggers().remove(trigger);
                }
                updateTriggersSelector();
                return true;
            default:
                return false;
        }
    }
}
