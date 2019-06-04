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

import mokyu.libsupaplex.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.Box;
import java.util.*;

/**
 *
 * @author Mokyu
 */
public class EditorView extends javax.swing.JFrame {

    private EditorController controller;
    private EditorModel model;

    public EditorView(EditorController controller, EditorModel model) {
        this.controller = controller;
        this.model = model;
        init();
    }

    private void init() {
        this.labels = new HashMap<>();
        this.buttons = new HashMap<>();
        this.icons = new HashMap<>();
        this.dropdowns = new HashMap<>();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Supaplex Level Editor");
        this.setMinimumSize(new Dimension(800, 600));
        this.addMouseListener(new MouseAdapter() { // to reliably trigger the (un)focusevent on JTextField
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame clicked = (JFrame) e.getSource();
                clicked.requestFocusInWindow();
            }
        });

        setLayout(new BorderLayout());

        // Create menubar
        initMenuBar();

        // create container frames 
        initFrames();

        // Initialize status bar
        initStatusBar();

        // Initialize level data window
        initLevelData();

        // set object translations
        labels.keySet().forEach((String key) -> {
            language.setComponentTranslation(controller.getPreferredLanguage(), labels.get(key));
        });
        buttons.keySet().forEach((String key) -> {
            language.setComponentTranslation(controller.getPreferredLanguage(), buttons.get(key));
        });

    }

    private void initMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu, viewMenu, helpMenu;
        JMenuItem menuItem;
        fileMenu = new JMenu();
        fileMenu.setName("menu_file");
        language.setComponentTranslation(controller.getPreferredLanguage(), fileMenu);
        viewMenu = new JMenu();
        viewMenu.setName("menu_view");
        language.setComponentTranslation(controller.getPreferredLanguage(), viewMenu);
        helpMenu = new JMenu();
        helpMenu.setName("menu_help");
        language.setComponentTranslation(controller.getPreferredLanguage(), helpMenu);

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_newLevelCol");                                  // New level collection
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_loadLevelCol");                                 // Load level collection
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_saveLevelCol");                                 // Save Level collection
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_exportLevel");                                  // Export current level
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_importLevel");                                  // Import current level
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_exit");                                         // Exit
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_currZoom");                                     // Current Zoom Level
        viewMenu.add(menuItem);

        viewMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_decZoom");                                      // Decrease Zoom Level
        viewMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_incZoom");                                      // Increase Zoom level
        viewMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_resZoom");                                      // Reset Zoom level
        viewMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_help_about");                                        // About
        helpMenu.add(menuItem);

        menu.add(fileMenu);
        menu.add(viewMenu);
        menu.add(helpMenu);

        for (int i = 0; i < fileMenu.getItemCount(); i++) {
            JMenuItem item = fileMenu.getItem(i);
            if (item != null) {
                item.addActionListener(controller);
                language.setComponentTranslation(controller.getPreferredLanguage(), item);
            }

        }
        for (int i = 0; i < viewMenu.getItemCount(); i++) {
            JMenuItem item = viewMenu.getItem(i);
            if (item != null) {
                item.addActionListener(controller);
                language.setComponentTranslation(controller.getPreferredLanguage(), item);
            }
        }
        for (int i = 0; i < helpMenu.getItemCount(); i++) {
            JMenuItem item = helpMenu.getItem(i);
            if (item != null) {
                item.addActionListener(controller);
                language.setComponentTranslation(controller.getPreferredLanguage(), item);
            }
        }
        this.setJMenuBar(menu);
    }

    private void initFrames() {
        toolContainer = new JPanel();
        toolContainer.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_tools")));

        toolContainer.setPreferredSize(new Dimension(175, 768));
        add(toolContainer, BorderLayout.WEST);

        editorContainer = new JPanel();
        editorContainer.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_levelView")));
        editorContainer.setPreferredSize(new Dimension(1280, 720));
        add(editorContainer, BorderLayout.CENTER);

        levelSettingsContainer = new JPanel();
        levelSettingsContainer.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_levelMetaData")));
        levelSettingsContainer.setPreferredSize(new Dimension(1280, 100));
        add(levelSettingsContainer, BorderLayout.NORTH);

        statusBar = new JPanel();
        statusBar.setPreferredSize(new Dimension(1280, 25));
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.LINE_AXIS));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void initStatusBar() {
        labels = new HashMap<>();
        //TODO: initiate buttons, and hook up to controller
        // initiate labels and such
        // and look into the scrollable frame
        // scalable canvas

        // create static labels
        // Status bar elements
        JLabel x = new JLabel();
        x.setName("label_statusBar_xCoord");
        statusBar.add(x);
        labels.put(x.getName(), x);
        x.setVisible(false);
        language.setComponentTranslation(controller.getPreferredLanguage(), x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        x = new JLabel();
        x.setName("label_statusBar_yCoord");
        statusBar.add(x);
        labels.put(x.getName(), x);
        x.setVisible(false);
        language.setComponentTranslation(controller.getPreferredLanguage(), x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        x = new JLabel();
        x.setName("label_statusBar_noExitWarning");
        statusBar.add(x);
        labels.put(x.getName(), x);
        x.setVisible(false);
        language.setComponentTranslation(controller.getPreferredLanguage(), x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // warning for missing murphy
        x = new JLabel();
        x.setName("label_statusBar_noMurphyWarning");
        labels.put(x.getName(), x);
        x.setVisible(false);
        statusBar.add(x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // hovered tile
        x = new JLabel();
        x.setVisible(false);
        x.setName("label_statusBar_hoveredTile");
        labels.put(x.getName(), x);
        statusBar.add(x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // selected tile
        x = new JLabel();
        x.setVisible(false);
        x.setName("label_statusBar_selectedTile");
        labels.put(x.getName(), x);
        statusBar.add(x);
    }

    private void initLevelData() {
        JPanel panel = levelSettingsContainer;
        JPanel childPanel;
        JLabel label;
        JButton button;
        JCheckBox checkBox;
        JComboBox comboBox;
        JTextField textField;

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints pc = new GridBagConstraints();
        pc.anchor = GridBagConstraints.FIRST_LINE_START;
        pc.fill = GridBagConstraints.EAST;
        pc.weightx = 1;
        pc.weighty = 1;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        childPanel = new JPanel(new GridBagLayout());
        childPanel.setBorder(new EtchedBorder());

        //<editor-fold defaultstate="collapsed" desc="Level selection">
        label = new JLabel();
        label.setName("label_levelData_selectLevelSlot");
        label.setPreferredSize(new Dimension(100, 20));
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        childPanel.add(label, c);
        labels.put(label.getName(), label);

        comboBox = new JComboBox(controller.getLevelList().toArray());
        comboBox.setName("combobox_levelData_selectLevelSlot");
        comboBox.setPreferredSize(new Dimension(200, 20));
        comboBox.setSelectedIndex(controller.getCurrentLevelSLot());
        dropdowns.put(comboBox.getName(), comboBox);
        comboBox.addActionListener(controller);
        c.gridx = 1;
        c.gridy = 0;
        childPanel.add(comboBox, c);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Level Name editing">
        c.insets = new Insets(20, 0, 0, 0); // top padding
        c.gridx = 0;
        c.gridy = 1;
        label = new JLabel();
        label.setName("label_levelData_levelName");
        label.setPreferredSize(new Dimension(100, 20));
        childPanel.add(label, c);
        labels.put(label.getName(), label);

        c.gridx = 1;
        c.gridy = 1;

        textField = new JTextField(model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getName());
        textField.setName("textField_levelData_levelName");
        textField.setPreferredSize(new Dimension(200, 20));
        textField.addFocusListener(controller);
        childPanel.add(textField, c);
        //</editor-fold>

        panel.add(childPanel, BorderLayout.EAST);

        childPanel = new JPanel(new GridBagLayout());
        childPanel.setBorder(new EtchedBorder());

        //<editor-fold defaultstate="collapsed" desc="Required Infotrons">
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        label = new JLabel();
        label.setName("label_levelData_requiredInfotrons");
        label.setPreferredSize(new Dimension(150, 20));
        labels.put(label.getName(), label);
        childPanel.add(label, c);

        textField = new JTextField(Integer.toString(model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getRequiredInfotrons()));
        textField.setName("textField_levelData_requiredInfotrons");
        textField.setPreferredSize(new Dimension(30, 20));
        textField.addFocusListener(controller);
        c.gridx = 1;
        c.gridy = 0;
        childPanel.add(textField, c);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Toggle level gravity">
        label = new JLabel();
        label.setName("label_levelData_gravity");
        label.setPreferredSize(new Dimension(150, 20));
        labels.put(label.getName(), label);
        c.gridx = 0;
        c.gridy = 1;
        childPanel.add(label, c);

        checkBox = new JCheckBox("", model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getGravity());
        checkBox.addItemListener(controller);
        checkBox.setName("checkbox_levelData_gravity");
        c.gridx = 1;
        c.gridy = 1;
        childPanel.add(checkBox, c);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Toggle zonk freeze">
        label = new JLabel();
        label.setName("label_levelData_freezeZonks");
        label.setPreferredSize(new Dimension(100, 20));
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        childPanel.add(label, c);
        labels.put(label.getName(), label);

        checkBox = new JCheckBox("", model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getFreezeZonks());
        checkBox.addItemListener(controller);
        checkBox.setName("checkbox_levelData_freezeZonks");
        c.gridx = 1;
        c.gridy = 2;
        childPanel.add(checkBox, c);
        
        //</editor-fold>
        
        panel.add(childPanel);
        
        childPanel = new JPanel(new GridBagLayout());
        childPanel.setBorder(new EtchedBorder());
        //<editor-fold defaultstate="collapsed" desc="Special port selection">
        // create the comboboxes
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Special port flags">
        // create the checkboxes + 1 
        //</editor-fold>
        //panel.add(childPanel);
    }

    /**
     * Called when model has changed and the UI needs to be updated. Thread Safe
     */
    public void refresh() {
        this.getContentPane().removeAll();
        init();
        this.repaint();
        System.out.println("GUI refresh");

    }

    /**
     * Sets the icon set used for internally rendering sprites in the view
     *
     * @param icons HashMap object containing tiles and their ImageIcon
     */
    public void setIcons(HashMap<Tile, ImageIcon> icons) {
        this.icons = icons;
    }

    /**
     * Change the selected tile icon (Thread safe)
     *
     * @param tile
     */
    public void setSelectedTile(Tile tile) {
        EventQueue.invokeLater(() -> {
            JLabel component = labels.get("label_statusBar_selectedTile");
            if (tile == null) {
                component.setVisible(false);
                return;
            }
            language.setComponentTranslation(controller.getPreferredLanguage(), component);
            component.setVisible(true);
            component.setText(component.getText() + ": " + tile.getNiceName());
            component.setHorizontalTextPosition(JLabel.LEFT);
            component.setIcon(icons.get(tile));
            component.setIconTextGap(5);
        });
    }

    /**
     * Change the hovered tile icon (Thread safe)
     *
     * @param tile
     */
    public void setHoveredTile(Tile tile) {
        EventQueue.invokeLater(() -> {
            JLabel component = labels.get("label_statusBar_hoveredTile");
            if (tile == null) {
                component.setVisible(false);
                return;
            }
            language.setComponentTranslation(controller.getPreferredLanguage(), component);
            component.setVisible(true);
            component.setText(component.getText() + ": " + tile.getNiceName());
            component.setHorizontalTextPosition(JLabel.LEFT);
            component.setIcon(icons.get(tile));
            component.setIconTextGap(5);
        });
    }

    /**
     * Set x coord in the status bar (Thread safe)
     *
     * @param value numeric value for the X axis
     */
    public void setX(Integer value) {
        EventQueue.invokeLater(() -> {
            JLabel component = labels.get("label_statusBar_xCoord");
            if (value == null) {
                component.setVisible(false);
                return;
            }
            component.setVisible(true);
            language.setComponentTranslation(controller.getPreferredLanguage(), component);
            component.setText(component.getText() + ":" + value);
        });
    }

    /**
     * Set y coord in the status bar (Thread safe)
     *
     * @param value numeric value for the Y axis
     */
    public void setY(Integer value) {
        EventQueue.invokeLater(() -> {

            JLabel component = labels.get("label_statusBar_yCoord");
            if (value == null) {
                component.setVisible(false);
                return;
            }
            component.setVisible(true);
            language.setComponentTranslation(controller.getPreferredLanguage(), component);
            component.setText(component.getText() + ":" + value);
        });
    }

    /**
     * Show or hide the missing Murphy warning. (Thread safe)
     *
     * @param value, toggles visibility of the warning text
     */
    public void setMissingMurphy(boolean value) {
        EventQueue.invokeLater(() -> {
            labels.get("label_statusBar_noMurphyWarning").setVisible(value);
        });
    }

    /**
     * Show or hide the missing Exit warning. (Thread safe)
     *
     * @param value, toggles visibility of the warning text
     */
    public void setMissingExit(boolean value) {
        EventQueue.invokeLater(() -> {
            labels.get("label_statusBar_noExitWarning").setVisible(value);
        });
    }

    /**
     * Spawn a message box, not thread safe
     *
     * @param type "ERROR", "WARNING" and any other value for a regular info
     * box.
     * @param content
     */
    public void spawnMsgBox(String type, String content) {
        switch (type) {
            case "ERROR":
                JOptionPane.showMessageDialog(null, content, "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case "WARNING":
                JOptionPane.showMessageDialog(null, content, "Warning", JOptionPane.WARNING_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(null, content, "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
        }

    }

    /**
     * Spawn Yes No Cancel message box
     *
     * @param msg the message to show
     * @return boolean, true when user clicks yes. False on any other action
     */
    public boolean spawnChoiceBox(String msg) {
        return JOptionPane.showConfirmDialog(null, msg) == JOptionPane.YES_OPTION;
    }

    private JPanel toolContainer;
    private JPanel editorContainer;
    private JPanel levelSettingsContainer;
    private JPanel statusBar;
    private HashMap<String, JLabel> labels;
    private HashMap<String, JButton> buttons;
    private HashMap<Tile, ImageIcon> icons;
    private HashMap<String, JComboBox> dropdowns;
    private HashMap<String, JSpinner> spinners;
    private HashMap<String, JCheckBox> checkboxes;
}
