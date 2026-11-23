package wildwestoutlaw.entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;

public class Bullet extends Projectile {

    public Bullet(GamePanel gp) {
        super(gp);

        name = "Bullet";
        speed = 10;
        maxLife = 50;
        life = maxLife;
        attack = 2;
        alive = false;

        // The sprite fills a whole tile with the bullet in the middle, so the hitbox sits in the middle too
        solidArea = new Rectangle(16, 16, 32, 32);
        animation = new Animation(2, 13, true);

        getImage();
    }

    public void getImage() {
        sprites.put(Direction.RIGHT, frames("LR"));
        sprites.put(Direction.LEFT, frames("LR"));
        sprites.put(Direction.UP, frames("Up"));
        sprites.put(Direction.DOWN, frames("Dn"));
        sprites.put(Direction.UP_LEFT, frames("UpL"));
        sprites.put(Direction.UP_RIGHT, frames("UpR"));
        sprites.put(Direction.DOWN_LEFT, frames("DnL"));
        sprites.put(Direction.DOWN_RIGHT, frames("DnR"));
    }

    private BufferedImage[] frames(String name) {
        return Assets.frames("/player/projectiles/" + name + " %d.png", 2, gp.tileSize);
    }
}
