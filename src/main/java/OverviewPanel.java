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

        for (Entity entity : entities) {
            GeneralPath path = entity.toPath();
            if (path != null) {
                GradientPaint redtowhite = new GradientPaint(0,0,Color.RED,100, 0,Color.WHITE);
                graphics.setPaint(redtowhite);
                //graphics.setPaint(Color.GRAY);
                graphics.fill(path);
                graphics.setPaint(Color.BLACK);
                graphics.draw(path);
            }
        }
    }
}
