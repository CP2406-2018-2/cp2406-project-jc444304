// Author: Yvan Burrie

package SmartHome;

import org.json.simple.*;

import java.util.ArrayList;

abstract class Asset implements JsonDeserializable, JsonTypeable, Synchronizable {

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

        assetBuffer.put("Type", JSON_TYPE);

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

    ArrayList<Point> dimension;

    public Entity(Automator automator) {
        super(automator);
    }

    public Entity(Automator automator, JSONObject entityBuffer) throws JsonDeserializedError {
        super(automator, entityBuffer);
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
        objectBuffer = entityBuffer.get("Dimension");
        if (objectBuffer instanceof JSONArray) {
            JSONArray dimensionBuffer = (JSONArray) objectBuffer;
            for (Object elementBuffer : dimensionBuffer) {
                if (elementBuffer instanceof JSONArray) {
                    JSONArray pointBuffer = (JSONArray) elementBuffer;
                    int pointX = 0, pointY = 0;
                    objectBuffer = pointBuffer.get(0);
                    if (objectBuffer instanceof Integer) {
                        pointX = (int) objectBuffer;
                    }
                    objectBuffer = pointBuffer.get(1);
                    if (objectBuffer instanceof Integer) {
                        pointY = (int) objectBuffer;
                    }
                    dimension.add(new Point(pointX, pointY));
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
        if (dimension != null) {
            JSONArray dimensionBuffer = new JSONArray();
            for (Point point : dimension) {
                JSONArray pointBuffer = new JSONArray();
                pointBuffer.add(point.x);
                pointBuffer.add(point.y);
                dimensionBuffer.add(pointBuffer);
            }
            entityBuffer.put("Dimension", dimensionBuffer);
        }

        return entityBuffer;
    }

    class Point {

        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
