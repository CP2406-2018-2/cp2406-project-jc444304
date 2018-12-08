// Author: Yvan Burrie

import SmartHome.*;
import javax.swing.*;

/**
 *
 */
public class AutomationPanel extends JPanel {

    Automator automator;

    public AutomationPanel() {

    }

    public void initialize(Automator automator) {

        this.automator = automator;
    }
}
