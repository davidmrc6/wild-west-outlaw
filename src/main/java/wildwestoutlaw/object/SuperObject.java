package wildwestoutlaw.object;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Graphics2D;

import wildwestoutlaw.GamePanel;
import wildwestoutlaw.UtilityTool;

public class SuperObject {
    
    public BufferedImage[] images; // Array of images for the frames
    public int currentFrame = 0; // The current frame to be drawn
    public int spriteCounter;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 64, 64);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    private int frameDelay = 10;
    private int frameCounter = 0;
    public int lifespan;

    UtilityTool uTool = new UtilityTool();

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(images[currentFrame], screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void updateFrame() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % images.length;
            frameCounter = 0;
        }
    }
}
