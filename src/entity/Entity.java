package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Entity {

    GamePanel gp;

    public int worldX, worldY;
    public int speed;

    public String name;

    public Rectangle hitbox;

    public BufferedImage left1, left2, left3, left4, left5, left6, left7, left8, left9, left10,
            right1, right2, right3, right4, right5, right6, right7, right8, right9, right10,
            up1, up2, up3, up4, up5, up6, up7, up8, up9, up10,
            down1, down2, down3, down4, down5, down6, down7, down8, down9, down10,
            downleft1, downleft2, downleft3, downleft4, downleft5, downleft6, downleft7, downleft8,
            downright1, downright2, downright3, downright4, downright5, downright6, downright7, downright8,
            upleft1, upleft2, upleft3, upleft4, upleft5, upleft6, upleft7, upleft8,
            upright1, upright2, upright3, upright4, upright5, upright6, upright7, upright8,
            idle_left1, idle_left2, idle_left3, idle_left4, idle_left5, idle_left6,
            idle_right1, idle_right2, idle_right3, idle_right4, idle_right5, idle_right6,
            idle_up1, idle_up2, idle_up3, idle_up4, idle_up5, idle_up6,
            idle_down1, idle_down2, idle_down3, idle_down4, idle_down5, idle_down6, 
            idle_downleft1, idle_downleft2, idle_downleft3, idle_downleft4, idle_downleft5, idle_downleft6,
            idle_downright1, idle_downright2, idle_downright3, idle_downright4, idle_downright5, idle_downright6,
            idle_upleft1, idle_upleft2, idle_upleft3, idle_upleft4, idle_upleft5, idle_upleft6,
            idle_upright1, idle_upright2, idle_upright3, idle_upright4, idle_upright5, idle_upright6,

            shooting_left1, shooting_left2, shooting_left3, shooting_left4, shooting_left5, shooting_left6,
            shooting_right1, shooting_right2, shooting_right3, shooting_right4, shooting_right5, shooting_right6,
            shooting_down1, shooting_down2, shooting_down3, shooting_down4, shooting_down5, shooting_down6,
            shooting_up1, shooting_up2, shooting_up3, shooting_up4, shooting_up5, shooting_up6,
            shooting_upleft1, shooting_upleft2, shooting_upleft3, shooting_upleft4, shooting_upleft5, shooting_upleft6,
            shooting_upright1, shooting_upright2, shooting_upright3, shooting_upright4, shooting_upright5, shooting_upright6,
            shooting_downleft1, shooting_downleft2, shooting_downleft3, shooting_downleft4, shooting_downleft5, shooting_downleft6,
            shooting_downright1, shooting_downright2, shooting_downright3, shooting_downright4, shooting_downright5, shooting_downright6,

            explosion1, explosion2, explosion3, explosion4, explosion5, explosion6, explosion7, explosion8,
            
            sprint_particle1, sprint_particle2, sprint_particle3;

    public String direction;

    public int actionLockCounter = 0;
    
    public boolean isIdle;

    public int spriteCounter = 0;
    public int spriteNumber = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;

    public int maxLife;
    public int life;
    public boolean alive = false;
    public boolean isShooting = false;
    public boolean dying = false;
    public boolean damaged = false;
    public boolean invincible = false;
    public boolean onPath = true;
    public int attack;
    public int maxMana;
    public int mana;
    public Projectile projectile;
    public int useCost;
    public int defense;
    public int shotAvailableCounter = 0;
    public int dyingCounter = 0;
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
    public int npcCoins = 0;
    public int maxNpcCoins = 1;
    public String dialogues[] = new String[20];

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                switch (direction) {
                   case "up":
                        switch (spriteNumber) {
                            case 1: image = up1; break;
                            case 2: image = up2; break;
                            case 3: image = up3; break;
                            case 4: image = up4; break;
                            case 5: image = up5; break;
                            case 6: image = up6; break;
                            case 7: image = up7; break;
                            case 8: image = up8; break;
                            case 9 : image = up9; break;
                            case 10: image = up10; break;                                
                        }
                    break;
                    case "down":
                        switch (spriteNumber) {
                            case 1: image = down1; break;
                            case 2: image = down2; break;
                            case 3: image = down3; break;
                            case 4: image = down4; break;
                            case 5: image = down5; break;
                            case 6: image = down6; break;
                            case 7: image = down7; break;
                            case 8: image = down8; break;
                            case 9 : image = down9; break;
                            case 10: image = down10; break; 
                        }
                    break;
                    case "left":
                        switch (spriteNumber) {
                            case 1: image = left1; break;
                            case 2: image = left2; break;
                            case 3: image = left3; break;
                            case 4: image = left4; break;
                            case 5: image = left5; break;
                            case 6: image = left6; break;
                            case 7: image = left7; break;
                            case 8 : image = left8; break;
                            case 9: image = left9; break;
                            case 10: image = left10; break;
                        }
                    break;
                    case "right":
                        switch (spriteNumber) {
                            case 1: image = right1; break;
                            case 2: image = right2; break;
                            case 3: image = right3; break;
                            case 4: image = right4; break;
                            case 5: image = right5; break;
                            case 6: image = right6; break;
                            case 7: image = right7; break;
                            case 8: image = right8; break;
                            case 9: image = right9; break;
                            case 10: image = right10; break;
                        }
                    break;
                    // For projectiles only
                    case "upright":
                        switch (spriteNumber) {
                            case 1: image = upright1; break;
                            case 2: image = upright2; break;
                        }
                    break;
                    case "upleft":
                        switch (spriteNumber) {
                            case 1: image = upleft1; break;
                            case 2: image = upleft2; break;
                        }
                    break;
                    case "downright":
                        switch (spriteNumber) {
                            case 1: image = downright1; break;
                            case 2: image = downright2; break;
                        }
                    break;
                    case "downleft":
                        switch (spriteNumber) {
                            case 1: image = downleft1; break;
                            case 2: image = downleft2; break;
                        }
                    break;
                }

                // Monster health bar
                if (type == 2) {

                    double oneScale = (double)gp.tileSize / maxLife;
                    double hpBarValue = oneScale * life;

                    g2.setColor(new Color(35, 35, 35));
                    g2.fillRect(screenX - 1, screenY - 11, gp.tileSize + 2, 6);
                    g2.setColor(new Color(255, 0, 80));
                    g2.fillRect(screenX, screenY - 10, (int)hpBarValue, 4);
                }

                if (dying == true) {
                    dying = false;
                    alive = false;
                }

                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
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
            if (gp.player.invincible == false) {
                gp.player.life --;
                gp.player.invincible = true;
            }
        } 
    }

    public void update() {

        setAction();
        checkCollision();

        if (collisionOn == false) {

            if (type == 2) {

                switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            	 }

                spriteCounter++;
                if (spriteCounter > 4) {
                    if (spriteNumber == 1) {
                        spriteNumber = 2;
                    } else if (spriteNumber == 2) {
                        spriteNumber = 3;
                    } else if (spriteNumber == 3) {
                        spriteNumber = 4;
                    } else if (spriteNumber == 4) {
                        spriteNumber = 5;
                    } else if (spriteNumber == 5) {
                        spriteNumber = 6;
                    } else if (spriteNumber == 6) {
                        spriteNumber = 7;
                    } else if (spriteNumber == 7) {
                        spriteNumber = 8;
                    } else if (spriteNumber == 8) {
                        spriteNumber = 9;
                    } else if (spriteNumber == 9) {
                        spriteNumber = 10;
                    } else if (spriteNumber == 10) {
                        spriteNumber = 1;
                    }
                    spriteCounter = 0;
                }
            }       
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
                 direction = "up";
             }
             else if (enTopY < nextY && enLeftX > nextX && enRightX < nextX + gp.tileSize) {
                 direction = "down";
             }
             else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                 if (enLeftX > nextX)
                     direction = "left";
                 if (enLeftX < nextX)
                     direction = "right";
             }
             else if (enTopY > nextY && enLeftX > nextX) {
                 direction = "up";
                 checkCollision();
                 if (collisionOn == true) {
                     direction = "left";
                 }
             }
             else if (enTopY > nextY && enLeftX < nextX) {
                 direction = "up";
                 checkCollision();
                 if (collisionOn == true) {
                     direction = "right";
                 }
             }
             else if (enTopY < nextY && enLeftX > nextX) {
                 direction = "down";
                 checkCollision();
                 if (collisionOn == true) {
                     direction = "left";
                 }
             }
             else if (enTopY < nextY && enLeftX < nextX) {
                 direction = "down";
                 checkCollision();
                 if (collisionOn == true) {
                     direction = "right";
                 }
             }
         }
    }

}
