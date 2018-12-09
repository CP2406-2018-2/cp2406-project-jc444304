// Author: Yvan Burrie

import java.awt.event.ActionEvent;
import javax.swing.*;
import SmartHome.*;
import com.sun.istack.internal.NotNull;

/**
 *
 */
public class TriggerEditorFrame extends EntityEditorFrame<Trigger> {

    NavigationPanel navigationPanel;

    Trigger trigger;

    JCheckBox startingCheck = new JCheckBox("Starting");
    JCheckBox loopingCheck = new JCheckBox("Looping");

    EntitySelectionList<Trigger.Event> eventsSelector = new EntitySelectionList<>();

    private JButton actionCreateButton = new JButton("+");
    private JButton actionEditButton = new JButton(">");
    private JButton actionRemoveButton = new JButton("X");

    EntitySelectionList<Trigger.Action> actionsSelector = new EntitySelectionList<>();

    private JButton eventCreateButton = new JButton("+");
    private JButton eventEditButton = new JButton(">");
    private JButton eventRemoveButton = new JButton("X");

    public TriggerEditorFrame(@NotNull NavigationPanel navigationPanel, @NotNull Trigger trigger) {

        super(trigger);

        this.navigationPanel = navigationPanel;
        this.trigger = trigger;

        setSize(300, 500);

        eventCreateButton.setToolTipText("");
        eventCreateButton.addActionListener(this);

        eventEditButton.setToolTipText("");
        eventEditButton.addActionListener(this);

        eventRemoveButton.setToolTipText("");
        eventRemoveButton.addActionListener(this);

        actionCreateButton.setToolTipText("");
        actionCreateButton.addActionListener(this);

        actionEditButton.setToolTipText("");
        actionEditButton.addActionListener(this);

        actionRemoveButton.setToolTipText("");
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

        add(startingCheck);
        add(loopingCheck);

        add(new JLabel("Events:"));
        add(eventsSelector);
        add(eventCreateButton);
        add(eventEditButton);
        add(eventRemoveButton);

        add(new JLabel("Actions:"));
        add(actionsSelector);
        add(actionCreateButton);
        add(actionEditButton);
        add(actionRemoveButton);
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

    boolean handleCreateEvent() {

        Trigger.Event triggerEvent;

        triggerEvent = trigger.new Event() {
            @Override
            public void synchronize(long loopsPerSecond) {

            }
        };
        trigger.getEvents().add(triggerEvent);
        handleShowEventEditor(triggerEvent);

        return true;
    }

    boolean handleEditEvent() {

        Trigger.Event triggerEvent = eventsSelector.getSelectedEntity();
        if (triggerEvent != null) {
            handleShowEventEditor(triggerEvent);
        }
        return true;
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

    boolean handleCreateAction() {

        return true;
    }

    boolean handleEditAction() {

        return true;
    }

    boolean handleRemoveAction() {

        for (Trigger.Action triggerAction : actionsSelector.getSelectedEntities()) {
            trigger.getActions().remove(triggerAction);
        }
        updateEventsSelector();

        return true;
    }
}
