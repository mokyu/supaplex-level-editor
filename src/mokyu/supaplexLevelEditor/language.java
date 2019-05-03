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

import java.util.*;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author Mokyu
 */
public class language {

    /**
     * Automatically assigns language specific Text for UI items
     *
     * @param language ISO 639-1 language as string e.g.: "en"
     * @param o JComponent like JButton, JMenu, JMenuItem, jLabel
     */
    private static final HashMap<String, HashMap<String, String>> library = new HashMap<>();

    public static void setComponentTranslation(String language, Object o) {
        String translation;
        switch (o.getClass().getName()) {
            case "javax.swing.JButton":
                translation = getFromTag(language, ((JButton) o).getName());
                ((JButton) o).setText(translation);
                break;
            case "javax.swing.JMenuItem":
                translation = getFromTag(language, ((JMenuItem) o).getName());
                ((JMenuItem) o).setText(translation);
                break;
            case "javax.swing.JMenu":
                translation = getFromTag(language, ((JMenu) o).getName());
                ((JMenu) o).setText(translation);
                break;
        }
    }

    public static String getFromTag(String language, String tag) {
        HashMap<String, String> translations = library.get(language);
        if (translations == null || translations.isEmpty()) {
            return tag + " " + language;
        } else {
            String result = translations.get(tag);
            if(result != null) {
                return result;
            }
            return tag+ " " + language;
        }
    }

    /**
     * Parses all json files in the ./translations folder and populates the
     * library hashmap
     *
     * @throws java.io.FileNotFoundException Throws when ./languages folder does
     * not exist
     * @throws java.io.IOException Thrown when i fail to read a file.
     */
    public static void getTranslations() throws FileNotFoundException, IOException {
        
        File dir = new File("languages");
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Current working directory: "+ System.getProperty("user.dir"));
            throw new FileNotFoundException("Fatal error: Missing './languages' folder in our CWD");
        }
        File[] listing = dir.listFiles();
        if (listing != null) {
            for (File item : listing) {
                InputStream fstream = new FileInputStream(item);
                Properties prop = new Properties();
                prop.load(fstream);
                fstream.close();
                library.put(prop.getProperty("language"), new HashMap<>());
                Set<String> keys = prop.stringPropertyNames();
                for (String key : keys) {
                    library.get(prop.getProperty("language")).put(key, prop.getProperty(key));
                }

            }
        }

    }
}
