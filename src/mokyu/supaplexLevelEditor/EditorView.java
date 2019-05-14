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
import javax.swing.border.BevelBorder;
import javax.swing.Box;
import java.util.*;

/**
 *
 * @author Mokyu
 */
public class EditorView extends javax.swing.JFrame {

    private EditorController controller;

    public EditorView(EditorController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Supaplex Level Editor");
        this.setMinimumSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Create menubar
        initMenuBar();

        // create container frames 
        initFrames();

        // create and initialize other components
        initElements();

        pack();
    }

    private void initMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu, viewMenu, helpMenu;
        JMenuItem newLevel, newLevelCol, loadLevel, loadLevelCol, saveLevel, saveLevelCol, exit, currZoom, incZoom, decZoom, resZoom, about;
        fileMenu = new JMenu();
        fileMenu.setName("menu_file");
        language.setComponentTranslation(controller.getPreferredLanguage(), fileMenu);
        viewMenu = new JMenu();
        viewMenu.setName("menu_view");
        language.setComponentTranslation(controller.getPreferredLanguage(), viewMenu);
        helpMenu = new JMenu();
        helpMenu.setName("menu_help");
        language.setComponentTranslation(controller.getPreferredLanguage(), helpMenu);

        newLevel = new JMenuItem();
        newLevel.setName("menu_file_newLevel");
        fileMenu.add(newLevel);
        newLevelCol = new JMenuItem();
        newLevelCol.setName("menu_file_newLevelCol");
        fileMenu.add(newLevelCol);

        fileMenu.addSeparator();

        loadLevel = new JMenuItem();
        loadLevel.setName("menu_file_loadLevel");
        fileMenu.add(loadLevel);

        loadLevelCol = new JMenuItem();
        loadLevelCol.setName("menu_file_loadLevelCol");
        fileMenu.add(loadLevelCol);

        fileMenu.addSeparator();
        saveLevel = new JMenuItem();
        saveLevel.setName("menu_file_saveLevel");
        fileMenu.add(saveLevel);

        saveLevelCol = new JMenuItem();
        saveLevelCol.setName("menu_file_saveLevelCol");
        fileMenu.add(saveLevelCol);

        fileMenu.addSeparator();
        exit = new JMenuItem();
        exit.setName("menu_file_exit");
        fileMenu.add(exit);

        currZoom = new JMenuItem();
        currZoom.setName("menu_view_currZoom");
        viewMenu.add(currZoom);
        viewMenu.addSeparator();

        incZoom = new JMenuItem();
        incZoom.setName("menu_view_decZoom");
        viewMenu.add(incZoom);

        decZoom = new JMenuItem();
        decZoom.setName("menu_view_decZoom");
        viewMenu.add(decZoom);

        resZoom = new JMenuItem();
        resZoom.setName("menu_view_resZoom");
        viewMenu.add(resZoom);

        about = new JMenuItem();
        about.setName("menu_help_about");
        helpMenu.add(about);

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

        toolContainer.setPreferredSize(new Dimension(100, 768));
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

    private void initElements() {
        labels = new HashMap<>();
        buttons = new HashMap<>();
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
        language.setComponentTranslation(controller.getPreferredLanguage(), x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        x = new JLabel();
        x.setName("label_statusBar_yCoord");
        statusBar.add(x);
        labels.put(x.getName(), x);
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

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // apply proper translations
        for (String key : labels.keySet()) {
            language.setComponentTranslation(controller.getPreferredLanguage(), labels.get(key));
        }
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
     * Show or hide the missing murphy warning. (Thread safe)
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

    private JPanel toolContainer;
    private JPanel editorContainer;
    private JPanel levelSettingsContainer;
    private JPanel statusBar;
    private HashMap<String, JLabel> labels;
    private HashMap<String, JButton> buttons;
    private HashMap<Tile, ImageIcon> icons;
}
