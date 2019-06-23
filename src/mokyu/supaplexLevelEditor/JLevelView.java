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

import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.util.*;
import java.awt.Image;
import mokyu.libsupaplex.*;

/**
 * Custom canvas object for rendering Supaplex levels or parts of Supaplex
 * levels
 *
 * @author Mokyu
 */
public class JLevelView extends JPanel implements MouseListener, MouseMotionListener {

    public static final Integer TILE_SIZE = 16;                                 // tiles are 16x16
    public static final Integer LEVEL_HEIGHT = 24;                              // level field is 24 high
    public static final Integer LEVEL_HEIGHT_PX = 24 * JLevelView.TILE_SIZE;    // pixel height at 1x zoom
    public static final Integer LEVEL_WIDTH = 60;                               // level field is 60 wide
    public static final Integer LEVEL_WIDTH_PX = 60 * JLevelView.TILE_SIZE;     // pixel weidth at 1x zoom
    private static final Integer TILE_NORTH = 0;
    private static final Integer TILE_EAST = 1;
    private static final Integer TILE_SOUTH = 2;
    private static final Integer TILE_WEST = 3;
    private List<JLevelViewListener> listeners = new ArrayList<>();

    public void addListener(JLevelViewListener listener) {
        listeners.add(listener);
    }

    private Integer zoomLevel;
    private Integer _x;
    private Integer _y;
    private Integer _height;
    private Integer _width;
    private Level level;
    private HashMap<Tile, ImageIcon> tileSet;
    private LinkedHashSet<Point> selection;
    private Tile fillTarget;
    private Tile source;
    private LinkedHashSet<Point> toFill;

    /**
     * Render parts of a Supaplex level. Useful if you do not have to show the
     * whole level.
     *
     * @param x start X coordinate of the viewport
     * @param y start Y coordinate of the viewport
     * @param height height of the viewport
     * @param width width of the viewport
     * @param level Level object to generate from
     * @param zoom
     * @param tileSet Matching list of icons to associate Supaplex tiles with.
     * @param tile Used for the floodfill algorithm to determine what tile to
     * fill other tiles in. fill.
     */
    public JLevelView(Integer x, Integer y, Integer height, Integer width, Level level, Integer zoom, HashMap<Tile, ImageIcon> tileSet, Tile tile) {
        super();
        this._x = x;
        this._y = y;
        this._height = height;
        this._width = width;
        this.zoomLevel = zoom;
        this.level = level;
        this.tileSet = tileSet;
        this.selection = new LinkedHashSet<>();
        this.fillTarget = tile;
        this.toFill = new LinkedHashSet<>();
        this.setPreferredSize(new Dimension(calc(62), calc(26))); // add space for 2 extra tiles to make it look more uniform
    }

    /**
     * Renders a complete Supaplex level
     *
     * @param level Level object to generate from
     * @param tileSet Matching list of icons to associate Supaplex tiles with.
     * @param zoom zoom level of the levelview 1-4
     * @param tile the target tile (used for calculating flood fill)
     */
    public JLevelView(Level level, HashMap<Tile, ImageIcon> tileSet, Integer zoom, Tile tile) {
        this(0, 0, JLevelView.LEVEL_HEIGHT, JLevelView.LEVEL_WIDTH, level, zoom, tileSet, tile);
        init();
    }

    private void init() {
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Integer zoom = getZoomLevel();
        // lets first rescale our icons to the scale of our levelView if applicable
        // we do this by getting an image and checking if it's the expected size
        if (this.tileSet.get(new Tile((byte) 0x01)).getIconWidth() != (this.getZoomLevel() * JLevelView.TILE_SIZE)) {
            this.tileSet.entrySet().forEach((set) -> {
                ImageIcon image = set.getValue();
                image = new ImageIcon(image.getImage().getScaledInstance(JLevelView.TILE_SIZE * zoomLevel, JLevelView.TILE_SIZE * zoomLevel, java.awt.Image.SCALE_FAST));
                tileSet.put(set.getKey(), image);
            });
        }
        // draw level
        for (int xCoord = 0; xCoord < 60; xCoord++) {
            for (int yCoord = 0; yCoord < 24; yCoord++) {
                Tile tile = level.getTile(new Point(xCoord, yCoord));
                Image icon = this.tileSet.get(tile).getImage();
                g.drawImage(icon, calc(xCoord + 1), calc(yCoord + 1), null);
            }
        }

        // draw labels and lines on x and y axis
        for (int i = 1; i <= 60; i++) {
            g.drawString(Integer.toString(i - 1), (calc(i) + (calc(1) / 2)), (calc(1) / 2));
            if (i != 60) {
                // if not last line, draw vertical lines
            }
        }
        for (int i = 1; i <= 24; i++) {
            g.drawString(Integer.toString(i - 1), (calc(1) / 2), (calc(i) + (calc(1) / 2)));
        }
        // draw semi transparent lines (2px thick)

    }

    public Integer getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(Integer zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    /**
     * abstract scaling math with this function
     *
     * @param coord
     * @return (coord * TILE_SIZE) * zoom
     */
    private Integer calc(Integer coord) {
        return (coord * TILE_SIZE) * getZoomLevel();
    }

    /**
     * Generate a line using the DDA algorithm
     *
     * @param s
     * @return
     */
    private LinkedHashSet<Point> line(LinkedHashSet<Point> s) {
        LinkedHashSet<Point> result = new LinkedHashSet<>();
        if (!s.iterator().hasNext()) {
            return result;
        }
        Point first = s.iterator().next();
        Point last = (Point) s.toArray()[s.size() - 1];
        if (last.x < first.x) {
            Point tmp = new Point(first.x, first.y);
            first = new Point(last.x, last.y);
            last = tmp;
        }
        double x, y, x1 = first.x, y1 = first.y, x2 = last.x, y2 = last.y, dx, dy, step;
        int i = 1;
        dx = last.x - first.x;
        dy = last.y - first.y;
        if (Math.abs(dx) >= Math.abs(dy)) {
            step = Math.abs(dx);
        } else {
            step = Math.abs(dy);
        }
        dx = dx / step;
        dy = dy / step;
        x = x1;
        y = y1;
        while (i <= step) {
            result.add(new Point((int) x, (int) y));
            x = x + dx;
            y = y + dy;
            ++i;
        }
        result.add(last);
        return result;
    }

    private LinkedHashSet<Point> rect(LinkedHashSet<Point> s) {
        LinkedHashSet<Point> result = new LinkedHashSet<>();
        if (!s.iterator().hasNext()) {
            return result;
        }
        Point first = s.iterator().next();
        Point last = (Point) s.toArray()[s.size() - 1];
        int x1 = first.x, x2 = last.x, y1 = first.y, y2 = last.y;
        if (first.x > last.x) {
            x1 = last.x;
            x2 = first.x;
        }
        if (first.y > last.y) {
            y1 = last.y;
            y2 = first.y;
        }
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
                result.add(new Point(x,y));
            }
        }
        return result;
    }

    /**
     * get the neighbour tile in 4 directions. returns null when tile is out of
     * bounds
     *
     * @param pt
     * @param direction
     * @return
     */
    private Point getNeighbour(Point pt, Integer direction) {
        if (direction.equals(JLevelView.TILE_NORTH)) {
            if (pt.y == 0) { // can't go further north than 0
                return null;
            }
            return new Point(pt.x, pt.y - 1);
        }
        if (direction.equals(JLevelView.TILE_EAST)) {
            if (pt.x == 59) { // can't go further east than 59
                return null;
            }
            return new Point(pt.x + 1, pt.y);
        }
        if (direction.equals(JLevelView.TILE_SOUTH)) {
            if (pt.y == 23) { // can't go further south than 23
                return null;
            }
            return new Point(pt.x, pt.y + 1);
        }
        if (direction.equals(JLevelView.TILE_WEST)) {
            if (pt.x == 0) { // can't go further west than 0
                return null;
            }
            return new Point(pt.x - 1, pt.y);
        }
        return null;
    }

    private LinkedHashSet<Point> fill_start(Point node, int button) {
        this.source = level.getTile(node);
        this.toFill.add(node);
        if (button == MouseEvent.BUTTON3) {
            this.fillTarget = StandardTiles.VOID;
        }
        fill(this.getNeighbour(node, JLevelView.TILE_NORTH));
        fill(this.getNeighbour(node, JLevelView.TILE_EAST));
        fill(this.getNeighbour(node, JLevelView.TILE_SOUTH));
        fill(this.getNeighbour(node, JLevelView.TILE_WEST));
        return this.toFill;
    }

    private void fill(Point node) {
        if (node == null) {
            return;
        }
        Tile tile = level.getTile(node);
        // only fill when the tile is different from our filltarget, when the tile is the same as the tile type we wish to fill and when it's not part of the selection already
        if (!tile.equals(this.fillTarget) && tile.equals(this.source) && !this.toFill.contains(node)) {
            this.toFill.add(node);
            fill(this.getNeighbour(node, JLevelView.TILE_NORTH));
            fill(this.getNeighbour(node, JLevelView.TILE_EAST));
            fill(this.getNeighbour(node, JLevelView.TILE_SOUTH));
            fill(this.getNeighbour(node, JLevelView.TILE_WEST));
        }
    }

    private Point parseMouseCoords(MouseEvent e) {
        if (e.getX() < (TILE_SIZE * getZoomLevel()) || e.getX() >= (TILE_SIZE * getZoomLevel() * 61)) {
            return null;
        }
        if (e.getY() < (TILE_SIZE * getZoomLevel()) || e.getY() >= (TILE_SIZE * getZoomLevel() * 25)) {
            return null;
        }
        Integer xCoord = (e.getX() / (TILE_SIZE * getZoomLevel())) - 1;
        Integer yCoord = (e.getY() / (TILE_SIZE * getZoomLevel())) - 1;
        return new Point(xCoord, yCoord);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // we can ignore this
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // empty the hashmap when we are not dragging
        selection = new LinkedHashSet<>();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point pt = parseMouseCoords(e);
        if (pt != null) {
            selection.add(pt);
        }
        // now we can process it.
        for (JLevelViewListener l : listeners) {
            l.pencilSelection(selection, e.getButton());
            l.lineSelection(line(selection), e.getButton());
            l.squareSelection(rect(selection), e.getButton());
            l.fillSelection(fill_start(parseMouseCoords(e), e.getButton()), e.getButton());
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // use to calculate drag between 2 points
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = parseMouseCoords(e);
        if (point != null) {
            for (JLevelViewListener l : listeners) {
                LinkedHashSet<Point> ps = new LinkedHashSet<>();
                ps.add(point);
                l.pencilSelection(ps, e.getButton());
                l.fillSelection(fill_start(parseMouseCoords(e), e.getButton()), e.getButton());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = parseMouseCoords(e);
        if (point != null) {
            for (JLevelViewListener l : listeners) {
                l.tileHovered(point);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point pt = parseMouseCoords(e);
        if (pt == null) {
            return;
        }
        selection.add(pt);
    }
}
