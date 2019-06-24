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
import javax.swing.ImageIcon;
import mokyu.libsupaplex.*;

/**
 *
 * @author Mokyu
 */
public class EditorModel {

    // properties
    private Supaplex supaplex;
    private Properties properties;
    private boolean dataChanged;
    private HashMap<Tile, ImageIcon> tileSetLevelView;
    private HashMap<Tile, ImageIcon> tiles;
    // UI State related stuff
    private Integer currentLevelSlot;
    private Integer currentSpecialPort;
    private Point currentHoveredPoint;
    private java.awt.Point scrollPos;
    private Tile selectedTile;
    private EditorController.drawMode drawMode;
    private Integer zoomLevel;
    /**
     * return zoomLevel used by JLevelView
     * @return 
     */
    public Integer getZoomLevel() {
        return zoomLevel;
    }
    /**
     * Set zoom level Integer from 1-4
     * @param zoomLevel 
     */
    public void setZoomLevel(Integer zoomLevel) {
        if (zoomLevel == 0 || zoomLevel > 4) {   // ignore zoom above 4 and below 1
            return;
        } else if (zoomLevel == -1) { // set default on -1
            zoomLevel = 2;
        }
        this.zoomLevel = zoomLevel;

    }
    /**
     * get the draw mode
     * @return 
     */
    public EditorController.drawMode getDrawMode() {
        return drawMode;
    }
    /**
     * Set the current draw mode
     * @param drawMode 
     */
    public void setDrawMode(EditorController.drawMode drawMode) {
        this.drawMode = drawMode;
    }

    /**
     * Get the currently selected Tile
     *
     * @return
     */
    public Tile getSelectedTile() {
        return selectedTile;
    }

    /**
     * Set which tile is selected in the statusBar panel
     *
     * @param selectedTile
     */
    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
    }

    /**
     * Get the currently saved scroll position
     *
     * @return
     */
    public java.awt.Point getScrollPos() {
        return scrollPos;
    }

    /**
     * Set the current scroll position, when the user moves the scroll bar you
     * want to call this so the scroll position doesn't get lost on the next gui
     * refresh.
     *
     * @param scrollPos
     */
    public void setScrollPos(java.awt.Point scrollPos) {
        this.scrollPos = scrollPos;
    }

    public Point getCurrentHoveredPoint() {
        return currentHoveredPoint;
    }

    public void setCurrentHoveredPoint(Point p) {
        if (this.currentHoveredPoint.x != p.x || this.currentHoveredPoint.y != p.y) {
            this.currentHoveredPoint = p;
        }
    }

    /**
     * Set current special port
     *
     * @param currentSpecialPort
     */
    public void setCurrentSpecialPort(Integer currentSpecialPort) {
        Integer old = getCurrentSpecialPort();
        this.currentSpecialPort = currentSpecialPort;
    }

    /**
     * Return current special port
     *
     * @return
     */
    public Integer getCurrentSpecialPort() {
        return currentSpecialPort;
    }

    /**
     * Change the current level slot.
     *
     * @param currentLevelSlot
     */
    public void setCurrentLevelSlot(Integer currentLevelSlot) {
        Integer old = getCurrentLevelSlot();
        this.currentLevelSlot = currentLevelSlot;
    }

    /**
     * Returns which level is currently opened in the editor.
     *
     * @return Integer containg the current levelslot (0-110)
     */
    public Integer getCurrentLevelSlot() {
        return currentLevelSlot;
    }

    public EditorModel() {
        supaplex = new Supaplex();
        properties = new Properties();
        dataChanged = false;
        currentLevelSlot = 0;
        currentSpecialPort = 1;
        scrollPos = new java.awt.Point(0, 0);
        currentHoveredPoint = new Point(0, 0);
        selectedTile = StandardTiles.BASE;
        drawMode = EditorController.drawMode.pencil;
        zoomLevel = 2;
        File props = new File("config.properties");
        if (props.exists()) {
            InputStream fstream;
            try {
                fstream = new FileInputStream(props);
                properties.load(fstream);
                fstream.close();
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
     * This value should be true when a user made a change that they might wish
     * to save.
     *
     * @return
     */
    public boolean getDataChanged() {
        return this.dataChanged;
    }

    /**
     * We set DataChanged to true when we make a change to a level. It's used to
     * check whether or not show a popup when the user tries to attempt a
     * destructive action with unsaved changes.
     *
     * @param value
     */
    public void setDataChanged(boolean value) {
        this.dataChanged = value;
    }

    /**
     * Overwrite existing level collection
     *
     * @param supaplex Supaplex object (for if you load a collection from a
     * file) or null to create an empty collection
     */
    public void setLevelCollection(Supaplex supaplex) {
        if (supaplex == null) {
            this.supaplex = new Supaplex();
        } else {
            this.supaplex = supaplex;
        }
    }

    /**
     * Get the whole level collection, essentially a hashmap with getters and
     * setters
     *
     * @return
     */
    public Supaplex getLevelCollection() {
        return this.supaplex;
    }

    ;

    /**
     * Get configuration property from config.properties
     * Currently the only value in config.properties is our preffered language
     * @param key
     * @return String or null when key not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Set configuration property in config.properties, directly written to FS
     *
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
    }

    /**
     * We use two different tilesets, this tileset is used by the LevelView.
     * When the user changes the zoom level the tiles get rescaled so we need 2
     * copies, for the levelView and the GUI
     *
     * @return
     */
    public HashMap<Tile, ImageIcon> getTileSetLevelView() {
        return tileSetLevelView;
    }

    /**
     * set the image collection set for supaplex tiles used for rendering
     *
     * @param tileSet
     */
    public void setTileSetLevelView(HashMap<Tile, ImageIcon> tileSet) {
        this.tileSetLevelView = tileSet;
    }

    /**
     * returns the tileset used by the GUI, always contains 16x16 images
     *
     * @return Hashmap with Tile and ImageIcons
     */
    public HashMap<Tile, ImageIcon> getTiles() {
        return tiles;
    }

    /**
     * Tileset used by the GUI, always contains 16x16 images.
     *
     * @param tiles Hashmap with Tile and ImageIcons
     */
    public void setTiles(HashMap<Tile, ImageIcon> tiles) {
        this.tiles = tiles;
    }
}
