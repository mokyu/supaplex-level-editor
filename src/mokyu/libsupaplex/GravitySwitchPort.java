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

import java.nio.ByteBuffer;

/**
 *
 * @author Mokyu
 */
public class GravitySwitchPort {

    private int x;
    private int y;
    private boolean gravity;
    private boolean freezeZonks;
    private boolean freezeEnemy;
    private byte padding;

    /**
     * Generate gravity switch port based on x,y coordinates
     *
     * @param x
     * @param y
     * @param gravity
     * @param freezeZonks
     * @param freezeEnemy
     * @param padding
     * @throws RuntimeException
     */
    public GravitySwitchPort(int x, int y, boolean gravity, boolean freezeZonks, boolean freezeEnemy, byte padding) throws RuntimeException {
        this.x = x;
        this.y = y;
        this.gravity = gravity;
        this.freezeZonks = freezeZonks;
        this.freezeEnemy = freezeEnemy;
        this.padding = padding;
    }

    public boolean getGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public boolean getFreezeZonks() {
        return freezeZonks;
    }

    public void setFreezeZonks(boolean freezeZonks) {
        this.freezeZonks = freezeZonks;
    }

    public boolean getFreezeEnemy() {
        return freezeEnemy;
    }

    public void setFreezeEnemy(boolean freezeEnemy) {
        this.freezeEnemy = freezeEnemy;
    }

    public byte getPadding() {
        return padding;
    }

    public void setPadding(byte padding) {
        this.padding = padding;
    }

    /**
     * Generate gravity switch port based on raw coordinate value directly from
     * *.SP/*.DAT files
     *
     * @param location 16 bit Big Endian unsigned integer
     * @param gravity
     * @param freezeZonks
     * @param freezeEnemy
     * @param padding
     */
    public GravitySwitchPort(Integer location, boolean gravity, boolean freezeZonks, boolean freezeEnemy, byte padding) {
        this(location / 2 % 60, location / 60 / 2, gravity, freezeZonks, freezeEnemy, padding);
    }

    public void setX(int newX) throws RuntimeException {
        if (newX > 60 || newX < 0) {
            throw new RuntimeException("x coord out of bounds");
        }
        this.x = newX;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int newY) throws RuntimeException {
        if (newY > 60 || newY < 0) {
            throw new RuntimeException("y coord out of bounds");
        }
        this.y = newY;
    }

    public int getY() {
        return this.y;
    }

    public byte[] toByteArray() {
        ByteBuffer b = ByteBuffer.allocate(6);
        Integer coord = 2 * (this.x + (this.y * 60));
        ByteBuffer crd = ByteBuffer.allocate(4);
        crd.putInt(coord);
        b.put(crd.array()[2]);
        b.put(crd.array()[3]);
        if (this.gravity) {
            b.put((byte) 0x1);
        } else {
            b.put((byte) 0x0);
        }
        if (this.freezeZonks) {
            b.put((byte) 0x2);
        } else {
            b.put((byte) 0x0);
        }
        if (this.freezeEnemy) {
            b.put((byte) 0x1);
        } else {
            b.put((byte) 0x0);
        }
        b.put((byte) 0xff);
        return b.array();
    }
}
