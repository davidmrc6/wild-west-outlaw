package wildwestoutlaw.tile;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// Where you can't walk, worked out from the buildings layer (MAIN.png) when the game starts:
// the map is split into small squares, and a square is solid if any of its pixels is opaque
public class CollisionMap {

    private static final int CELL = 8; // pixels of the original artwork per square

    private final boolean[][] solid;
    private final int cellSize; // in game pixels
    private final int columns;
    private final int rows;

    // scale: how much bigger the map is drawn than the artwork
    public CollisionMap(BufferedImage buildings, int scale) {

        columns = buildings.getWidth() / CELL;
        rows = buildings.getHeight() / CELL;
        cellSize = CELL * scale;
        solid = new boolean[columns][rows];

        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                solid[col][row] = hasOpaquePixel(buildings, col * CELL, row * CELL);
            }
        }
    }

    private static boolean hasOpaquePixel(BufferedImage image, int x0, int y0) {
        for (int y = y0; y < y0 + CELL; y++) {
            for (int x = x0; x < x0 + CELL; x++) {
                if ((image.getRGB(x, y) >>> 24) >= 128) {
                    return true;
                }
            }
        }
        return false;
    }

    // True if any part of the rectangle (in game pixels) is solid or off the map
    public boolean isBlocked(Rectangle area) {
        int firstCol = Math.floorDiv(area.x, cellSize);
        int lastCol = Math.floorDiv(area.x + area.width - 1, cellSize);
        int firstRow = Math.floorDiv(area.y, cellSize);
        int lastRow = Math.floorDiv(area.y + area.height - 1, cellSize);

        for (int col = firstCol; col <= lastCol; col++) {
            for (int row = firstRow; row <= lastRow; row++) {
                if (isSolid(col, row)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSolid(int col, int row) {
        return col < 0 || row < 0 || col >= columns || row >= rows || solid[col][row];
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
