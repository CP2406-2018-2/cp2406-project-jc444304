// Author: Yvan Burrie

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
class PathEditorPanel extends JPanel implements ActionListener {

    private Entity entity;

    private JList<String> pointsSelector = new JList<>();

    private DefaultListModel<String> listModel = new DefaultListModel<>();

    private JButton createButton = new JButton("+");

    private JButton removeButton = new JButton("X");

    PathEditorPanel(Entity entity) {

        this.entity = entity;

        setupListModel();
        pointsSelector.setModel(listModel);

        createButton.setToolTipText("Create a new point where selected.");
        createButton.addActionListener(this);

        removeButton.setToolTipText("Remove selected point(s).");
        removeButton.addActionListener(this);

        setupComponents();
    }

    private void setupListModel() {

        listModel.clear();
        for (Point point : entity.getDimensions()) {
            listModel.addElement(point.x + "," + point.y);
        }
    }

    void setupComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(pointsSelector);
        add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(createButton);
        panel.add(removeButton);
        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object eventSource = event.getSource();

        if (eventSource == createButton) {
            handleCreatePoint();
        }
        if (eventSource == removeButton) {
            handleRemovePoints();
        }
    }

    private boolean handleCreatePoint() {

        String coordinateInput = "0,0";
        while (true) {
            coordinateInput = JOptionPane.showInputDialog(this, "X,Y Coordinate", coordinateInput);
            if (coordinateInput == null) {
                /* Cancel creating point. */
                return false;
            }
            String[] ordinates = coordinateInput.split(",");
            if (ordinates.length != 2) {
                JOptionPane.showMessageDialog(this, "A coordinate must have 2 digits separated by a comma.");
                continue;
            }
            int pointX, pointY;
            try {
                pointX = new Integer(ordinates[0]);
                pointY = new Integer(ordinates[1]);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Ordinates must be a digit.");
                continue;
            }
            entity.getDimensions().add(pointsSelector.getSelectedIndex() + 1, new Point(pointX, pointY));
            setupListModel();
            return true;
        }
    }

    private boolean handleRemovePoints() {

        int[] selectedIndices = pointsSelector.getSelectedIndices();
        int dialogResult = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove (" + selectedIndices.length + ") point(s).",
                "Remove Points",
                JOptionPane.YES_NO_OPTION);
        switch (dialogResult) {
            case JOptionPane.YES_OPTION:
                ArrayList<Point> points = new ArrayList<>();
                for (int selectedIndex : selectedIndices) {
                    points.add(entity.getDimensions().get(selectedIndex));
                }
                for (Point point : points) {
                    entity.getDimensions().remove(point);
                }
                setupListModel();
                return true;
            default:
                return false;
        }
    }
}
