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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Mokyu
 */
public class EditorController implements ActionListener {

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

    public void updateView() {
        EventQueue.invokeLater(() -> {
        });
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
        }

    }

    private void buttonHandler(JButton source) {
        System.out.println("Button press: " + source.getName());
    }

    private void menuHandler(JMenuItem source) {
        System.out.println("Menu item: " + source.getName());
        switch (source.getName()) {
            case "menu_file_exit":
                this.view.dispose();
                System.exit(0);
                break;
        }
    }

    // functionality
    public String getPreferredLanguage() {
        return model.getProperty("language");
    }
    
    public void setPreferredLanguage(String language) {
        model.setProperty("language", language);
        model.fireChanged();
    }

}
