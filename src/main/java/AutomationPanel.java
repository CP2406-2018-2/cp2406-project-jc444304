// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
class AutomationPanel extends JPanel {

    private MainFrame mainFrame;

    private Automator automator;

    private OverviewPanel overviewPanel = new OverviewPanel();

    AutomationPanel(@NotNull MainFrame mainFrame) {

        this.mainFrame = mainFrame;

    }

    void initialize(@NotNull Automator automator) {

        this.automator = automator;

        JFrame frame = new JFrame();
        for (Venue venue : automator.getVenues()) {
            overviewPanel.addEntity(venue);
        }
        frame.add(overviewPanel);
        frame.setVisible(true);
    }
}
