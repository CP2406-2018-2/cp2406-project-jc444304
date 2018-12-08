// Author: Yvan Burrie

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

        add(new JLabel("Type: " + MainFrame.getReadableEntityType(entity)));

        add(new JLabel("Id:"));
        add(idField);

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Description:"));
        add(descriptionField);
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
