// Author: Yvan Burrie

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import SmartHome.*;
import com.sun.istack.internal.NotNull;

/**
 *
 */
class OptionsFrame extends JFrame implements ActionListener {

    private Simulator simulator;

    private ClockPanel periodStartPanel;

    private ClockPanel periodEndPanel;

    private JTextField speedField = new JTextField(1);

    private JButton applyButton = new JButton("Apply");

    OptionsFrame(@NotNull Simulator simulator) {

        this.simulator = simulator;

        periodStartPanel = new ClockPanel(simulator.getPeriodStart());
        periodEndPanel = new ClockPanel(simulator.getPeriodEnd());

        updateSpeedField();

        setupComponents();
    }

    void updateSpeedField() {

        speedField.setText(Long.toString(simulator.getSyncSpeed()));
    }

    void setupComponents() {

        add(new JLabel("Period Start:"));
        add(periodStartPanel);

        add(new JLabel("Period Start:"));
        add(periodEndPanel);

        add(new JLabel("Speed:"));
        add(speedField);

        add(applyButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        Object actionEventSource = actionEvent.getSource();

        if (actionEventSource == applyButton) {
            handleApply();
            return;
        }
    }

    private boolean handleApply() {

        if (speedField.getText()) {
            updateSpeedField();
        }

        return true;
    }
}
