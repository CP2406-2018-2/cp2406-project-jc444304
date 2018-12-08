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

    private JTextField loopsPerSecField = new JTextField(1);

    private JButton applyButton = new JButton("Apply");

    OptionsFrame(@NotNull Simulator simulator) {

        this.simulator = simulator;

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        periodStartPanel = new ClockPanel(simulator.getPeriodStart());
        periodEndPanel = new ClockPanel(simulator.getPeriodEnd());

        updateSpeedField();
        updateLoopsPerSecField();

        setupComponents();

        setSize(300, 400);
        setResizable(false);
    }

    private void updateSpeedField() {

        speedField.setText(Long.toString(simulator.getSyncSpeed()));
    }

    private void updateLoopsPerSecField() {

        loopsPerSecField.setText(Long.toString(simulator.getSyncLoopsPerSec()));
    }

    private void setupComponents() {

        add(new JLabel("Period Start:"));
        add(periodStartPanel);

        add(new JLabel("Period End:"));
        add(periodEndPanel);

        add(new JLabel("Speed:"));
        add(speedField);

        add(new JLabel("Loops per second:"));
        add(loopsPerSecField);

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

        simulator.setSyncSpeed(new Long(speedField.getText()));
        updateSpeedField();

        simulator.setSyncLoopsPerSec(new Long(loopsPerSecField.getText()));
        updateSpeedField();

        return true;
    }
}
