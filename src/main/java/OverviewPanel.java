// Author: Yvan Burrie

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import javax.swing.JComponent;
import SmartHome.*;

/**
 * This component will visually render the entire home.
 */
public class OverviewPanel extends JComponent {

    private ArrayList<Entity> entities = new ArrayList<>();

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void paint(Graphics g) {

    }
}
