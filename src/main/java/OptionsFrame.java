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

    private MainFrame mainFrame;

    private Simulator simulator;

    private JTextField nameField = new JTextField(1);

    private JTextArea descriptionField = new JTextArea();

    private ClockPanel startClockPanel;

    private ClockPanel endClockPanel;

    private JTextField speedField = new JTextField(1);

    private JTextField loopsPerSecField = new JTextField(1);

    private JButton applyButton = new JButton("Apply");

    OptionsFrame(@NotNull MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        this.simulator = mainFrame.simulator;

        setTitle(MainFrame.APPLICATION_NAME + " - Options");

        Container container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        startClockPanel = new ClockPanel(this, simulator.getStartClock());
        endClockPanel = new ClockPanel(this, simulator.getEndClock());

        applyButton.addActionListener(this);

        updateNameField();
        updateDescriptionField();
        updateSpeedField();
        updateLoopsPerSecField();

        setupComponents();

        setSize(300, 700);
        setLocationByPlatform(true);
        pack();
        setResizable(false);
    }

    private void updateNameField() {

        nameField.setText(simulator.getName());
    }

    private void updateDescriptionField() {

        descriptionField.setText(simulator.getDescription());
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
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

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

        simulator.setName(nameField.getText());
        updateNameField();

        simulator.setDescription(descriptionField.getText());
        updateDescriptionField();

        startClockPanel.handleApply();

        endClockPanel.handleApply();

        simulator.setSyncSpeed(new Long(speedField.getText()));
        updateSpeedField();

        simulator.setSyncLoopsPerSec(new Long(loopsPerSecField.getText()));
        updateSpeedField();

        mainFrame.handleProjectChanged();

        return true;
    }
}
