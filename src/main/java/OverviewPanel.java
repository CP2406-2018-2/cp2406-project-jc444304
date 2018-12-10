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

    void addEntity(Entity entity) {
        entities.add(entity);
    }

    void clearEntities() {
        entities.clear();
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D graphics = (Graphics2D) g;

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}
