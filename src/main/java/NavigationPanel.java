// Author: Yvan Burrie

import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public class NavigationPanel extends JPanel implements ActionListener {

    private MainFrame mainFrame;

    private Automator automator;

    public NavigationPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        setupComponents();
    }

    @Override
    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
    }

    void setupComponents() {

    }

    public void initialize(Automator automator) {

        this.automator = automator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
}
