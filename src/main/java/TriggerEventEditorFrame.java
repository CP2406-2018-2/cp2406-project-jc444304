// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
class TriggerEventEditorFrame extends EntityEditorFrame<Trigger.Event> {

    Trigger.Event entity;

    JCheckBox orPreviousCheck = new JCheckBox("OR with previous Event.");

    TriggerEventEditorFrame(@NotNull Trigger.Event triggerEvent) {

        super(triggerEvent);

        orPreviousCheck.setToolTipText(
                "If checked, the previous Event will be OR'ed, otherwise AND'ed.\n" +
                        "Note that this does not apply to the first or single Event.");
    }

    private void updateOrPreviousCheck() {

        orPreviousCheck.setSelected(entity.isOrPrevious());
    }

    @Override
    void setupComponents() {

        super.setupComponents();

        add(orPreviousCheck);
    }

    @Override
    boolean handleApply() {

        entity.setOrPrevious(orPreviousCheck.isSelected());
        updateOrPreviousCheck();

        return super.handleApply();
    }
}
