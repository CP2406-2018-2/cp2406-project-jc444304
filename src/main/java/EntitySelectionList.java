// Author: Yvan Burrie

import javax.swing.*;
import java.util.ArrayList;
import SmartHome.*;

/**
 *
 */
class EntitySelectionList<T extends Entity> extends JList<String> {

    private ArrayList<T> entities = new ArrayList<>();

    T getSelectedEntity() {
        int selectedIndex = getSelectedIndex();
        return selectedIndex >= 0 ? entities.get(selectedIndex) : null;
    }

    ArrayList<T> getSelectedEntities() {
        ArrayList<T> selectedEntities = new ArrayList<>();
        for (int index : getSelectedIndices()) {
            selectedEntities.add(entities.get(index));
        }
        return selectedEntities;
    }

    private DefaultListModel<String> listModel = new DefaultListModel<>();

    EntitySelectionList() {

        super();
        setup();
    }

    EntitySelectionList(ArrayList<T> entities) {

        super();
        setup();
        initialize(entities);
    }

    private void setup() {

        setModel(listModel);
    }

    void initialize(ArrayList<T> entities) {

        this.entities = entities;

        listModel.clear();
        for (T entity : entities) {
            listModel.addElement(entity.getName());
        }
    }
}
