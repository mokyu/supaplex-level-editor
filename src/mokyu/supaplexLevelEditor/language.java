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
     * @param language User defined string representing the language e.g.
     * "Nederlands (Dutch)"
     * @param o JComponent like JButton, JMenu, JMenuItem, jLabel
     */
    private static final HashMap<String, HashMap<String, String>> library = new HashMap<>();

    public static HashMap<String, HashMap<String, String>> getLibrary() {
        return library;
    }

    /**
     * The objects 'name' field is used to find the appropriate text based on
     * the language and uses it to set the text field of the component. When no
     * translation is found, the 'name' of the object is stored in the text
     * field together with the target language.
     *
     * @param language String containing a language name. see "english
     * (us).properties" "language = English"
     * @param o JButton, JMenuItem, JLabel, JMenu
     */
    public static void setComponentTranslation(String language, Object o) {

        String translation;
        switch (o.getClass().getName()) {
            case "javax.swing.JButton":
                translation = getFromTag(language, ((JButton) o).getName());
                ((JButton) o).setText(translation);
                break;
            case "javax.swing.JMenuItem":
                translation = getFromTag(language, ((JMenuItem) o).getName());
                if (translation == null) {
                    return;
                }
                ((JMenuItem) o).setText(translation);
                break;
            case "javax.swing.JMenu":
                translation = getFromTag(language, ((JMenu) o).getName());
                if (translation == null) {
                    return;
                }
                ((JMenu) o).setText(translation);
                break;
            case "javax.swing.JLabel":
                translation = getFromTag(language, ((JLabel) o).getName());
                if (translation == null) {
                    return;
                }
                ((JLabel) o).setText(translation);
                break;
        }
    }
    /**
     * Some elements do not have a name field or we just need to get the translation string itself (e.g. confirmation boxes)
     * @param language target language
     * @param tag the identifier used to get the translation (e.g.: "label_statusBar_noMurphyWarning")
     * @return 
     */
    public static String getFromTag(String language, String tag) {
        if (tag == null) {
            return null;
        }
        HashMap<String, String> translations = library.get(language);
        if (translations == null || translations.isEmpty()) {
            return tag + " " + language;
        } else {
            String result = translations.get(tag);
            if (result != null) {
                return result;
            }
            return tag + " " + language;
        }
    }

    /**
     * Parses all properties files in the ./translations folder and populates
     * the library hashmap
     *
     * @throws java.io.FileNotFoundException Throws when ./languages folder does
     * not exist
     * @throws java.io.IOException Thrown when i fail to read a file.
     */
    public static void getTranslations() throws FileNotFoundException, IOException {

        File dir = new File("languages");
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            throw new FileNotFoundException("Fatal error: Missing './languages' folder in our CWD");
        }
        File[] listing = dir.listFiles();
        if (listing != null) {
            for (File item : listing) {
                Properties prop;
                try (InputStream fstream = new FileInputStream(item)) {
                    prop = new Properties();
                    prop.load(new InputStreamReader(fstream, "UTF-8"));
                    fstream.close();
                }
                library.put(prop.getProperty("language"), new HashMap<>());
                Set<String> keys = prop.stringPropertyNames();
                for (String key : keys) {
                    library.get(prop.getProperty("language")).put(key, prop.getProperty(key));
                }

            }
        }

    }
}
