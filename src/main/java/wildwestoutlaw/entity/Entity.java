package wildwestoutlaw.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;

public class Entity {

    protected GamePanel gp;

    public int worldX, worldY;
    public int speed;

    public String name;

    // The animation frames for each direction the entity can face
    protected final Map<Direction, BufferedImage[]> sprites = new EnumMap<>(Direction.class);
    protected Animation animation = new Animation(1, 1, true);

    public Direction direction = Direction.DOWN;

    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public boolean collisionOn = false;

    public int maxLife;
    public int life;
    public boolean alive = false;
    public int attack;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    // Loads "<folder>/back 1.png", "front 1.png", "left 1.png", "right 1.png" and so on
    protected void loadSprites(String folder, int frames) {
        sprites.put(Direction.UP, Assets.frames(folder + "/back %d.png", frames, gp.tileSize));
        sprites.put(Direction.DOWN, Assets.frames(folder + "/front %d.png", frames, gp.tileSize));
        sprites.put(Direction.LEFT, Assets.frames(folder + "/left %d.png", frames, gp.tileSize));
        sprites.put(Direction.RIGHT, Assets.frames(folder + "/right %d.png", frames, gp.tileSize));
    }

    // For characters with a single image per direction
    protected BufferedImage[] still(String path) {
        return new BufferedImage[] { Assets.image(path, gp.tileSize) };
    }

    // The first frame facing the camera (used on the title screen)
    public BufferedImage frontSprite() {
        return sprites.get(Direction.DOWN)[0];
    }

    // Whether other things bump into it
    public boolean isSolid() {
        return true;
    }

    // The hitbox in world coordinates
    public Rectangle worldHitbox() {
        return new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);
    }

    public int screenX() {
        return worldX - gp.player.worldX + gp.player.screenX;
    }

    public int screenY() {
        return worldY - gp.player.worldY + gp.player.screenY;
    }

    protected boolean isOnScreen() {
        return worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY;
    }

    protected BufferedImage currentImage() {
        return sprites.get(direction)[animation.frame()];
    }

    public void draw(Graphics2D g2) {
        if (isOnScreen()) {
            g2.drawImage(currentImage(), screenX(), screenY(), null);
        }
    }

    public void setAction() {}

    // Returns true if it is touching the player
    public boolean checkCollision() {

        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        boolean contactPlayer = gp.collisionChecker.checkPlayerCollision(this);
        gp.collisionChecker.checkEntityCollision(this, gp.monster);
        gp.collisionChecker.checkEntityCollision(this, gp.npc);
        return contactPlayer;
    }

    public void update() {
        setAction();
        checkCollision();
    }

}
