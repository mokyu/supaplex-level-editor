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

    private final EditorController controller;
    private final EditorModel model;

    public EditorView(EditorController controller, EditorModel model) {
        this.controller = controller;
        this.model = model;
        init();
    }

    private void init() {
        this.labels = new HashMap<>();
        this.buttons = new HashMap<>();
        this.dropdowns = new HashMap<>();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Supaplex Level Editor");
        this.setMinimumSize(new Dimension(1600, 900));
        this.addMouseListener(new MouseAdapter() { // reliably trigger the (un)focusevent on JTextField to parse changes
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

        // initialize level view
        initLevelView();

        // initialize tools
        initTools();

        // initialize tiles
        initTiles();
        // set object translations
        labels.keySet().forEach((String key) -> {
            language.setComponentTranslation(controller.getPreferredLanguage(), labels.get(key));
        });
        buttons.keySet().forEach((String key) -> {
            language.setComponentTranslation(controller.getPreferredLanguage(), buttons.get(key));
        });

    }

    private void initTools() {
        JPanel toolSet = new JPanel(new GridLayout(4, 1));

        JButton button;
        toolSet.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_toolSet")));
        toolContainer.add(toolSet);

        button = new JButton(language.getFromTag(controller.getPreferredLanguage(), "button_toolSet_pencilTool"));
        button.setName("button_toolSet_pencilTool");
        button.addActionListener(controller);
        button.setEnabled(model.getDrawMode() != EditorController.drawMode.pencil);
        language.setComponentTranslation(controller.getPreferredLanguage(), button);
        toolSet.add(button);

        button = new JButton(language.getFromTag(controller.getPreferredLanguage(), "button_toolSet_lineTool"));
        button.setName("button_toolSet_lineTool");
        button.addActionListener(controller);
        button.setEnabled(model.getDrawMode() != EditorController.drawMode.line);
        language.setComponentTranslation(controller.getPreferredLanguage(), button);
        toolSet.add(button);

        button = new JButton(language.getFromTag(controller.getPreferredLanguage(), "button_toolSet_rectangleTool"));
        button.setName("button_toolSet_rectangleTool");
        button.addActionListener(controller);
        button.setEnabled(model.getDrawMode() != EditorController.drawMode.rect);
        language.setComponentTranslation(controller.getPreferredLanguage(), button);
        toolSet.add(button);

        button = new JButton(language.getFromTag(controller.getPreferredLanguage(), "button_toolSet_fillTool"));
        button.setName("button_toolSet_fillTool");
        button.addActionListener(controller);
        button.setEnabled(model.getDrawMode() != EditorController.drawMode.fill);
        language.setComponentTranslation(controller.getPreferredLanguage(), button);
        toolSet.add(button);
    }

    private void initTiles() {
        JButton x;
        Tile tile;
        //<editor-fold defaultstate="collapsed" desc="basic tiles">
        JPanel basic = new JPanel(new GridLayout(3, 3));
        basic.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_toolSet_basic")));
        toolContainer.add(basic);

        x = new JButton();
        tile = StandardTiles.BASE;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.ZONK;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.MURPHY;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.INFOTRON;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.EXIT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.SNIK_SNAK;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.ELECTRON;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.BUG;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        x = new JButton();
        tile = StandardTiles.VOID;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        basic.add(x);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="floppy tiles">
        JPanel floppies = new JPanel(new GridLayout(2, 2));
        floppies.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_toolSet_floppy")));
        toolContainer.add(floppies);

        x = new JButton();
        tile = StandardTiles.UTILITY_DISK_RED;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        floppies.add(x);

        x = new JButton();
        tile = StandardTiles.UTILITY_DISK_ORANGE;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        floppies.add(x);

        x = new JButton();
        tile = StandardTiles.UTILITY_DISK_YELLOW;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        floppies.add(x);

        x = new JButton();
        tile = StandardTiles.TERMINAL;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        floppies.add(x);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="port tiles">
        JPanel ports = new JPanel(new GridLayout(3, 4));
        ports.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_toolSet_ports")));
        toolContainer.add(ports);

        x = new JButton();
        tile = StandardTiles.PORT_UP;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.PORT_RIGHT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.PORT_DOWN;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.PORT_LEFT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.SPECIAL_PORT_UP;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.SPECIAL_PORT_RIGHT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.SPECIAL_PORT_DOWN;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.SPECIAL_PORT_LEFT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.PORT_HORIZONTAL;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.PORT_VERTICAL;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        x = new JButton();
        tile = StandardTiles.PORT_COMPLETE;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        ports.add(x);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="decoration tiles">
        JPanel decoration = new JPanel(new GridLayout(4, 5));
        decoration.setBorder(BorderFactory.createTitledBorder(language.getFromTag(controller.getPreferredLanguage(), "panel_toolSet_decoration")));
        toolContainer.add(decoration);

        x = new JButton();
        tile = StandardTiles.RAM_CHIP_STANDARD;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.RAM_CHIP_LEFT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.RAM_CHIP_RIGHT;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.RAM_CHIP_UP;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.RAM_CHIP_DOWN;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_STANDARD;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_0;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_1;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_2;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_3;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_4;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_5;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_6;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_7;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_8;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.HARDWARE_9;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        x = new JButton();
        tile = StandardTiles.INVISIBLE_WALL;
        x.addActionListener(controller);
        x.setName(tile.getNiceName());
        x.setToolTipText(tile.getNiceName());
        x.setIcon(model.getTiles().get(tile));
        decoration.add(x);

        //</editor-fold>
    }

    private void initLevelView() {
        JLevelView a = new JLevelView(model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()), model.getTileSetLevelView(), model.getZoomLevel(), model.getSelectedTile());
        a.addListener(controller);
        editorContainer.setLayout(new BorderLayout());
        editorContainer.add(a, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(a);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorContainer.add(scroll);

        /* We need to attach listeners to the scroll bars because we need to restore scroll position when redrawing the UI */
        scroll.getViewport().setViewPosition(model.getScrollPos());
        scroll.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int x = e.getValue();
                java.awt.Point pt = model.getScrollPos();
                pt.x = x;
                model.setScrollPos(pt);
            }
        });
        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int y = e.getValue();
                java.awt.Point pt = model.getScrollPos();
                pt.y = y;
                model.setScrollPos(pt);
            }
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
        menuItem.setName("menu_file_newLevelCol");                              // New level collection
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_loadLevelCol");                             // Load level collection
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_saveLevelCol");                             // Save Level collection
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_exportLevel");                              // Export current level
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_importLevel");                              // Import current level
        fileMenu.add(menuItem);

        fileMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_file_exit");                                     // Exit
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_currZoom");                                 // Current Zoom Level
        viewMenu.add(menuItem);

        viewMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_decZoom");                                  // Decrease Zoom Level
        viewMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_incZoom");                                  // Increase Zoom level
        viewMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_view_resZoom");                                  // Reset Zoom level
        viewMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setName("menu_help_language");                                 // Languages
        helpMenu.add(menuItem);

        for (String lang : language.getLibrary().keySet()) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem();
            item.setName("menu_help_languageItem");
            item.setSelected(controller.getPreferredLanguage().equals(lang));
            item.setText(lang);
            item.addActionListener(controller);
            helpMenu.add(item);
        }

        helpMenu.addSeparator();                                                // --------------------

        menuItem = new JMenuItem();
        menuItem.setName("menu_help_about");                                    // About
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
        toolContainer.setLayout(new GridLayout(5, 1));

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
        mokyu.libsupaplex.Point point = model.getCurrentHoveredPoint();
        Tile tile = null;
        if (point != null) {
            tile = model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getTile(point);
        } else {
            point = new mokyu.libsupaplex.Point(0, 0);
            tile = StandardTiles.VOID;
        }

        this.statusBarX = new JLabel();
        statusBar.add(this.statusBarX);
        this.statusBarX.setText("x:" + point.x);
        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        this.statusBarY = new JLabel();
        statusBar.add(this.statusBarY);
        this.statusBarY.setText(this.statusBarX.getText() + ":" + point.y);
        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        JLabel x = new JLabel();
        x.setName("label_statusBar_noExitWarning");
        statusBar.add(x);
        labels.put(x.getName(), x);
        x.setVisible((controller.getExitCount() == 0));
        language.setComponentTranslation(controller.getPreferredLanguage(), x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // warning for missing murphy
        x = new JLabel();
        x.setName("label_statusBar_noMurphyWarning");
        labels.put(x.getName(), x);
        x.setVisible((controller.getMurphyCount() == 0));
        statusBar.add(x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // warning for not enough infotrons
        x = new JLabel();
        x.setName("label_statusBar_notEnoughInfotronsWarning");
        language.setComponentTranslation(controller.getPreferredLanguage(), x);
        x.setText(x.getText() + " (" + controller.getInfotronCount() + "/" + model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getRequiredInfotrons() + ")]");
        x.setVisible((controller.getInfotronCount() < model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getRequiredInfotrons()));
        statusBar.add(x);

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // hovered tile
        this.hoveredTile = new JLabel();
        this.hoveredTile.setVisible(true);
        this.hoveredTile.setName("label_statusBar_hoveredTile");
        statusBar.add(this.hoveredTile);
        language.setComponentTranslation(controller.getPreferredLanguage(), this.hoveredTile);
        this.hoveredTile.setText(this.hoveredTile.getText() + ": " + tile.getNiceName());
        this.hoveredTile.setHorizontalTextPosition(JLabel.LEFT);
        this.hoveredTile.setIcon(model.getTiles().get(tile));
        this.hoveredTile.setIconTextGap(5);
        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        // selected tile
        x = new JLabel();
        x.setVisible(true);
        x.setName("label_statusBar_selectedTile");
        language.setComponentTranslation(controller.getPreferredLanguage(), x);
        x.setText(x.getText() + ": " + model.getSelectedTile().getNiceName());
        x.setHorizontalTextPosition(JLabel.LEFT);
        x.setIcon(model.getTiles().get(model.getSelectedTile()));
        x.setIconTextGap(5);
        statusBar.add(x);
    }

    private void initLevelData() {
        JPanel panel = levelSettingsContainer;
        JPanel childPanel;
        JLabel label;
        JCheckBox checkBox;
        JComboBox<String> comboBox;
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
        label.setPreferredSize(new Dimension(150, 20));
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        childPanel.add(label, c);
        labels.put(label.getName(), label);
        comboBox = new JComboBox<>(controller.getLevelList().toArray(new String[0]));
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
        label.setPreferredSize(new Dimension(150, 20));
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
        // get current gravity switch port
        GravitySwitchPort port = model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getGravitySwitchPortData(controller.getCurrentSpecialPort());

        //<editor-fold defaultstate="collapsed" desc="Special port selection">
        label = new JLabel();
        label.setName("label_levelData_gravitySwitchPortCount");
        label.setPreferredSize(new Dimension(180, 20));
        labels.put(label.getName(), label);
        c.gridx = 0;
        c.gridy = 0;
        childPanel.add(label, c);

        textField = new JTextField(Integer.toString(model.getLevelCollection().getLevel(controller.getCurrentLevelSLot()).getGravitySwitchPorts()));
        textField.setName("textField_levelData_gravitySwitchPortCount");
        textField.setPreferredSize(new Dimension(30, 20));
        textField.addFocusListener(controller);
        c.gridx = 1;
        c.gridy = 0;
        childPanel.add(textField, c);

        label = new JLabel();
        label.setName("label_levelData_gravityPortSelection");
        label.setPreferredSize(new Dimension(150, 20));
        labels.put(label.getName(), label);
        c.gridx = 2;
        c.gridy = 0;
        childPanel.add(label, c);

        textField = new JTextField(Integer.toString(controller.getCurrentSpecialPort()));
        textField.setName("textField_levelData_gravitySwitchPortSelection");
        textField.setPreferredSize(new Dimension(30, 20));
        textField.addFocusListener(controller);
        c.gridx = 3;
        c.gridy = 0;
        childPanel.add(textField, c);

        label = new JLabel();
        label.setName("label_levelData_gravityPortX");
        label.setPreferredSize(new Dimension(20, 20));
        labels.put(label.getName(), label);
        c.gridx = 4;
        c.gridy = 0;
        childPanel.add(label, c);

        textField = new JTextField(Integer.toString(port.getX()));
        textField.setName("textField_levelData_gravitySwitchPortX");
        textField.setPreferredSize(new Dimension(30, 20));
        textField.addFocusListener(controller);
        c.gridx = 5;
        c.gridy = 0;
        childPanel.add(textField, c);

        label = new JLabel();
        label.setName("label_levelData_gravityPortY");
        label.setPreferredSize(new Dimension(20, 20));
        labels.put(label.getName(), label);
        c.gridx = 6;
        c.gridy = 0;
        childPanel.add(label, c);

        textField = new JTextField(Integer.toString(port.getY()));
        textField.setName("textField_levelData_gravitySwitchPortY");
        textField.setPreferredSize(new Dimension(30, 20));
        textField.addFocusListener(controller);
        c.gridx = 7;
        c.gridy = 0;
        childPanel.add(textField, c);

        // create the comboboxes
        //</editor-fold>
       
        //<editor-fold defaultstate="collapsed" desc="Special port flags">
        // create the checkboxes
        checkBox = new JCheckBox(language.getFromTag(controller.getPreferredLanguage(), "checkBox_levelData_specialPortToggleGravity"), port.getGravity());
        checkBox.addItemListener(controller);
        checkBox.setName("checkBox_levelData_specialPortToggleGravity");
        c.insets = new Insets(20, 0, 0, 0); // top padding
        c.fill = GridBagConstraints.REMAINDER;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        childPanel.add(checkBox, c);

        checkBox = new JCheckBox(language.getFromTag(controller.getPreferredLanguage(), "checkBox_levelData_specialPortToggleFreezeEnemy"), port.getFreezeEnemy());
        checkBox.addItemListener(controller);
        checkBox.setName("checkBox_levelData_specialPortToggleFreezeEnemy");
        c.gridx = 1;
        c.gridy = 1;
        childPanel.add(checkBox, c);

        checkBox = new JCheckBox(language.getFromTag(controller.getPreferredLanguage(), "checkBox_levelData_specialPortToggleFreezeZonks"), port.getFreezeZonks());
        checkBox.addItemListener(controller);
        checkBox.setName("checkBox_levelData_specialPortToggleFreezeZonks");
        c.gridx = 3;
        c.gridy = 1;
        childPanel.add(checkBox, c);
        //</editor-fold>

        panel.add(childPanel);
    }

    /**
     * When we make changes to the model we will redraw the whole UI.
     * Because our UI is relatively simple we can get away with it with minimal performance loss
     */
    public void refresh() {
        this.getContentPane().removeAll();
        init();
        this.repaint();

    }

    /*
     * Spawn a mess
     * @param type "ERROR", "WARNING" and any other value for a regular info
     * box.
     * @param content just a string
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
    
    public void spawnAboutBox() {
        About about = new About();
        about.versionLabel.setText(Information.VERSION);
        about.setVisible(true);
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
    private JPanel tools;
    private JPanel tiles;
    private HashMap<String, JLabel> labels;
    private HashMap<String, JButton> buttons;
    private HashMap<String, JComboBox> dropdowns;
    private HashMap<String, JSpinner> spinners;
    private HashMap<String, JCheckBox> checkboxes;
    public JLabel statusBarX;
    public JLabel statusBarY;
    public JLabel hoveredTile;
}
