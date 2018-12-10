// Author: Yvan Burrie

import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;
import com.sun.istack.internal.NotNull;

/**
 *
 */
public abstract class EntityFrame<T extends Entity> extends JFrame implements ActionListener {

    T entity;

    JTextField idField = new JTextField();

    JTextField nameField = new JTextField();

    JTextArea descriptionField = new JTextArea();

    EntityFrame(@NotNull T entity) {

        this.entity = entity;

        setLocationByPlatform(true);
        setResizable(false);
    }

    public void initialize() {

        setupComponents();
        pack();
        update();
    }

    void update() {

        updateId();
        updateName();
        updateDescription();
    }

    abstract void setupComponents();

    private void updateId() {

        idField.setText(entity.getId());
    }

    private void updateName() {

        nameField.setText(entity.getName());
    }

    private void updateDescription() {

        descriptionField.setText(entity.getDescription());
    }

    boolean handleApply() {

        String idFieldText = idField.getText();
        if (!idFieldText.equals(entity.getId())) {
            entity.setId(idFieldText);
            updateId();
        }
        String nameFieldText = nameField.getText();
        if (!nameFieldText.equals(entity.getName())) {
            entity.setName(nameFieldText);
            updateName();
        }
        String descriptionFieldText = descriptionField.getText();
        if (!descriptionFieldText.equals(entity.getDescription())) {
            entity.setDescription(descriptionFieldText);
            updateDescription();
        }

        return true;
    }
}
