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
import java.util.HashMap;
import java.awt.Image;
import mokyu.libsupaplex.*;

/**
 * Custom canvas object for rendering Supaplex levels or parts of Supaplex
 * levels
 *
 * @author Mokyu
 */
public class JLevelView extends JPanel implements MouseListener {

    public static final Integer TILE_SIZE = 16;                                 // tiles are 16x16
    public static final Integer LEVEL_HEIGHT = 24;                              // level field is 24 high
    public static final Integer LEVEL_HEIGHT_PX = 24 * JLevelView.TILE_SIZE;    // pixel height at 1x zoom
    public static final Integer LEVEL_WIDTH = 60;                               // level field is 60 wide
    public static final Integer LEVEL_WIDTH_PX = 60 * JLevelView.TILE_SIZE;     // pixel weidth at 1x zoom

    private Integer zoomLevel;
    private Integer x;
    private Integer y;
    private Integer height;
    private Integer width;
    private Level level;
    private HashMap<Tile, ImageIcon> tileSet;

    /**
     * Render parts of a Supaplex level. Useful if you do not have to show the
     * whole level.
     *
     * @param x start X coordinate of the viewport
     * @param y start Y coordinate of the viewport
     * @param height height of the viewport
     * @param width width of the viewport
     * @param level Level object to generate from
     * @param tileSet Matching list of icons to associate Supaplex tiles with.
     */
    public JLevelView(Integer x, Integer y, Integer height, Integer width, Level level, HashMap<Tile, ImageIcon> tileSet) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.zoomLevel = 2;
        this.level = level;
        this.tileSet = tileSet;
        this.setPreferredSize(new Dimension((JLevelView.LEVEL_WIDTH_PX * this.zoomLevel) + (TILE_SIZE * this.zoomLevel), (JLevelView.LEVEL_HEIGHT_PX * this.zoomLevel) + (TILE_SIZE * this.zoomLevel)));
    }

    /**
     * Renders a complete Supaplex level
     *
     * @param level Level object to generate from
     * @param tileSet Matching list of icons to associate Supaplex tiles with.
     */
    public JLevelView(Level level, HashMap<Tile, ImageIcon> tileSet) {
        this(0, 0, JLevelView.LEVEL_HEIGHT, JLevelView.LEVEL_WIDTH, level, tileSet);
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

    @Override
    public void mouseExited(MouseEvent e) {
        // we can ignore this
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // we can ignore this
        // figure out a way to track mouse events
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // use to calculate drag between 2 points
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // use to calculate drag between 2 points
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // use for fill, right click clear
    }
}
