// Author: Yvan Burrie

import java.awt.*;
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

    private ClockPanel startClockPanel;

    private ClockPanel endClockPanel;

    private JTextField speedField = new JTextField(1);

    private JTextField loopsPerSecField = new JTextField(1);

    private JButton applyButton = new JButton("Apply");

    OptionsFrame(@NotNull Simulator simulator) {

        this.simulator = simulator;

        Container container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        startClockPanel = new ClockPanel(this, simulator.getStartClock());
        endClockPanel = new ClockPanel(this, simulator.getEndClock());

        applyButton.addActionListener(this);

        updateSpeedField();
        updateLoopsPerSecField();

        setupComponents();

        setBounds(100, 100, 300, 700);
        pack();
        setResizable(false);
    }

    private void updateSpeedField() {

        speedField.setText(Long.toString(simulator.getSyncSpeed()));
    }

    private void updateLoopsPerSecField() {

        loopsPerSecField.setText(Long.toString(simulator.getSyncLoopsPerSec()));
    }

    private void setupComponents() {

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Period Start:"));
        panel.add(startClockPanel);

        panel.add(new JLabel("Period End:"));
        panel.add(endClockPanel);

        panel.add(new JLabel("Speed:"));
        panel.add(speedField);

        panel.add(new JLabel("Loops per second:"));
        panel.add(loopsPerSecField);

        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(applyButton);
        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        Object actionEventSource = actionEvent.getSource();

        if (actionEventSource == applyButton) {
            handleApply();
            return;
        }
    }

    boolean handleApply() {

        startClockPanel.handleApply();

        endClockPanel.handleApply();

        simulator.setSyncSpeed(new Long(speedField.getText()));
        updateSpeedField();

        simulator.setSyncLoopsPerSec(new Long(loopsPerSecField.getText()));
        updateSpeedField();

        return true;
    }
}
