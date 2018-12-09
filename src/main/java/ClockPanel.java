// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import SmartHome.*;

class ClockPanel extends JPanel {

    private Clock clock;

    JTextField yearField = new JTextField();
    JTextField monthField = new JTextField();
    JTextField dayField = new JTextField();
    JTextField hourField = new JTextField();
    JTextField minuteField = new JTextField();
    JTextField secondField = new JTextField();

    ClockPanel(@NotNull ActionListener parent, @NotNull Clock clock) {

        this.clock = clock;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        yearField.addActionListener(parent);
        yearField.setMinimumSize(new Dimension(100, 20));
        yearField.setToolTipText("Year");

        monthField.addActionListener(parent);
        monthField.setToolTipText("Month");

        dayField.addActionListener(parent);
        dayField.setToolTipText("Day");

        hourField.addActionListener(parent);
        hourField.setToolTipText("Hour");

        minuteField.addActionListener(parent);
        minuteField.setToolTipText("Minute");

        secondField.addActionListener(parent);
        secondField.setToolTipText("Second");

        update();

        setupComponents();
    }

    void update() {

        yearField.setText(Integer.toString(clock.get(Clock.YEAR)));
        monthField.setText(Integer.toString(clock.get(Clock.MONTH)));
        dayField.setText(Integer.toString(clock.get(Clock.DAY_OF_MONTH)));
        hourField.setText(Integer.toString(clock.get(Clock.HOUR_OF_DAY)));
        minuteField.setText(Integer.toString(clock.get(Clock.MINUTE)));
        secondField.setText(Integer.toString(clock.get(Clock.SECOND)));
    }

    private void setupComponents() {

        add(yearField);
        add(monthField);
        add(dayField);
        add(hourField);
        add(minuteField);
        add(secondField);
    }

    boolean handleApply() {

        clock.set(Clock.YEAR, new Integer(yearField.getText()));
        clock.set(Clock.MONTH, new Integer(monthField.getText()));
        clock.set(Clock.DAY_OF_MONTH, new Integer(dayField.getText()));
        clock.set(Clock.HOUR_OF_DAY, new Integer(hourField.getText()));
        clock.set(Clock.MINUTE, new Integer(minuteField.getText()));
        clock.set(Clock.SECOND, new Integer(secondField.getText()));

        update();

        return true;
    }
}