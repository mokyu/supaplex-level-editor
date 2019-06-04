/*
 * Copyright (C) 2019 Mokyu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mokyu.supaplexLevelEditor;

import java.awt.Color;
import mokyu.libsupaplex.*;
import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.beans.*;
import java.util.*;

/**
 *
 * @author Mokyu
 */
public class EditorController implements ActionListener, PropertyChangeListener, FocusListener, ChangeListener, ItemListener {

    private final EditorModel model;
    private EditorView view;

    public EditorController(EditorModel model) {
        super();
        this.model = model;
    }

    public void setView(EditorView view) {
        if (this.view == null) {
            this.view = view;
            EventQueue.invokeLater(() -> {
                this.view.setVisible(true);
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getSource().getClass().getName()) {
            case "javax.swing.JButton":
                buttonHandler((JButton) e.getSource());
                break;
            case "javax.swing.JMenuItem":
                menuHandler((JMenuItem) e.getSource());
                break;
            case "javax.swing.JComboBox":
                comboBoxHandler((JComboBox) e.getSource());
                break;
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        System.out.println("Property Change Trigger succesful");
        System.out.println(e.toString());
        view.refresh();
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        switch (((JComponent) e.getSource()).getName()) {
            case "checkbox_levelData_gravity":
                model.setDataChanged(true);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravity(true);
                } else {
                    model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravity(false);
                }
                break;
            case "checkbox_levelData_freezeZonks":
                model.setDataChanged(true);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setFreezeZonks(true);
                } else {
                    model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setFreezeZonks(false);
                }
                break;
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        switch (e.getSource().getClass().getName()) {
            case "javax.swing.JTextField":
                JTextField component = (JTextField) e.getSource();
                switch (component.getName()) {
                    case "textField_levelData_levelName":
                        try {
                            model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setName(component.getText());
                            component.setBorder(new JTextField().getBorder());
                            model.setDataChanged(true);
                        } catch (Exception ex) {
                            // spawning a popup box every time we fail to validate the new name is a bad idea, lets just create a red border instead when it's invalid.
                            component.setBorder(new LineBorder(Color.red, 1));

                        }
                        break;
                    case "textField_levelData_requiredInfotrons":
                        Integer value = null;
                        try {
                            value = Integer.parseInt(component.getText());
                            if (value > -1 && value < 256) {
                                component.setBorder(new JTextField().getBorder());
                                model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setRequiredInfotrons(value);
                                model.setDataChanged(true);
                                return;
                            }

                        } catch (NumberFormatException ex) {
                            // don't do anything with error, handle here
                        }
                        // set border red when number out of range or invalid
                        component.setBorder(new LineBorder(Color.red, 1));

                        break;
                }

        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("State changed");
    }

    private void buttonHandler(JButton source) {
        System.out.println("Button press: " + source.getName());
    }

    private void comboBoxHandler(JComboBox source) {
        System.out.println("ComboBox change: " + source.getName());
        switch (source.getName()) {
            case "combobox_levelData_selectLevelSlot":
                this.setCurrentLevelSlot(source.getSelectedIndex());
                model.fireChanged();
                break;
        }
    }

    private void menuHandler(JMenuItem source) {
        System.out.println("Menu item: " + source.getName());
        // for loading collections and such
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter collectionFilter = new FileNameExtensionFilter("Supaplex level collection (*.DAT)", "dat", "DAT");
        FileNameExtensionFilter levelFilter = new FileNameExtensionFilter("Supaplex level file (*.SP)", "sp", "SP");
        // File Menu
        switch (source.getName()) {
            case "menu_file_newLevelCol":
                //<editor-fold defaultstate="collapsed" desc="New Level Collection">
                if (model.getDataChanged()) {
                    if (view.spawnChoiceBox(language.getFromTag(this.getPreferredLanguage(), "string_confirmation_discardUnsavedChanges")) == false) {
                        return;
                    }
                    model.setDataChanged(false);
                }
                model.setLevelCollection(new Supaplex());
                model.getLevelCollection().init();
                model.init(); // bind new supaplex object to model listener
                //</editor-fold>
                break;
            case "menu_file_loadLevelCol":
                //<editor-fold defaultstate="collapsed" desc="Load Level Collection">
                chooser.setFileFilter(collectionFilter);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (model.getDataChanged()) {
                        if (view.spawnChoiceBox(language.getFromTag(this.getPreferredLanguage(), "string_confirmation_discardUnsavedChanges")) == false) {
                            return;
                        }
                        model.setDataChanged(false);
                    }
                    try {
                        model.getLevelCollection().loadLevelPackFromFile(chooser.getSelectedFile().getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.spawnMsgBox("ERROR", e.toString());
                        return;
                    }
                }
                //</editor-fold>
                break;
            case "menu_file_saveLevelCol":
                //<editor-fold defaultstate="collapsed" desc="Save Level Collection">
                chooser.setFileFilter(collectionFilter);
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        model.getLevelCollection().saveLevelPackToFile(chooser.getSelectedFile().getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.spawnMsgBox("ERROR", e.toString());
                        return;
                    }
                }
                //</editor-fold>
                break;
            case "menu_file_exportLevel":
                System.out.println("export");
                break;
            case "menu_file_importLevel":
                System.out.println("import");
                break;
            case "menu_file_exit":
                //<editor-fold defaultstate="collapsed" desc="Exit">
                EventQueue.invokeLater(() -> {
                    if (model.getDataChanged()) {
                        if (view.spawnChoiceBox(language.getFromTag(this.getPreferredLanguage(), "string_confirmation_discardUnsavedChanges")) == false) {
                            return;
                        }
                        model.setDataChanged(false);
                    }
                    this.view.dispose();
                    System.exit(0);

                });
                //</editor-fold>
                break;
            case "":
        }
        // View Menu

        // Help menu
    }

    public Integer getCurrentLevelSLot() {
        return model.getCurrentLevelSlot();
    }

    public void setCurrentLevelSlot(int value) {
        model.setCurrentLevelSlot(value);
    }

    // functionality
    public String getPreferredLanguage() {
        return model.getProperty("language");
    }

    public void setPreferredLanguage(String language) {
        model.setProperty("language", language);
        model.fireChanged();
    }

    /**
     * Get list of all level names
     *
     * @return Array with all level names
     */
    public List<String> getLevelList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 111; i++) {
            list.add(String.format("%d: %s", (i + 1), model.getLevelCollection().getLevel(i).getName()));
        }
        return list;
    }

}
