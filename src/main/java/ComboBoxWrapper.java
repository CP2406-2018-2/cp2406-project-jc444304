// Author: Yvan Burrie

import javax.swing.*;
import java.util.HashMap;

class ComboBoxWrapper<K> extends JComboBox<String> {

    private HashMap<K, String> buffers = new HashMap<>();

    ComboBoxWrapper() {

    }

    void addItem(String item, K key) {

        super.addItem(item);
        buffers.put(key, item);
    }

    K getSelectedKey() {

        for (HashMap.Entry<K, String> buffer : buffers.entrySet()) {
            Object selectedItem = getSelectedItem();
            if (selectedItem instanceof String && selectedItem.equals(buffer.getValue())) {
                return buffer.getKey();
            }
        }
        return null;
    }
}
