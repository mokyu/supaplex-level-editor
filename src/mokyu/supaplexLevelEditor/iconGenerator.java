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
import java.util.*;
import java.io.*;
import javax.swing.ImageIcon;
import java.awt.image.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Mokyu
 */
public class iconGenerator extends StandardTiles {

    private static final int MAX_TILES = 40; // There are 40 unique tile image stored in tiles.png (656x16 pixels)
    private static final int TILE_SIZE = 16; // Each of these tiles are 16 by 16 pixels in size and stored side by side.

    /**
     * Splits up an image into 16x16 tiles with their associated Tile objects
     * @param path, path to the image containing all 40 tiles used by the editor, image is expected to be 656x16 pixels in size.
     * @return  HashMap with a Tile as a key and BufferedImage as value. You can compare these Tile objects with each other to easily get the BufferedImage associated with your Tile()
     */
    public static HashMap<Tile, ImageIcon> getTilesetFromFile(String path) {

        HashMap<Tile, ImageIcon> tiles = new HashMap<>();
        BufferedImage tileset = null;
        try {
            tileset = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return tiles;
        }
        for (int i = 0; i < (MAX_TILES + 1); i++) {
            tiles.put(new Tile((byte) i), new ImageIcon(tileset.getSubimage(TILE_SIZE * i, 0, TILE_SIZE, TILE_SIZE))); // x,y,width,height
        }
        return tiles;
    }
}
