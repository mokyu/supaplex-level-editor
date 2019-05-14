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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import mokyu.libsupaplex.*;

/**
 *
 * @author Mokyu
 */
public class EditorModel {

    // listener 
    public interface Listener {

        public void modelChanged();
    }
    private final List<Listener> listeners = new ArrayList<>();

    public void addListener(EditorModel.Listener view) {
        listeners.remove(view);
        listeners.add(view);
    }

    public void removeListener(EditorModel.Listener view) {
        listeners.remove(view);
    }

    public void fireChanged() {
        listeners.forEach((listener) -> {
            listener.modelChanged();
        });
    }

    // properties
    private Supaplex supaplex;
    private Properties properties;

    public EditorModel() {
        supaplex = new Supaplex();
        properties = new Properties();
        File props = new File("config.properties");
        if (props.exists()) {
            InputStream fstream;
            try {
                fstream = new FileInputStream(props);
                properties.load(fstream);
                fstream.close();
                return;
            } catch (FileNotFoundException ex) {
                properties.setProperty("language", "EN");
                System.out.println(properties.getProperty("language"));
            } catch (IOException ex) {
                properties.setProperty("language", "EN");
            }
        } else {
            setProperty("language", "EN");
        }
    }
    /**
     * Get configuration property from config.properties
     * @param key
     * @return String or null when key not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    /**
     *  Set configuration property in config.properties
     * @param key the key to set
     * @param value they value belonging to the key
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            properties.store(new FileOutputStream("config.properties"), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EditorModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditorModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        fireChanged();
    }

}
