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

/**
 * Tile object, used to set, get and compare game tiles on a Supaplex level. In
 * total there are 28 tiles, directly comparable with other Tiles
 */
public class Tile {

    private byte rawTile;
    private String niceName;

    /**
     * Creates a Tile object from a byte
     *
     * @param rawTile value between 0x0 and 0x28 expected
     */
    public Tile(byte rawTile) {
        String[] names = {
            "Void", "Zonk", "Base", "Murphy", "Infotron",
            "RAM chip (Standard)", "Hardware (Standard)",
            "Exit", "Utility Disk (Orange)", "Port (right)",
            "Port (Down)", "Port (Left)", "Port (Up)",
            "Sp. Port (Right)", "Sp. Port (Down)", "Sp. Port (Left)",
            "Sp. Port (Up)", "Snik Snak", "Utility Disk (Yellow)",
            "Terminal", "Utility Disk (Red)", "Port (Vertical)",
            "Port (Horizontal)", "Port (Complete)", "Electron",
            "Bug", "RAM Chip (Left)", "RAM chip (Right)", "Hardware 0",
            "Hardware 1", "Hardware 2", "Hardware 3", "Hardware 4",
            "Hardware 5", "Hardware 6", "Hardware 7", "Hardware 8",
            "Hardware 9", "RAM Chip (Up)", "RAM Chip (Down)", "Invisible Wall (Void)"
        };
        this.setTile(rawTile);
        this.niceName = names[(int) rawTile];
    }

    /**
     * Creates a Tile object from a byte
     *
     * @param tile, Byte
     * @throws RuntimeException, When parameter tile is not within 0x0 and 0x28
     */
    public void setTile(byte tile) throws RuntimeException {
        if (tile > 0x28) {
            throw new RuntimeException("Attempted to create invalid tile 0x" + String.format("%02x", rawTile));
        }
        this.rawTile = tile;
    }

    /**
     *
     * @return byte representation of the tile
     */
    public byte getTile() {
        return this.rawTile;
    }

    /**
     * Returns the nicename of the tile
     *
     * @return
     */
    public String getNiceName() {
        return this.niceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass() || o == null) {
            return false;
        }
        Tile tile = (Tile) o;
        return (this.rawTile == tile.rawTile);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.rawTile;
        return hash;
    }
}
