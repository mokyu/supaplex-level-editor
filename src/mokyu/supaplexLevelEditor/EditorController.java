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
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.beans.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.*;

/**
 *
 * @author Mokyu
 */
public class EditorController implements ActionListener, FocusListener, ItemListener, JLevelViewListener {

    private final EditorModel model;
    private EditorView view;

    public enum drawMode {
        pencil, line, rect, fill
    };

    public EditorController(EditorModel model) {
        super();
        this.model = model;
    }

    public void setView(EditorView view) {
        if (this.view == null) {
            this.view = view;
            this.view.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getSource().getClass().getName()) {
            case "javax.swing.JButton":
                buttonHandler((JButton) e.getSource());
                break;
            case "javax.swing.JMenuItem":
            case "javax.swing.JRadioButtonMenuItem":
                menuHandler((JMenuItem) e.getSource());
                break;
            case "javax.swing.JComboBox":
                comboBoxHandler((JComboBox) e.getSource());
                break;
        }
        view.refresh();
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        GravitySwitchPort port = model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).getGravitySwitchPortData(this.getCurrentSpecialPort());

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
            case "checkBox_levelData_specialPortToggleGravity":
                model.setDataChanged(true);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    port.setGravity(true);
                } else {
                    port.setGravity(false);
                }
                break;
            case "checkBox_levelData_specialPortToggleFreezeEnemy":
                model.setDataChanged(true);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    port.setFreezeEnemy(true);
                } else {
                    port.setFreezeEnemy(false);
                }
                break;
            case "checkBox_levelData_specialPortToggleFreezeZonks":
                model.setDataChanged(true);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    port.setFreezeZonks(true);
                } else {
                    port.setFreezeZonks(false);
                }
                break;
        }
        model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravitySwitchPortData(this.getCurrentSpecialPort(), port);
        view.refresh();
    }

    /**
     * focusLost events are used to validate and apply changes made in text
     * fields
     *
     * @param e
     */
    @Override
    public void focusLost(FocusEvent e) {
        GravitySwitchPort port = model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).getGravitySwitchPortData(this.getCurrentSpecialPort());
        switch (e.getSource().getClass().getName()) {
            case "javax.swing.JTextField":
                JTextField component = (JTextField) e.getSource();
                switch (component.getName()) {
                    case "textField_levelData_levelName":
                        try {
                            // Lets append dashes infront and after the level name when the string is less than 23 characters long
                            if (component.getText().length() < 23) {
                                Integer pad = 23 - component.getText().length();
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < (pad / 2); i++) {
                                    stringBuilder.append('-');
                                }
                                stringBuilder.append(component.getText());
                                for (int i = 0; i < (pad / 2 + (pad % 2)); i++) {
                                    stringBuilder.append('-');
                                }
                                component.setText(stringBuilder.toString());
                            }
                            model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setName(component.getText());
                            component.setBorder(new JTextField().getBorder());
                        } catch (Exception ex) {
                            component.setBorder(new LineBorder(Color.red, 1));
                        }
                        break;
                    case "textField_levelData_requiredInfotrons":
                        try {
                            Integer val = Integer.parseInt(component.getText());
                            if (val > -1 && val < 256) {
                                component.setBorder(new JTextField().getBorder());
                                model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setRequiredInfotrons(val);
                                view.refresh();
                            }

                        } catch (NumberFormatException ex) {
                            component.setBorder(new LineBorder(Color.red, 1));
                        }

                        break;
                    case "textField_levelData_gravitySwitchPortCount":
                        try {
                            Integer val = Integer.parseInt(component.getText());
                            if (val > 0 && val < 11) {
                                component.setBorder(new JTextField().getBorder());
                                model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravitySwitchPorts(val);
                                model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravitySwitchPortData(this.getCurrentSpecialPort(), port);
                            }
                        } catch (NumberFormatException ex) {
                            component.setBorder(new LineBorder(Color.red, 1));
                        }
                        break;
                    case "textField_levelData_gravitySwitchPortSelection":
                        try {
                            Integer val = Integer.parseInt(component.getText());
                            if (val > 0 && val < 11) {
                                component.setBorder(new JTextField().getBorder());
                                model.setCurrentSpecialPort(val);
                                model.setDataChanged(true);
                            }
                        } catch (NumberFormatException ex) {
                            component.setBorder(new LineBorder(Color.red, 1));
                        }
                        break;
                    case "textField_levelData_gravitySwitchPortX":
                        try {
                            Integer val = Integer.parseInt(component.getText());
                            component.setBorder(new JTextField().getBorder());
                            port.setX(val);
                            model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravitySwitchPortData(this.getCurrentSpecialPort(), port);
                        } catch (NumberFormatException ex) {
                            component.setBorder(new LineBorder(Color.red, 1));
                        }
                        break;
                    case "textField_levelData_gravitySwitchPortY":
                        try {
                            Integer val = Integer.parseInt(component.getText());
                            component.setBorder(new JTextField().getBorder());
                            port.setY(val);
                            model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).setGravitySwitchPortData(this.getCurrentSpecialPort(), port);
                        } catch (NumberFormatException ex) {
                            component.setBorder(new LineBorder(Color.red, 1));
                        }
                        break;
                }

        }
        view.refresh();
    }

    private void buttonHandler(JButton source) {
        System.out.println(source.getName());
        switch (source.getName()) {
            //<editor-fold defaultstate="collapsed" desc="tools">
            case "button_toolSet_pencilTool":
                model.setDrawMode(EditorController.drawMode.pencil);
                break;
            case "button_toolSet_lineTool":
                model.setDrawMode(EditorController.drawMode.line);
                break;
            case "button_toolSet_rectangleTool":
                model.setDrawMode(EditorController.drawMode.rect);
                break;
            case "button_toolSet_fillTool":
                model.setDrawMode(EditorController.drawMode.fill);
                break;
            //</editor-fold>
        }

        // handle all the other buttons (tile buttons)
        for (int i = 0x0; i <= 0x28; i++) {
            if (new Tile((byte) i).getNiceName().equals(source.getName())) {
                model.setSelectedTile(new Tile((byte) i));
                return;
            }
        }
        view.refresh();
    }

    private void comboBoxHandler(JComboBox source) {
        switch (source.getName()) {
            case "combobox_levelData_selectLevelSlot":
                this.setCurrentLevelSlot(source.getSelectedIndex());
                break;
        }
        view.refresh();
    }

    private void menuHandler(JMenuItem source) {
        // for loading collections and such
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter collectionFilter = new FileNameExtensionFilter("Supaplex level collection (*.DAT)", "dat", "DAT");
        FileNameExtensionFilter levelFilter = new FileNameExtensionFilter("Supaplex level file (*.SP)", "sp", "SP");
        // File Menu
        switch (source.getName()) {
            case "menu_file_newLevelCol":
                //<editor-fold defaultstate="collapsed" desc="New Level Collection">
                if (model.getDataChanged()) {
                    if (view.spawnChoiceBox(language.getFromTag(this.getPreferredLanguage(), "string_confirmation_discardUnsavedChanges")) == true) {
                        model.setDataChanged(false);
                        model.setLevelCollection(new Supaplex());
                    }
                } else {
                    model.setDataChanged(false);
                    model.setLevelCollection(new Supaplex());
                }

                //</editor-fold>
                break;
            case "menu_file_loadLevelCol":
                //<editor-fold defaultstate="collapsed" desc="Load Level Collection">
                chooser.setFileFilter(collectionFilter);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (model.getDataChanged()) {
                        if (view.spawnChoiceBox(language.getFromTag(this.getPreferredLanguage(), "string_confirmation_discardUnsavedChanges")) == true) {
                            model.setDataChanged(false);
                            try {
                                model.getLevelCollection().loadLevelPackFromFile(chooser.getSelectedFile().getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                                view.spawnMsgBox("ERROR", e.toString());
                                return;
                            }
                        }
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
                    }
                }
                //</editor-fold>
                break;
            case "menu_file_exportLevel":
                //<editor-fold defaultstate="collapsed" desc="Export level">
                chooser.setFileFilter(levelFilter);
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    byte[] buff = model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).toByteArray();
                    File file = new File(chooser.getSelectedFile().getAbsolutePath());
                    try (OutputStream os = new FileOutputStream(file)) {
                        os.write(buff);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.spawnMsgBox("ERROR", e.toString());
                    }
                }
                //</editor-fold>
                break;
            case "menu_file_importLevel":
                //<editor-fold defaultstate="collapsed" desc="Import level">              
                chooser.setFileFilter(levelFilter);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (model.getDataChanged()) {
                        if (view.spawnChoiceBox(language.getFromTag(this.getPreferredLanguage(), "string_confirmation_discardUnsavedChanges")) == false) {
                            break;
                        }
                        model.setDataChanged(false);
                    }
                    try {
                        byte[] array = Files.readAllBytes(Paths.get(chooser.getSelectedFile().getAbsolutePath()));
                        model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).fromByteArray(array);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.spawnMsgBox("ERROR", e.toString());
                    }

                }
                //</editor-fold>
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
        }
        // View Menu
        switch (source.getName()) {
            case "menu_view_decZoom":
                model.setZoomLevel(model.getZoomLevel() - 1);
                break;
            case "menu_view_incZoom":
                model.setZoomLevel(model.getZoomLevel() + 1);
                break;
            case "menu_view_resZoom":
                model.setZoomLevel(-1);
                break;
        }
        // Help menu
        switch (source.getName()) {
            case "menu_help_languageItem":
                this.setPreferredLanguage(source.getText());
                break;
            case "menu_help_about":
                view.spawnAboutBox();
                break;
        }
        view.refresh();
    }
    /**
     * Get the current level slot
     * @return 
     */
    public Integer getCurrentLevelSLot() {
        return model.getCurrentLevelSlot();
    }
    /**
     * set the current level slot
     * @param value 
     */
    public void setCurrentLevelSlot(int value) {
        model.setCurrentLevelSlot(value);
        view.refresh();
    }

    /**
     * Set selected special port in the UI
     *
     * @param currentSpecialPort
     */
    public void setCurrentSpecialPort(Integer currentSpecialPort) {
        model.setCurrentSpecialPort(currentSpecialPort);
        view.refresh();
    }

    /**
     * Return current special port
     *
     * @return
     */
    public Integer getCurrentSpecialPort() {
        return model.getCurrentSpecialPort();
    }

    /**
     * Retrieves the user prefered language loaded from config.properties
     *
     * @return
     */
    public String getPreferredLanguage() {
        return model.getProperty("language");
    }

    /**
     * Language strings here are stores in config.properties. These language
     * strings are retrieved from [language].properties files (see
     * /languages/*.properties for an example)
     *
     * @param language
     */
    public void setPreferredLanguage(String language) {
        model.setProperty("language", language);
        view.refresh();
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
    /**
     * 
     * @param point 
     */
    @Override
    public void tileHovered(Point point) {
        model.setCurrentHoveredPoint(point);
        Tile tile = model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).getTile(point);
        // we are gonna directly manipulate the view to update the x/y coordinates for this instead of redrawing the whole UI.
        // calling view.refresh() for mouse movement sometimes drops other mouse input.
        view.statusBarX.setText("x:" + point.x);
        view.statusBarY.setText("y:" + point.y);
        language.setComponentTranslation(this.getPreferredLanguage(), view.hoveredTile);
        view.hoveredTile.setText(view.hoveredTile.getText() + ": " + tile.getNiceName());
        view.hoveredTile.setHorizontalTextPosition(JLabel.LEFT);
        view.hoveredTile.setIcon(model.getTiles().get(tile));
        view.hoveredTile.setIconTextGap(5);
    }

    @Override
    public void pencilSelection(LinkedHashSet<Point> ps, Integer click) {
        Level level = model.getLevelCollection().getLevel(this.getCurrentLevelSLot());
        if (model.getDrawMode() != EditorController.drawMode.pencil) {
            return;
        }
        if (click == MouseEvent.BUTTON1) {
            for (Point pt : ps) {
                level.setTile(pt, model.getSelectedTile());
            }
        } else if (click == MouseEvent.BUTTON3) {
            for (Point pt : ps) {
                level.setTile(pt, StandardTiles.VOID);
            }
        }
        model.setDataChanged(true);
        view.refresh();
    }

    @Override
    public void lineSelection(LinkedHashSet<Point> ps, Integer click) {
        Level level = model.getLevelCollection().getLevel(this.getCurrentLevelSLot());
        if (model.getDrawMode() != EditorController.drawMode.line) {
            return;
        }
        if (click == MouseEvent.BUTTON1) {
            for (Point pt : ps) {
                level.setTile(pt, model.getSelectedTile());
            }
        } else if (click == MouseEvent.BUTTON3) {
            for (Point pt : ps) {
                level.setTile(pt, StandardTiles.VOID);
            }
        }
        model.setDataChanged(true);
        view.refresh();
    }

    @Override
    public void squareSelection(LinkedHashSet<Point> ps, Integer click) {
        Level level = model.getLevelCollection().getLevel(this.getCurrentLevelSLot());
        if (model.getDrawMode() != EditorController.drawMode.rect) {
            return;
        }
        if (click == MouseEvent.BUTTON1) {
            for (Point pt : ps) {
                level.setTile(pt, model.getSelectedTile());
            }
        } else if (click == MouseEvent.BUTTON3) {
            for (Point pt : ps) {
                level.setTile(pt, StandardTiles.VOID);
            }
        }
        model.setDataChanged(true);
        view.refresh();
    }

    @Override
    public void fillSelection(LinkedHashSet<Point> ps, Integer click) {
        Level level = model.getLevelCollection().getLevel(this.getCurrentLevelSLot());
        if (model.getDrawMode() != EditorController.drawMode.fill) {
            return;
        }
        if (click == MouseEvent.BUTTON1) {
            for (Point pt : ps) {
                level.setTile(pt, model.getSelectedTile());
            }
        } else if (click == MouseEvent.BUTTON3) {
            for (Point pt : ps) {
                level.setTile(pt, StandardTiles.VOID);
            }
        }
        model.setDataChanged(true);
        view.refresh();
    }
    /**
     * scan the level for exit tiles
     * @return 
     */
    public Integer getExitCount() {
        Integer count = 0;
        for (int x = 0; x < 60; x++) {
            for (int y = 0; y < 24; y++) {
                if (model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).getTile(new Point(x, y)).equals(StandardTiles.EXIT)) {

                    count++;
                }
            }
        }
        return count;
    }
    /**
     * scan the level for murphy tiles
     * @return 
     */
    public Integer getMurphyCount() {
        Integer count = 0;
        for (int x = 0; x < 60; x++) {
            for (int y = 0; y < 24; y++) {
                if (model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).getTile(new Point(x, y)).equals(StandardTiles.MURPHY)) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * scan the level for infotron tiles
     * @return 
     */
    public Integer getInfotronCount() {
        Integer count = 0;
        for (int x = 0; x < 60; x++) {
            for (int y = 0; y < 24; y++) {
                if (model.getLevelCollection().getLevel(this.getCurrentLevelSLot()).getTile(new Point(x, y)).equals(StandardTiles.INFOTRON)) {

                    count++;
                }
            }
        }
        return count;
    }
}
