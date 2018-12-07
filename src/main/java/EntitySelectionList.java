// Author: Yvan Burrie

import javax.swing.*;
import java.util.ArrayList;
import SmartHome.*;

/**
 *
 */
public class EntitySelectionList<T extends Entity> extends JList<String> {

    private ArrayList<T> entities = new ArrayList<>();

    public T getSelectedEntity() {
        return entities.get(getSelectedIndex());
    }

    public ArrayList<T> getSelectedEntities() {
        ArrayList<T> selectedEntities = new ArrayList<>();
        for (int index : getSelectedIndices()) {
            selectedEntities.add(entities.get(index));
        }
        return selectedEntities;
    }

    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public EntitySelectionList() {

        super();
        setup();
    }

    public EntitySelectionList(ArrayList<T> entities) {

        super();
        setup();
        enlist(entities);
    }

    private void setup() {

        setModel(listModel);
    }

    void enlist(ArrayList<T> entities) {

        this.entities = entities;

        listModel.clear();
        for (T entity : entities) {
            listModel.addElement(entity.getName());
        }
    }
}
