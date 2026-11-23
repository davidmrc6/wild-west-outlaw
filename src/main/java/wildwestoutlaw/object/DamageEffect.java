package wildwestoutlaw.object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;
import wildwestoutlaw.entity.Entity;

// The small burst shown on a monster when it is hit (it follows the monster)
public class DamageEffect {

    GamePanel gp;
    Entity entity;

    private final BufferedImage[] frames;
    private final Animation animation = new Animation(4, 5, false);
    int worldX, worldY;

    public DamageEffect(GamePanel gp, Entity entity) {

        this.entity = entity;

        this.gp = gp;

        frames = Assets.frames("/player/dmgExplosion/explosion %d.png", 4, gp.tileSize);
    }

    public void update() {

        this.worldX = entity.worldX;
        this.worldY = entity.worldY;

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
