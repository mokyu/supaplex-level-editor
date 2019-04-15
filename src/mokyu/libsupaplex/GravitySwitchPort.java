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
 *
 * @author Mokyu
 */
public class GravitySwitchPort {

    private int x;
    private int y;
    public boolean gravity;
    public boolean freezeZonks;
    public boolean freezeEnemy;
    public byte padding;
    public GravitySwitchPort(int x, int y, boolean gravity, boolean freezeZonks, boolean freezeEnemy, byte padding) throws RuntimeException {
        if (x >= 60 || x < 0 || y >= 24 || y < 0) {
            throw new RuntimeException("Invalid coordinates given. Expected x value between 0 and 60 and y value between 0 and 24.");
        }
        this.x = x;
        this.y = y;
        this.gravity = gravity;
        this.freezeZonks = freezeZonks;
        this.freezeEnemy = freezeEnemy;
        this.padding = padding;
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
}
