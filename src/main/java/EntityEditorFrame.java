// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
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

    EntityEditorFrame(@NotNull T entity) {

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
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Type: " + MainFrame.getReadableType(entity)));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Id:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(idField);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Name:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(nameField);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Description:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(descriptionField);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Dimensions:"));
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
