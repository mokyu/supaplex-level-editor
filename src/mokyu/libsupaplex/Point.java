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
 * Used for accessing members in the Level HashMap
 * Point objects with the same x and y values can be compared with each other
 * @author Mokyu
 */
public class Point {

    public int x;
    public int y;

    public Point(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(this.getClass() != o.getClass() || o == null) {
            return false;
        }
        Point pt = (Point)o;
        return (this.x == pt.x && this.y == pt.y);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        return hash;
    }
}
