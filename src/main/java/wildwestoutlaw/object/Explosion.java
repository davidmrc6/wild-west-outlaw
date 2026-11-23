package wildwestoutlaw.object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;

public class Explosion {

    GamePanel gp;

    private final BufferedImage[] frames;
    private final Animation animation = new Animation(8, 6, false);
    int worldX, worldY;

    public Explosion(GamePanel gp, int worldX, int worldY) {

        this.gp = gp;

        this.worldX = worldX;
        this.worldY = worldY;

        frames = Assets.frames("/player/explosion/explosion %d.png", 8, gp.tileSize);
    }

    public void update() {
        animation.update();
    }

    public boolean isFinished() {
        return animation.isFinished();
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(frames[animation.frame()], screenX, screenY, null);
    }

}
