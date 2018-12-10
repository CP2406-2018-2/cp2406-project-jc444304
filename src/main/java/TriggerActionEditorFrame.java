// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
import SmartHome.*;

/**
 *
 */
class TriggerActionEditorFrame extends EntityEditorFrame<Trigger.Action> {

    private Trigger.Action triggerAction;

    TriggerActionEditorFrame(@NotNull Trigger.Action triggerAction) {

        super(triggerAction);

        this.triggerAction = triggerAction;
    }
}
