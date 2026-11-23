package wildwestoutlaw.tile;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;

public class TileManager {

    GamePanel gp;
    public final CollisionMap collisionMap;
    private final boolean[][] blockedTiles;
    BufferedImage backgroundImage;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        BufferedImage buildings = Assets.image("/environment/MAIN.png");
        BufferedImage originalImage = Assets.image("/environment/Background.png");

        // The finished map is fully opaque, so no alpha channel (much faster to draw)
        backgroundImage = new BufferedImage(gp.worldWidth, gp.worldHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, gp.worldWidth, gp.worldHeight, null);
        g2d.drawImage(buildings, 0, 0, gp.worldWidth, gp.worldHeight, null);
        g2d.dispose();

        collisionMap = new CollisionMap(buildings, gp.scale);

        blockedTiles = new boolean[gp.maxWorldCol][gp.maxWorldRow];
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                blockedTiles[col][row] = collisionMap.isBlocked(new Rectangle(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize));
            }
        }
    }

    // True if anything in the tile is solid (the path finder works in whole tiles)
    public boolean isTileBlocked(int col, int row) {
        return blockedTiles[col][row];
    }

    public void draw(Graphics2D g2) {
        // Calculate the position of the background image
        int bgX = gp.player.screenX - gp.player.worldX;
        int bgY = gp.player.screenY - gp.player.worldY;

        // The visible area of the screen
        Rectangle visibleRect = new Rectangle(0, 0, gp.screenWidth, gp.screenHeight);

        // Calculate the intersection of the visible area and the background image
        Rectangle bgRect = new Rectangle(bgX, bgY, backgroundImage.getWidth(), backgroundImage.getHeight());
        Rectangle intersection = visibleRect.intersection(bgRect);

        // Draw the visible portion of the background image
        if (!intersection.isEmpty()) {
            BufferedImage visibleImage = backgroundImage.getSubimage(intersection.x - bgX, intersection.y - bgY, intersection.width, intersection.height);
            g2.drawImage(visibleImage, intersection.x, intersection.y, null);
        }
    }
}
