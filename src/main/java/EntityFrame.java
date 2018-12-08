// Author: Yvan Burrie

import java.awt.event.*;
import javax.swing.*;
import SmartHome.*;

/**
 *
 */
public abstract class EntityFrame<T extends Entity> extends JFrame implements ActionListener {

    T entity;

    JTextField idField = new JTextField();

    JTextField nameField = new JTextField();

    JTextArea descriptionField = new JTextArea();

    EntityFrame(T entity) {

        this.entity = entity;

        setAlwaysOnTop(true);
        setResizable(false);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }

    public void initialize() {

        setupComponents();
        update();
    }

    abstract void setupComponents();

    void update() {

        updateId();
        updateName();
        updateDescription();
    }

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
