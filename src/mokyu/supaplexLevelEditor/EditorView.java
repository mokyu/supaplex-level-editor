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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author Mokyu
 */
public class EditorView extends javax.swing.JFrame {

    public EditorView(EditorController controller) {
        init(controller);
    }

    private void init(EditorController controller) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Supaplex Level Editor");
        this.setSize(1024, 768);
        setLayout(new BorderLayout());

        // Create menubar
        initMenuBar(controller);

        // create container frames 
        initFrames(controller);

        pack();
    }

    private void initMenuBar(EditorController controller) {
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

    private void initFrames(EditorController controller) {
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
        add(levelSettingsContainer, BorderLayout.SOUTH);
    }

    private JPanel toolContainer;
    private JPanel editorContainer;
    private JPanel levelSettingsContainer;

}
