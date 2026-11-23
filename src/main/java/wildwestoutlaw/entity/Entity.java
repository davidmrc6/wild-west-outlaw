package wildwestoutlaw.entity;

import java.awt.Color;
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

    public int actionLockCounter = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;

    public int maxLife;
    public int life;
    public boolean alive = false;
    public boolean dying = false;
    public boolean damaged = false;
    public boolean invincible = false;
    public boolean onPath = true;
    public int attack;
    public Projectile projectile;
    public int shotAvailableCounter = 0;
    public int invincibleCounter;
    public int type;
    public int reputation;
    public int maxReputation;
    public int wanted;
    public int maxWanted;
    public int coins;
    public int maxCoins;
    public int dialogueIndex = 0;
    public boolean hasGameJustStarted = true;
    public boolean hasDialogue = false;
    public boolean doesCollectCoins = false;
    public String dialogues[] = new String[20];

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

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                BufferedImage image = sprites.get(direction)[animation.frame()];

                // Monster health bar
                if (type == 2) {

                    double oneScale = (double)gp.tileSize / maxLife;
                    double hpBarValue = oneScale * life;

                    g2.setColor(new Color(35, 35, 35));
                    g2.fillRect(screenX - 1, screenY - 11, gp.tileSize + 2, 6);
                    g2.setColor(new Color(255, 0, 80));
                    g2.fillRect(screenX, screenY - 10, (int)hpBarValue, 4);
                }

                g2.drawImage(image, screenX, screenY, null);
            }

    }

    public void setAction() {}

    public void speak() {}

    public void checkCollision() {

        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        boolean contactPlayer = gp.collisionChecker.checkPlayerCollision(this);
        gp.collisionChecker.checkEntityCollision(this, gp.monster);
        gp.collisionChecker.checkEntityCollision(this, gp.npc);

        if (this.type == 2 && contactPlayer == true) {
            gp.player.takeDamage(attack);
        }
    }

    public void update() {

        setAction();
        checkCollision();

        if (collisionOn == false && type == 2) {
            worldX += direction.dx * speed;
            worldY += direction.dy * speed;
            animation.update();
        }
    }

    public void searchPath(int goalCol, int goalRow) {

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

         gp.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

         if (gp.pathFinder.search() == true) {

             int nextX = gp.pathFinder.pathList.get(0).col * gp.tileSize;
             int nextY = gp.pathFinder.pathList.get(0).row * gp.tileSize;

             int enLeftX = worldX + solidArea.x;
             int enRightX = worldX + solidArea.x + solidArea.width;
             int enTopY = worldY + solidArea.y;
             int enBottomY = worldY + solidArea.y + solidArea.height;

             if (enTopY > nextY && enLeftX > nextX && enRightX < nextX + gp.tileSize) {
                 direction = Direction.UP;
             }
             else if (enTopY < nextY && enLeftX > nextX && enRightX < nextX + gp.tileSize) {
                 direction = Direction.DOWN;
             }
             else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                 if (enLeftX > nextX)
                     direction = Direction.LEFT;
                 if (enLeftX < nextX)
                     direction = Direction.RIGHT;
             }
             else if (enTopY > nextY && enLeftX > nextX) {
                 direction = Direction.UP;
                 checkCollision();
                 if (collisionOn == true) {
                     direction = Direction.LEFT;
                 }
             }
             else if (enTopY > nextY && enLeftX < nextX) {
                 direction = Direction.UP;
                 checkCollision();
                 if (collisionOn == true) {
                     direction = Direction.RIGHT;
                 }
             }
             else if (enTopY < nextY && enLeftX > nextX) {
                 direction = Direction.DOWN;
                 checkCollision();
                 if (collisionOn == true) {
                     direction = Direction.LEFT;
                 }
             }
             else if (enTopY < nextY && enLeftX < nextX) {
                 direction = Direction.DOWN;
                 checkCollision();
                 if (collisionOn == true) {
                     direction = Direction.RIGHT;
                 }
             }
         }
    }

}
