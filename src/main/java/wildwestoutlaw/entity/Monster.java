package wildwestoutlaw.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;

public class Monster extends Entity {

    private static final int DEATH_TIME = 20; // frames a killed monster lies there before it explodes

    public final MonsterType type;
    public boolean dying = false;
    public boolean damaged = false;
    private int deathTimer = 0;
    private final BufferedImage deathSprite;

    public Monster(GamePanel gp, MonsterType type) {
        super(gp);

        this.type = type;
        name = type.folder;
        speed = type.speed;
        life = type.life;
        maxLife = life;
        attack = type.attack;
        alive = true;
        direction = Direction.DOWN;

        solidArea = new Rectangle(18, 12, 28, 44);

        animation = new Animation(10, 5, true);
        loadSprites("/mobs/" + type.folder, 10);
        deathSprite = type.deathSprite != null ? Assets.image(type.deathSprite, gp.tileSize) : null;
    }

    public void startDying() {
        dying = true;
        deathTimer = 0;
    }

    // A dying monster can't be bumped into, shot again or hurt the player
    @Override
    public boolean isSolid() {
        return !dying;
    }

    @Override
    public void update() {

        if (dying) {
            deathTimer++;
            if (deathTimer >= DEATH_TIME) {
                alive = false;
            }
            return;
        }

        setAction();
        checkCollision();

        if (collisionOn == false) {
            worldX += direction.dx * speed;
            worldY += direction.dy * speed;
            animation.update();
        }
    }

    @Override
    public boolean checkCollision() {
        boolean contactPlayer = super.checkCollision();
        if (contactPlayer) {
            gp.player.takeDamage(attack);
        }
        return contactPlayer;
    }

    @Override
    public void draw(Graphics2D g2) {

        if (!isOnScreen()) {
            return;
        }

        // Blinks in its death pose before exploding
        if (dying) {
            if ((deathTimer / 4) % 2 == 0) {
                g2.drawImage(deathSprite != null ? deathSprite : currentImage(), screenX(), screenY(), null);
            }
            return;
        }

        // Health bar
        double oneScale = (double)gp.tileSize / maxLife;
        double hpBarValue = oneScale * life;

        g2.setColor(new Color(35, 35, 35));
        g2.fillRect(screenX() - 1, screenY() - 11, gp.tileSize + 2, 6);
        g2.setColor(new Color(255, 0, 80));
        g2.fillRect(screenX(), screenY() - 10, (int)hpBarValue, 4);

        g2.drawImage(currentImage(), screenX(), screenY(), null);
    }

    // Chases the player using the path finder
    @Override
    public void setAction() {

        int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
        int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;

        searchPath(goalCol, goalRow);
    }

    private void searchPath(int goalCol, int goalRow) {

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
