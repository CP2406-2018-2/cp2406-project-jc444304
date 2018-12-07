// Author: Yvan Burrie

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public class PathEditorPanel extends JPanel implements ActionListener {

    private Entity entity;

    private JList<String> pointsSelector = new JList<>();

    private DefaultListModel<String> listModel = new DefaultListModel<>();

    private JButton createButton = new JButton("+");

    private JButton removeButton = new JButton("X");

    private JComponent sample = new JComponent() {
        @Override
        public void paint(Graphics g) {
            Graphics2D graphics = (Graphics2D) g;
            graphics.setPaint(Color.BLACK);
            graphics.fill(entity.toPath());
        }
    };

    public PathEditorPanel(Entity entity) {

        this.entity = entity;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setupListModel();
        pointsSelector.setModel(listModel);

        createButton.setToolTipText("Create a new point where selected.");
        createButton.addActionListener(this);

        removeButton.setToolTipText("Remove selected point(s).");
        removeButton.addActionListener(this);

        sample.setMinimumSize(new Dimension(300, 300));

        setupComponents();
    }

    private void setupListModel() {

        listModel.clear();
        for (Point point : entity.getDimensions()) {
            listModel.addElement(point.x + "," + point.y);
        }
    }

    void setupComponents() {

        add(pointsSelector);
        add(createButton);
        add(removeButton);
        add(sample);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object eventSource = event.getSource();

        if (eventSource == createButton) {
            handleCreatePoints();
        }
    }

    private boolean handleCreatePoints() {

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
}
