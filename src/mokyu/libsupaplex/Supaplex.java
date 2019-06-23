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
package mokyu.libsupaplex;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 *
 * @author Mokyu
 */
public class Supaplex extends StandardTiles {

    private final Map<Integer, Level> levels;

    /**
     * Generate a Supaplex object with empty levels.
     * To prevent leaking `this` you have to manually call Supaplex.init() after creation;
     */
    public Supaplex() {
        this.levels = new HashMap<>();
        for(int i = 0; i < 111; i++) {
            this.levels.put(i, new Level());
        }
    }

    /**
     * Load a level pack from a file.
     *
     * @param uri Path to a valid LEVELS.DAT file. These are *not* individual
     * levels (1536 byte .SP files)
     * @throws IOException General I/O file errors
     * @throws Exception When file size is not exactly 170496 bytes in size
     */
    public void loadLevelPackFromFile(String uri) throws IOException, Exception {
        Path path = Paths.get(uri);
        byte[] file;
        file = Files.readAllBytes(path);
        if (file.length != 170496) {
            throw new Exception("Unexpected file size. Expected 170496 bytes.");
        }
        for (int i = 0; i < 111; i++) {
            byte[] lvl = Arrays.copyOfRange(file, i * 1536, i * 1536 + 1536);
            Level l = new Level(lvl);
            this.levels.put(i, l);

        }

    }

    /**
     * Save level pack to a file, please note that this does not create a
     * LEVELS.LST file
     *
     * @param uri Path to where to save the level pack
     * @throws IOException General file I/O failure
     */
    public void saveLevelPackToFile(String uri) throws IOException {
        Path path = Paths.get(uri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (int i = 0; i < 111; i++) {
            stream.write(this.getLevel(i).toByteArray());
        }
        byte[] levelPack = stream.toByteArray();
        Files.write(path, levelPack);
    }

    /**
     * Use this to get a specific level
     *
     * @param index Selects the level (111 total, 0-110)
     * @return returns a Level object or null when an invalid integer is
     * supplied or no level pack is loaded
     */
    public Level getLevel(int index) {
        return this.levels.get(index);
    }

    /**
     * Use this to update a specific level in the level set. This also
     * automatically adds the level to this propertyChangeListener
     *
     * @param index The level you wish to override (111 total, 0-110)
     * @param level Level object
     */
    public void setLevel(int index, Level level) {
        Level old = getLevel(index);
        this.levels.put(index, level);
    }
    /**
     * Call this when the Supaplex() instructor is used to properly assign listeners
     */
}
