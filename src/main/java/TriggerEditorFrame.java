// Author: Yvan Burrie

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import SmartHome.*;
import com.sun.istack.internal.NotNull;

/**
 *
 */
class TriggerEditorFrame extends EntityEditorFrame<Trigger> {

    private NavigationPanel navigationPanel;

    private Trigger trigger;

    private JCheckBox startingCheck = new JCheckBox("Starting");
    private JCheckBox loopingCheck = new JCheckBox("Looping");

    private EntitySelectionList<Trigger.Event> eventsSelector = new EntitySelectionList<>();

    private ComboBoxWrapper<String> eventTypesSelector = new ComboBoxWrapper<>();

    private JButton actionCreateButton = new JButton("+");
    private JButton actionEditButton = new JButton(">");
    private JButton actionRemoveButton = new JButton("X");

    private EntitySelectionList<Trigger.Action> actionsSelector = new EntitySelectionList<>();

    private ComboBoxWrapper<String> actionTypesSelector = new ComboBoxWrapper<>();

    private JButton eventCreateButton = new JButton("+");
    private JButton eventEditButton = new JButton(">");
    private JButton eventRemoveButton = new JButton("X");

    TriggerEditorFrame(@NotNull NavigationPanel navigationPanel, @NotNull Trigger trigger) {

        super(trigger);

        this.navigationPanel = navigationPanel;
        this.trigger = trigger;

        eventTypesSelector.setToolTipText("Choose Event type to create.");
        eventTypesSelector.addItem(Trigger.ApparatusDetectionEvent.class.toString(), MainFrame.getReadableTypes());

        eventCreateButton.setToolTipText("Create a new Event.");
        eventCreateButton.addActionListener(this);

        eventEditButton.setToolTipText("Edit selected Event.");
        eventEditButton.addActionListener(this);

        eventRemoveButton.setToolTipText("Remove selected Events(s).");
        eventRemoveButton.addActionListener(this);

        actionTypesSelector.setToolTipText("Choose Action type to create.");
        eventTypesSelector.addItem(Trigger.ChangeTriggerStartingAction.class.toString(), MainFrame.getReadableTypes());

        actionCreateButton.setToolTipText("Create a new Action.");
        actionCreateButton.addActionListener(this);

        actionEditButton.setToolTipText("Edit selected Action.");
        actionEditButton.addActionListener(this);

        actionRemoveButton.setToolTipText("Remove selected Actions(s).");
        actionRemoveButton.addActionListener(this);
    }

    @Override
    void update() {

        super.update();

        updateStarting();
        updateLooping();
        updateEventsSelector();
        updateActionsSelector();
    }

    private void updateStarting() {

        startingCheck.setSelected(trigger.isStarting());
    }

    private void updateLooping() {

        loopingCheck.setSelected(trigger.isLooping());
    }

    private void updateEventsSelector() {

        eventsSelector.initialize(trigger.getEvents());
    }

    private void updateActionsSelector() {

        actionsSelector.initialize(trigger.getActions());
    }

    @Override
    void setupComponents() {

        super.setupComponents();

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(startingCheck);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(loopingCheck);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Events:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(eventsSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(eventTypesSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(eventCreateButton);
        panel.add(eventEditButton);
        panel.add(eventRemoveButton);
        add(panel);

        add(new JLabel("Actions:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(actionsSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(actionTypesSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(actionCreateButton);
        panel.add(actionEditButton);
        panel.add(actionRemoveButton);
        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        
        Object actionEventSource = actionEvent.getSource();
        
        if (actionEventSource == eventCreateButton) {
            handleCreateEvent();
            return;
        }
        if (actionEventSource == eventEditButton) {
            handleEditEvent();
            return;
        }
        if (actionEventSource == eventRemoveButton) {
            handleRemoveEvent();
            return;
        }
        if (actionEventSource == actionCreateButton) {
            handleCreateAction();
            return;
        }
        if (actionEventSource == actionEditButton) {
            handleEditAction();
            return;
        }
        if (actionEventSource == actionRemoveButton) {
            handleRemoveAction();
            return;
        }
        super.actionPerformed(actionEvent);
    }

    @Override
    boolean handleApply() {

        trigger.setStarting(startingCheck.isSelected());
        updateStarting();

        trigger.setLooping(loopingCheck.isSelected());
        updateLooping();

        navigationPanel.handleAppliedTriggerEditor();

        return super.handleApply();
    }

    private void handleCreateEvent() {

        Trigger.Event triggerEvent;

        if (eventTypesSelector.equals(Trigger.ChangeTriggerStartingAction.class.toString())) {
            handleCreateEvent(trigger.new ApparatusDetectionEvent());
            return;
        }
        // TODO: more events
    }

    private void handleCreateEvent(@NotNull Trigger.Event triggerEvent) {

        trigger.getEvents().add(triggerEvent);
        handleShowEventEditor(triggerEvent);
        updateEventsSelector();
    }

    private void handleEditEvent() {

        Trigger.Event triggerEvent = eventsSelector.getSelectedEntity();
        if (triggerEvent == null) {
            return;
        }
        handleShowEventEditor(triggerEvent);
    }

    void handleShowEventEditor(Trigger.Event triggerEvent) {

        TriggerEventEditorFrame triggerEventEditor = new TriggerEventEditorFrame(triggerEvent);
        triggerEventEditor.setVisible(true);
    }

    boolean handleRemoveEvent() {

        for (Trigger.Event triggerEvent : eventsSelector.getSelectedEntities()) {
            trigger.getEvents().remove(triggerEvent);
        }
        updateEventsSelector();

        return true;
    }

    private void handleCreateAction() {

        String actionTypeSelected = actionTypesSelector.getSelectedKey();

        if (actionTypeSelected.equals(Trigger.ChangeTriggerStartingAction.class.toString())) {
            handleCreateAction(trigger.new ChangeTriggerStartingAction());
            return;
        }
        // TODO: more actions
    }

    private void handleCreateAction(@NotNull Trigger.Action triggerAction) {

        trigger.getActions().add(triggerAction);
        handleShowActionEditor(triggerAction);
    }

    private void handleEditAction() {

        Trigger.Action triggerAction = actionsSelector.getSelectedEntity();
        if (triggerAction == null) {
            return;
        }
        handleShowActionEditor(triggerAction);

    }

    private void handleShowActionEditor(@NotNull Trigger.Action triggerAction) {

        TriggerActionEditorFrame triggerActionEditor = new TriggerActionEditorFrame(triggerAction);
        triggerActionEditor.setVisible(true);
    }

    private void handleRemoveAction() {

        for (Trigger.Action triggerAction : actionsSelector.getSelectedEntities()) {
            trigger.getActions().remove(triggerAction);
        }
        updateEventsSelector();
    }
}
