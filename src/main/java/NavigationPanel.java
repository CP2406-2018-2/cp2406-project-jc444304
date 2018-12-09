// Author: Yvan Burrie

import java.awt.*;
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

    NavigationPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        deviceTypesSelector.setToolTipText("Choose the type of Device to create.");

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

        fixtureTypesSelector.setToolTipText("Choose the type of Fixture to create.");
        fixtureTypesSelector.addItem("Wall", WallFixture.class.getName());
        fixtureTypesSelector.addItem("Bench", BenchFixture.class.getName());

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
        panel.add(new JScrollPane(deviceSelector));
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

    boolean handleCreateFixture(Fixture fixture) {

        fixture.setId("FIXTURE_" + automator.getFixtures().size());
        fixture.setName("New Fixture");
        automator.getFixtures().add(fixturesSelector.getSelectedIndex() + 1, fixture);
        updateFixturesSelector();
        handleShowFixtureEditor(fixture);

        return true;
    }

    boolean handleEditFixture() {

        Fixture fixture = fixturesSelector.getSelectedEntity();
        handleShowFixtureEditor(fixture);

        return true;
    }

    boolean handleShowFixtureEditor(Fixture fixture) {

        FixtureEditorFrame fixtureEditor = new FixtureEditorFrame(this, fixture);
        fixtureEditor.initialize();
        fixtureEditor.setAlwaysOnTop(true);
        fixtureEditor.setVisible(true);

        return true;
    }

    boolean handleRemoveFixture() {

        if (automator == null) {
            return false;
        }

        for (Fixture fixture : fixturesSelector.getSelectedEntities()) {
            automator.getFixtures().remove(fixture);
        }
        updateFixturesSelector();

        return true;
    }

    boolean handleCreateTrigger() {

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

    boolean handleEditTrigger() {

        Trigger trigger = triggersSelector.getSelectedEntity();
        handleShowTriggerEditor(trigger);

        return true;
    }

    boolean handleShowTriggerEditor(Trigger trigger) {

        TriggerEditorFrame triggerEditor = new TriggerEditorFrame(this, trigger);
        triggerEditor.initialize();
        triggerEditor.setAlwaysOnTop(true);
        triggerEditor.setVisible(true);

        return true;
    }

    boolean handleAppliedTriggerEditor() {

        updateTriggersSelector();
        mainFrame.setStatusText("Trigger edited...");

        return true;
    }

    boolean handleRemoveTrigger() {

        if (automator == null) {
            return false;
        }

        for (Trigger trigger : triggersSelector.getSelectedEntities()) {
            automator.getTriggers().remove(trigger);
        }
        updateTriggersSelector();

        return true;
    }
}
