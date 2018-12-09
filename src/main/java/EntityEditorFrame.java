// Author: Yvan Burrie

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public abstract class EntityEditorFrame<T extends Entity> extends EntityFrame {

    private JButton applyButton = new JButton("Apply");

    public EntityEditorFrame(T entity) {

        super(entity);
    }

    public void initialize() {

        super.initialize();

        add(applyButton);
        applyButton.setToolTipText("Apply all changes.");
        applyButton.addActionListener(this);
    }

    void setupComponents() {

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Type: "));
        panel.add(new JLabel(MainFrame.getReadableType(entity)));

        panel.add(new JLabel("Id:"));
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        Object actionEventSource = actionEvent.getSource();

        if (actionEventSource == applyButton) {
            if (handleApply()) {
                dispose();
            }
            return;
        }
    }
}
