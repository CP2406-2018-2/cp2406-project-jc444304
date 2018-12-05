// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

abstract class Asset implements JsonDeserializable, Synchronizable {

    @Override
    public String getJsonType() {
        return null;
    }

    Automator automator;

    public Asset(Automator automator) {
        this.automator = automator;
    }

    public Asset(Automator automator, JSONObject assetBuffer) throws JsonDeserializedError {
        this.automator = automator;
        jsonDeserialize(assetBuffer);
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject assetBuffer = new JSONObject();

        String jsonType = getJsonType();
        if (jsonType != null) {
            assetBuffer.put("Type", jsonType);
        }

        return assetBuffer;
    }
}

/**
 * An entity is anything that is drawn, saved, and part of the automation system.
 */
abstract class Entity extends Asset {

    String id;

    String name;

    String description;

    ArrayList<Point> dimensions;

    public Entity(Automator automator) {
        super(automator);
    }

    public Entity(Automator automator, JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator);
        jsonDeserialize(entityBuffer);
    }

    @Override
    public void jsonDeserialize(JSONObject entityBuffer) throws JsonDeserializedError {

        Object objectBuffer;

        objectBuffer = entityBuffer.get("Id");
        if (objectBuffer instanceof String) {
            id = (String) objectBuffer;
        }
        objectBuffer = entityBuffer.get("Name");
        if (objectBuffer instanceof String) {
            name = (String) objectBuffer;
        }
        objectBuffer = entityBuffer.get("Description");
        if (objectBuffer instanceof String) {
            description = (String) objectBuffer;
        }
        objectBuffer = entityBuffer.get("Dimensions");
        if (objectBuffer instanceof JSONArray) {
            dimensions = new ArrayList<>();
            JSONArray dimensionBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : dimensionBuffer) {
                if (elementBuffer instanceof JSONArray) {
                    JSONArray pointBuffer = (JSONArray) elementBuffer;
                    long pointX = 0, pointY = 0;
                    objectBuffer = pointBuffer.get(0);
                    if (objectBuffer instanceof Long) {
                        pointX = (long) objectBuffer;
                    }
                    objectBuffer = pointBuffer.get(1);
                    if (objectBuffer instanceof Long) {
                        pointY = (long) objectBuffer;
                    }
                    dimensions.add(new Point((int) pointX, (int) pointY));
                }
            }
        }
    }

    @Override
    public JSONObject jsonSerialize() throws JsonSerializedError {

        JSONObject entityBuffer = super.jsonSerialize();

        if (id != null) {
            entityBuffer.put("Id", id);
        }
        if (name != null) {
            entityBuffer.put("Name", name);
        }
        if (description != null) {
            entityBuffer.put("Description", description);
        }
        if (dimensions != null) {
            JSONArray dimensionBuffer = new JSONArray();
            for (Point point : dimensions) {
                JSONArray pointBuffer = new JSONArray();
                pointBuffer.add(point.x);
                pointBuffer.add(point.y);
                dimensionBuffer.add(pointBuffer);
            }
            entityBuffer.put("Dimensions", dimensionBuffer);
        }

        return entityBuffer;
    }

    /**
     * Generates a path polygon according to dimensions.
     * @return Returns an instance of a Path or null if no points are present.
     */
    public GeneralPath toPath() {

        GeneralPath path = null;

        if (dimensions != null) {
            int pointsSize = dimensions.size();
            path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, pointsSize);
            for (int i = 0; i < pointsSize; i++) {
                Point point = dimensions.get(i);
                if (i == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                }
            }
            path.closePath();
        }

        return path;
    }
}
