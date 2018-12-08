// Author: Yvan Burrie

import SmartHome.*;

/**
 *
 */
public class TriggerActionEditorFrame extends EntityEditorFrame<Trigger.Action> {

    private Trigger.Action triggerAction;

    public TriggerActionEditorFrame(Trigger.Action triggerAction) {

        super(triggerAction);

        this.triggerAction = triggerAction;
    }
}
