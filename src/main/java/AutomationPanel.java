// Author: Yvan Burrie

import SmartHome.*;
import javax.swing.*;

/**
 *
 */
class AutomationPanel extends JPanel {

    private MainFrame mainFrame;

    private Automator automator;

    private OverviewPanel overviewPanel = new OverviewPanel();

    AutomationPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

    }

    void initialize(Automator automator) {

        this.automator = automator;
    }
}
