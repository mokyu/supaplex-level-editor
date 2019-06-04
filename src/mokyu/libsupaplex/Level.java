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

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.beans.*;
/**
 *
 * @author Mokyu
 */
public class Level {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Map<Point, Tile> level;
    private byte[] padding0;
    private byte gravity;
    private byte speedFixVersion;
    private byte[] rawName;
    private byte freezeZonks;
    private byte requiredInfontrons;
    private byte gravitySwitchPorts;
    private byte[] specialPortDataRaw;
    private Map<Integer, GravitySwitchPort> specialPortData;
    private byte[] speedfixDemoInfo;

    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    /**
     * Generates a completely fresh empty level.
     */
    public Level() {
        this.level = new HashMap<>();
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 24; j++) {
                Point point = new Point(i, j);
                Tile tile = new Tile(Supaplex.VOID.getTile());
                this.level.put(point, tile);
            }
        }
        this.padding0 = new byte[4];
        Arrays.fill(this.padding0, (byte) 0x0);
        this.setGravity(false);
        this.setSpeedFixVersion((byte) 0x20);
        this.setName("-----------------------");
        this.setFreezeZonks(false);
        this.setRequiredInfotrons(0);
        this.setGravitySwitchPorts(0);
        this.specialPortData = new HashMap<>();
        this.speedfixDemoInfo = new byte[4];
        Arrays.fill(this.speedfixDemoInfo, (byte) 0x0);
        
    }

    /**
     * Load supaplex level from a File, use this function to load .SP files
     * (individual supaplex levels)
     *
     * @param uri Path to load level from. Use this if you want to modify .SP
     * files. Must be 1536 bytes
     * @throws IOException General I/O errors
     */
    public Level(String uri) throws IOException {
        this();
        Path path = Paths.get(uri);
        byte[] file;
        file = Files.readAllBytes(path);
        if (file.length != 1536) {
            throw new IOException("Unexpected file size. Expected 170496 bytes.");
        }
        this.fromByteArray(file);

    }

    /**
     * Create a Level object from a byte array, must be exactly 1536 bytes in
     * size
     *
     * @param levelData a whole raw 1536 byte supaplex level.
     */
    public Level(byte[] levelData) throws RuntimeException {
        this();
        this.fromByteArray(levelData);
    }

    /**
     * This function returns the tile at the specified coordinates of the
     * supaplex game world
     *
     * @param point, a Point object containing the x and y coordinates of the
     * tile you wish to retrieve.
     * @returns Tile object Containing a byte value representing the tile at
     * those coordinates. Expected values are 0-59 for X and 0-24 for Y
     */
    final public Tile getTile(Point point) throws RuntimeException {
        if (point.x >= 60 || point.x < 0 || point.y >= 24 || point.y < 0) {
            throw new RuntimeException("Invalid coordinates given. Expected x value between 0 and 60 and y value between 0 and 24.");
        }
        return this.level.get(point);
    }

    /**
     * This function returns the tile at the specified coordinates of the
     * supaplex game world
     *
     * @param point, a Point object containing the x and y coordinates of the
     * tile you wish to update.
     * @param tile, A tile object you wish to use to replace the tile stored at
     * the coordinates stored in point
     */
    final public void setTile(Point point, Tile tile) {
        Tile old = this.getTile(point);
        this.level.put(point, tile);
        pcs.firePropertyChange("Level.level", 0, 1);
    }

    /**
     * Enable or disable gravity, Murphy will fall down when enabled.
     *
     * @param value, boolean value, speaks for itself.
     */
    final public void setGravity(boolean value) {
        byte old = this.gravity;
        if (value == true) {
            this.gravity = 0x1;
        } else {
            this.gravity = 0x0;
        }
        pcs.firePropertyChange("Level.gravity", old, value);
    }

    /**
     * get the value of gravity
     *
     * @return boolean containing the current state of gravity.
     */
    final public boolean getGravity() {
        return this.gravity == 0x1;
    }

    /**
     * Unused in the original 1991 version of the game. Used by Speedfix for
     * it's version information.
     *
     * @return byte containing the speedfix version. This is 0x20 by default.
     */
    final public byte getSpeedFixVersion() {
        return this.speedFixVersion;
    }

    /**
     * Unused in the original 1991 version of the game. Used by Speedfix for
     * it's own version information.
     *
     * @param version Byte value. 0x20 by default.
     */
    final public void setSpeedFixVersion(byte version) {
        byte old = getSpeedFixVersion();
        this.speedFixVersion = version;
        pcs.firePropertyChange("Level.speedFixVersion", old, version);
    }

    /**
     * Returns the name of the current level, exactly 23 ANSI characters, all
     * uppercase
     *
     * @return String containing the name of the level. This includes trailing
     * and leading space or dashes.
     */
    final public String getName() { // problem: this gets called before it's initialized due to 
        return new String(this.rawName);
    }

    /**
     * Returns the name of the current level, exactly 23 ANSI characters.
     *
     * @param newName String containing the new name for the level. Must be
     * exactly 23 characters. This value will be converted to US_ASCII and
     * converted to uppercase before being applied.
     */
    final public void setName(String newName) throws RuntimeException {
        int length = newName.getBytes(StandardCharsets.US_ASCII).length;
        if (length != 23) {
            throw new RuntimeException("Expected level name to be 23 bytes in length, " + length + " given.");
        } else {
            this.rawName = newName.toUpperCase().getBytes(StandardCharsets.US_ASCII); // Supaplex only renders uppercase characters accurately
        }
        pcs.firePropertyChange("Level.rawName", 0, 1);

    }

    /**
     * Enable or disable freezing zonks in place, they will not fall down when
     * this is set to true.
     *
     * @param value, boolean value, speaks for itself.
     */
    final public void setFreezeZonks(boolean value) {
        boolean old = getFreezeZonks();
        if (value == true) {
            this.freezeZonks = 0x2;
        } else {
            this.freezeZonks = 0x0;
        }
        pcs.firePropertyChange("Level.freezeZonks", 0, 1);
    }

    /**
     * get the value of the freeze-zonks flag, determines whether or not zonks
     * fall down.
     *
     * @return boolean value.
     */
    final public boolean getFreezeZonks() {
        return this.freezeZonks == 0x2;
    }

    /**
     * How many infotrons you need before you can exit the level
     *
     * @param infotrons, value between 0 and 255. 0 being auto detect (every
     * infotron on the level)
     */
    final public void setRequiredInfotrons(int infotrons) throws RuntimeException {
        int old = getRequiredInfotrons();
        if (infotrons > 255 || infotrons < 0) {
            throw new RuntimeException("Invalid number of infotrons given. Expected value between 0 and 255");
        }
        this.requiredInfontrons = (byte) infotrons;
        pcs.firePropertyChange("Level.requiredInfotrons", 0, 1);
    }

    /**
     * How many infotrons you need before you can exit the level
     *
     * @return int, amount of infotrons you need to complete the level, with 0
     * being every single infotron on the level
     */
    final public int getRequiredInfotrons() {
        return this.requiredInfontrons & 0xFF;
    }

    /**
     * Set how many gravity switch ports exist on the level.
     *
     * @param ports ,Set the amount of gravity switch ports on the level, a
     * number between 10 and 0
     */
    final public void setGravitySwitchPorts(int ports) throws RuntimeException {
        int old = getGravitySwitchPorts();
        if (ports > 10 || ports < 0) {
            throw new RuntimeException("Invalid number of ports given. Expected value between 0 and 255");
        }
        this.gravitySwitchPorts = (byte) ports;
        pcs.firePropertyChange("Level.gravitySwitchPorts", 0, 1);
    }

    /**
     * Set how many gravity switch ports exist on the level.
     *
     * @return int , The amount of existing gravity switch ports
     */
    final public int getGravitySwitchPorts() {
        return this.gravitySwitchPorts & 0xFF;
    }

    /**
     * Sets gravity switch port data
     * @param port the port number which to update
     * @param portData the port info
     */
    final public void setGravitySwitchPortData(int port, GravitySwitchPort portData) {
        GravitySwitchPort old = getGravitySwitchPortData(port);
        this.specialPortData.put(port, portData);
        pcs.firePropertyChange("Level.specialPortData", 0, 1);
    }

    /**
     * Get Gravity switch port data
     * @param port
     * @return GravitySwitchPort object containing port data
     */
    final public GravitySwitchPort getGravitySwitchPortData(int port) {
        return this.specialPortData.get(port);
    }

    /**
     * Metadata used by speedfix versions of the game
     * @return byte array (4 bytes)
     */
    final public byte[] getSpeedFixDemoInfo() {
        return this.speedfixDemoInfo;
    }

    /**
     * This sets metadata used by SpeedFix (4 bytes)
     * @param byte array (4 bytes)
     * @throws RuntimeException
     */
    final public void setSpeedFixDemoInfo(byte[] data) throws RuntimeException {
        byte[] old = getSpeedFixDemoInfo();
        if (data.length != 4) {
            throw new RuntimeException("Expected 4 bytes but invalid amount given");
        }
        this.speedfixDemoInfo = data;
        pcs.firePropertyChange("Level.speedfixDemoInfo", 0, 1);
    }

    /**
     * Turn level object into a byte representation readable by the original
     * 1991 Supaplex game
     *
     * @return byte array Containing a complete Supaplex readable level, 1536
     * bytes in size, could be used to create a LevelPack or to write to an .SP
     * file for certain supaplex clones
     */
    final public byte[] toByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            for (int i = 0; i < 60; i++) {
                for (int j = 0; j < 24; j++) {
                    Point pt = new Point(i, j);
                    stream.write(this.level.get(pt).getTile());
                }
            }
            stream.write(this.padding0);
            stream.write(this.gravity);
            stream.write(this.speedFixVersion);
            stream.write(this.rawName);
            stream.write(this.freezeZonks);
            stream.write(this.requiredInfontrons);
            stream.write(this.gravitySwitchPorts);
            stream.write(this.specialPortDataRaw);
            stream.write(this.speedfixDemoInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream.toByteArray();
    }
    /**
     * Load level from byte array
     * @param level 1536 bytes
     */
    final public void fromByteArray(byte[] level) {
        if (level.length != 1536) {
            throw new RuntimeException("Invalid level size " + level.length + " bytes (expected 1536)");
        }
        this.level = new HashMap<>();
        int offset = 0;
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 24; j++) {
                Point pt = new Point(i, j);
                Tile t = new Tile(level[offset]);
                this.level.put(pt, t);
                ++offset;
            }
        }
        this.padding0 = Arrays.copyOfRange(level, 1440, 1444);
        this.gravity = level[1444];
        this.speedFixVersion = level[1445];
        this.rawName = Arrays.copyOfRange(level, 1446, 1469);
        this.freezeZonks = level[1469];
        this.requiredInfontrons = level[1470];
        this.gravitySwitchPorts = level[1471];
        this.specialPortData = new HashMap<>();
        this.specialPortDataRaw = Arrays.copyOfRange(level, 1472, 1532);
        for (int i = 1472; i < 1532; i += 6) {
            //ByteBuffer bb = ByteBuffer.wrap(level, i, 2);
            // Currently not sure on how to do this. Ignore special ports for nwo.
            //int v = (Short.toUnsignedInt(bb.getShort()));
            //System.out.format("i=%d -> 0x%02x 0x%02x -> %d -> x%d y%d\n",i, level[i], level[i+1], v, (v & 0xFFFF) / 256, (v & 0xFFFF) % 256);

            //SpecialPort sp = new SpecialPort(0, 0, level[i + 2] == 0x1, level[i + 3] == 0x2, level[i + 4] == 0x1, level[i + 5]);
            //this.specialPortData.put(count, sp);
        }
        this.speedfixDemoInfo = Arrays.copyOfRange(level, 1532, 1536);
    }

}
