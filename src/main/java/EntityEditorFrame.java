// Author: Yvan Burrie

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public abstract class EntityEditorFrame<T extends Entity> extends EntityFrame {

    private PathEditorPanel pathEditor;

    private JButton applyButton = new JButton("Apply");

    public EntityEditorFrame(T entity) {

        super(entity);

        pathEditor = new PathEditorPanel(entity);

        applyButton.setToolTipText("Apply all changes.");
        applyButton.addActionListener(this);
    }

    public void initialize() {

        super.initialize();

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(applyButton);
        add(panel);
    }

    @Override
    void setupComponents() {

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        panel.add(new JLabel("Type: "));
        panel.add(new JLabel(MainFrame.getReadableType(entity)));

        panel.add(new JLabel("Id:"));
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Dimensions:"));
        panel.add(pathEditor);

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
