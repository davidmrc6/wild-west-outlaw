package wildwestoutlaw.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.AlphaComposite;

import javax.imageio.ImageIO;

import wildwestoutlaw.GamePanel;
import wildwestoutlaw.KeyHandler;
import wildwestoutlaw.UtilityTool;
import wildwestoutlaw.object.OBJ_bullet;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player (GamePanel gp, KeyHandler keyH) {

        super(gp);
        
        this.keyH = keyH;

        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
        
        // The solidArea (aka the hitbox of the player) is slightly smaller than the player character
        solidArea = new Rectangle();
        solidArea.x = 16;
        solidArea.y = 24;
        solidArea.width = 31;
        solidArea.height = 42;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
        setDefaultPosition();
    }

    public void setDefaultPosition() {

        worldX = gp.worldWidth / 2 - gp.tileSize / 2;
        worldY = gp.worldHeight - (gp.tileSize * 8);
    }

    public void setDefaultValues() {

        speed = 4;
        direction = "down";
        isIdle = true;
        type = 0;

        maxLife = 12;
        life = maxLife;

        maxReputation = 12;
        reputation = 0;

        maxWanted = 12;
        wanted = 0;

        coins = 0;
        maxCoins = 3;

        projectile = new OBJ_bullet(gp);
    }

    public void restoreLife() {
        life = maxLife;
        reputation = 0;
        wanted = 0;
        coins = 0;
    }
    
    public void getPlayerImage() {

        right1 = setup("walking/Side/R1");
        right2 = setup("walking/Side/R2");
        right3 = setup("walking/Side/R3");
        right4 = setup("walking/Side/R4");
        right5 = setup("walking/Side/R5");
        right6 = setup("walking/Side/R6");
        right7 = setup("walking/Side/R7");
        right8 = setup("walking/Side/R8");
        left1 = setup("walking/Side/L1");
        left2 = setup("walking/Side/L2");
        left3 = setup("walking/Side/L3");
        left4 = setup("walking/Side/L4");
        left5 = setup("walking/Side/L5");
        left6 = setup("walking/Side/L6");
        left7 = setup("walking/Side/L7");
        left8 = setup("walking/Side/L8");
        up1 = setup("walking/Back/1");
        up2 = setup("walking/Back/2");
        up3 = setup("walking/Back/3");
        up4 = setup("walking/Back/4");
        up5 = setup("walking/Back/5");
        up6 = setup("walking/Back/6");
        up7 = setup("walking/Back/7");
        up8 = setup("walking/Back/8");
        down1 = setup("walking/Front/1");
        down2 = setup("walking/Front/2");
        down3 = setup("walking/Front/3");
        down4 = setup("walking/Front/4");
        down5 = setup("walking/Front/5");
        down6 = setup("walking/Front/6");
        down7 = setup("walking/Front/7");
        down8 = setup("walking/Front/8");
        downleft1 = setup("walking/Angle/Angle 1/DL1");
        downleft2 = setup("walking/Angle/Angle 1/DL2");
        downleft3 = setup("walking/Angle/Angle 1/DL3");
        downleft4 = setup("walking/Angle/Angle 1/DL4");
        downleft5 = setup("walking/Angle/Angle 1/DL5");
        downleft6 = setup("walking/Angle/Angle 1/DL6");
        downleft7 = setup("walking/Angle/Angle 1/DL7");
        downleft8 = setup("walking/Angle/Angle 1/DL8");
        downright1 = setup("walking/Angle/Angle 1/DR1");
        downright2 = setup("walking/Angle/Angle 1/DR2");
        downright3 = setup("walking/Angle/Angle 1/DR3");
        downright4 = setup("walking/Angle/Angle 1/DR4");
        downright5 = setup("walking/Angle/Angle 1/DR5");
        downright6 = setup("walking/Angle/Angle 1/DR6");
        downright7 = setup("walking/Angle/Angle 1/DR7");
        downright8 = setup("walking/Angle/Angle 1/DR8");
        upleft1 = setup("walking/Angle/Angle 2/UL1");
        upleft2 = setup("walking/Angle/Angle 2/UL2");
        upleft3 = setup("walking/Angle/Angle 2/UL3");
        upleft4 = setup("walking/Angle/Angle 2/UL4");
        upleft5 = setup("walking/Angle/Angle 2/UL5");
        upleft6 = setup("walking/Angle/Angle 2/UL6");
        upleft7 = setup("walking/Angle/Angle 2/UL7");
        upleft8 = setup("walking/Angle/Angle 2/UL8");
        upright1 = setup("walking/Angle/Angle 2/UR1");
        upright2 = setup("walking/Angle/Angle 2/UR2");
        upright3 = setup("walking/Angle/Angle 2/UR3");
        upright4 = setup("walking/Angle/Angle 2/UR4");
        upright5 = setup("walking/Angle/Angle 2/UR5");
        upright6 = setup("walking/Angle/Angle 2/UR6");
        upright7 = setup("walking/Angle/Angle 2/UR7");
        upright8 = setup("walking/Angle/Angle 2/UR8");
        sprint_particle1 = setup("particles/sprint1 12x12");
        sprint_particle2 = setup("particles/sprint2 12x12");
        sprint_particle3 = setup("particles/sprint3 12x12");
        idle_downleft1 = setup("idle/Angle 1/DL1");
        idle_downleft2 = setup("idle/Angle 1/DL2");
        idle_downleft3 = setup("idle/Angle 1/DL3");
        idle_downleft4 = setup("idle/Angle 1/DL4");
        idle_downleft5 = setup("idle/Angle 1/DL5");
        idle_downleft6 = setup("idle/Angle 1/DL6");
        idle_downright1 = setup("idle/Angle 1/DR1");
        idle_downright2 = setup("idle/Angle 1/DR2");
        idle_downright3 = setup("idle/Angle 1/DR3");
        idle_downright4 = setup("idle/Angle 1/DR4");
        idle_downright5 = setup("idle/Angle 1/DR5");
        idle_downright6 = setup("idle/Angle 1/DR6");
        idle_upleft1 = setup("idle/Angle 2/UL1");
        idle_upleft2 = setup("idle/Angle 2/UL2");
        idle_upleft3 = setup("idle/Angle 2/UL3");
        idle_upleft4 = setup("idle/Angle 2/UL4");
        idle_upleft5 = setup("idle/Angle 2/UL5");
        idle_upleft6 = setup("idle/Angle 2/UL6");
        idle_upright1 = setup("idle/Angle 2/UR1");
        idle_upright2 = setup("idle/Angle 2/UR2");
        idle_upright3 = setup("idle/Angle 2/UR3");
        idle_upright4 = setup("idle/Angle 2/UR4");
        idle_upright5 = setup("idle/Angle 2/UR5");
        idle_upright6 = setup("idle/Angle 2/UR6");
        idle_right1 = setup("idle/Side/R1");
        idle_right2 = setup("idle/Side/R2");
        idle_right3 = setup("idle/Side/R3");
        idle_right4 = setup("idle/Side/R4");
        idle_right5 = setup("idle/Side/R5");
        idle_right6 = setup("idle/Side/R6");
        idle_left1 = setup("idle/Side/L1");
        idle_left2 = setup("idle/Side/L2");
        idle_left3 = setup("idle/Side/L3");
        idle_left4 = setup("idle/Side/L4");
        idle_left5 = setup("idle/Side/L5");
        idle_left6 = setup("idle/Side/L6");
        idle_up1 = setup("idle/Back/1");
        idle_up2 = setup("idle/Back/2");
        idle_up3 = setup("idle/Back/3");
        idle_up4 = setup("idle/Back/4");
        idle_up5 = setup("idle/Back/5");
        idle_up6 = setup("idle/Back/6");
        idle_down1 = setup("idle/Front/1");
        idle_down2 = setup("idle/Front/2");
        idle_down3 = setup("idle/Front/3");
        idle_down4 = setup("idle/Front/4");
        idle_down5 = setup("idle/Front/5");
        idle_down6 = setup("idle/Front/6");
        shooting_down1 = setup("shooting/down 1");
        shooting_down2 = setup("shooting/down 2");
        shooting_down3 = setup("shooting/down 3");
        shooting_down4 = setup("shooting/down 4");
        shooting_down5 = setup("shooting/down 5");
        shooting_down6 = setup("shooting/down 6");
        shooting_up1 = setup("shooting/up 1");
        shooting_up2 = setup("shooting/up 2");
        shooting_up3 = setup("shooting/up 3");
        shooting_up4 = setup("shooting/up 4");
        shooting_up5 = setup("shooting/up 5");
        shooting_up6 = setup("shooting/up 6");
        shooting_left1 = setup("shooting/left 1");
        shooting_left2 = setup("shooting/left 2");
        shooting_left3 = setup("shooting/left 3");
        shooting_left4 = setup("shooting/left 4");
        shooting_left5 = setup("shooting/left 5");
        shooting_left6 = setup("shooting/left 6");
        shooting_right1 = setup("shooting/right 1");
        shooting_right2 = setup("shooting/right 2");
        shooting_right3 = setup("shooting/right 3");
        shooting_right4 = setup("shooting/right 4");
        shooting_right5 = setup("shooting/right 5");
        shooting_right6 = setup("shooting/right 6");
        shooting_downleft1 = setup("shooting/downleft 1");
        shooting_downleft2 = setup("shooting/downleft 2");
        shooting_downleft3 = setup("shooting/downleft 3");
        shooting_downleft4 = setup("shooting/downleft 4");
        shooting_downleft5 = setup("shooting/downleft 5");
        shooting_downleft6 = setup("shooting/downleft 6");
        shooting_downright1 = setup("shooting/downright 1");
        shooting_downright2 = setup("shooting/downright 2");
        shooting_downright3 = setup("shooting/downright 3");
        shooting_downright4 = setup("shooting/downright 4");
        shooting_downright5 = setup("shooting/downright 5");
        shooting_downright6 = setup("shooting/downright 6");
        shooting_upright1 = setup("shooting/upright 1");
        shooting_upright2 = setup("shooting/upright 2");
        shooting_upright3 = setup("shooting/upright 3");
        shooting_upright4 = setup("shooting/upright 4");
        shooting_upright5 = setup("shooting/upright 5");
        shooting_upright6 = setup("shooting/upright 6");
        shooting_upleft1 = setup("shooting/upleft 1");
        shooting_upleft2 = setup("shooting/upleft 2");
        shooting_upleft3 = setup("shooting/upleft 3");
        shooting_upleft4 = setup("shooting/upleft 4");
        shooting_upleft5 = setup("shooting/upleft 5");
        shooting_upleft6 = setup("shooting/upleft 6");
        
    }

    public BufferedImage setup (String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    public void update() {

        if (keyH.leftPressed == true || keyH.rightPressed == true ||
                keyH.upPressed == true || keyH.downPressed == true) {
            if (keyH.upPressed == true &&  keyH.rightPressed == true) {
                direction = "upright";
                isIdle = false;
            } else if (keyH.upPressed == true &&  keyH.leftPressed == true) {
                direction = "upleft";
                isIdle = false;
            } else if (keyH.downPressed == true &&  keyH.rightPressed == true) {
                direction = "downright";
                isIdle = false;
            } else if (keyH.downPressed == true &&  keyH.leftPressed == true) {
                direction = "downleft";
                isIdle = false;
            } else if (keyH.downPressed == true) {
                direction = "down";
                isIdle = false;
            } else if (keyH.rightPressed == true) {
                direction = "right";
                isIdle = false;
            } else if (keyH.leftPressed == true) {
                direction = "left";
                isIdle = false;
            } else if (keyH.upPressed == true) {
                direction = "up";
                isIdle = false;
            }

            // Check tile collision
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            // Check object collision
            int objIndex = gp.collisionChecker.checkObjectCollision(this, true);
            pickUpObject(objIndex);

            // Check monster collision
            int monsterIndex = gp.collisionChecker.checkEntityCollision(this, gp.monster);
            contactMonster(monsterIndex);

            // Check npc collision
            int npcIndex = gp.collisionChecker.checkEntityCollision(this, gp.npc);
            interactNPC(npcIndex);

            // If collision is false, player can move
            if (collisionOn == false) {
                switch (direction) {
                    case "upright":
                        worldY -= speed;
                        worldX += speed;
                        break;
                    case "upleft":
                        worldY -= speed;
                        worldX -= speed;
                        break;
                    case "downright":
                        worldY += speed;
                        worldX += speed;
                        break;
                    case "downleft":
                        worldY += speed;
                        worldX -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "up":
                        worldY -= speed;
                        break;
                }
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
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }

        if (keyH.spacePressed == true && projectile.alive == false && shotAvailableCounter == 30) {
            projectile.set(worldX, worldY, direction, true, this);

            gp.projectileList.add(projectile);

            isShooting = true;

            shotAvailableCounter = 0;
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        if (keyH.downPressed == false && keyH.upPressed == false &&
                keyH.leftPressed == false && keyH.rightPressed == false) {
            isIdle = true;
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
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }

        if (invincible == true) {
            invincibleCounter ++;
            if (invincibleCounter == 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (life <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        if (isShooting == true) {

            if (spriteNumber > 6) spriteNumber = 1;

            switch (direction) {
                case "right":
                    switch (spriteNumber) {
                        case 1: image = shooting_right1; break;
                        case 2: image = shooting_right2; break;
                        case 3: image = shooting_right3; break;
                        case 4: image = shooting_right4; break;
                        case 5: image = shooting_right5; break;
                        case 6: image = shooting_right6; isShooting = false; break;
                    }
                break;
                case "down":
                    switch (spriteNumber) {
                        case 1: image = shooting_down1; break;
                        case 2: image = shooting_down2; break;
                        case 3: image = shooting_down3; break;
                        case 4: image = shooting_down4; break;
                        case 5: image = shooting_down5; break;
                        case 6: image = shooting_down6; isShooting = false; break;
                    }
                break;
                case "left":
                    switch (spriteNumber) {
                        case 1: image = shooting_left1; break;
                        case 2: image = shooting_left2; break;
                        case 3: image = shooting_left3; break;
                        case 4: image = shooting_left4; break;
                        case 5: image = shooting_left5; break;
                        case 6: image = shooting_left6; isShooting = false; break;
                    }
                break;
                case "up":
                    switch (spriteNumber) {
                        case 1: image = shooting_up1; break;
                        case 2: image = shooting_up2; break;
                        case 3: image = shooting_up3; break;
                        case 4: image = shooting_up4; break;
                        case 5: image = shooting_up5; break;
                        case 6: image = shooting_up6; isShooting = false; break;
                    }
                break;
                case "upright":
                    switch (spriteNumber) {
                        case 1: image = shooting_upright1; break;
                        case 2: image = shooting_upright2; break;
                        case 3: image = shooting_upright3; break;
                        case 4: image = shooting_upright4; break;
                        case 5: image = shooting_upright5; break;
                        case 6: image = shooting_upright6; isShooting = false; break;
                    }
                break;
                case "upleft":
                    switch (spriteNumber) {
                        case 1: image = shooting_upleft1; break;
                        case 2: image = shooting_upleft2; break;
                        case 3: image = shooting_upleft3; break;
                        case 4: image = shooting_upleft4; break;
                        case 5: image = shooting_upleft5; break;
                        case 6: image = shooting_upleft6; isShooting = false; break;
                    }
                break;
                case "downright":
                    switch (spriteNumber) {
                        case 1: image = shooting_downright1; break;
                        case 2: image = shooting_downright2; break;
                        case 3: image = shooting_downright3; break;
                        case 4: image = shooting_downright4; break;
                        case 5: image = shooting_downright5; break;
                        case 6: image = shooting_downright6; isShooting = false; break;
                    }
                break;
                case "downleft":
                    switch (spriteNumber) {
                        case 1: image = shooting_downleft1; break;
                        case 2: image = shooting_downleft2; break;
                        case 3: image = shooting_downleft3; break;
                        case 4: image = shooting_downleft4; break;
                        case 5: image = shooting_downleft5; break;
                        case 6: image = shooting_downleft6; isShooting = false; break;
                    }
                break;
            }
            if (invincible == true) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            }

            g2.drawImage(image, screenX, screenY, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        if (isShooting == false) {

            if (!isIdle) {

                switch (direction) {
                    case "right":
                        switch (spriteNumber) {
                            case 1:
                                image = right1;
                                g2.drawImage(sprint_particle1, screenX - 3, screenY + 22, 12, 12, null);
                                break;
                            case 2:
                                image = right2;
                                break;
                            case 3:
                                image = right3;
                                g2.drawImage(sprint_particle2, screenX - 3, screenY + 22, 12, 12, null);
                                break;
                            case 4:
                                image = right4;
                                break;
                            case 5:
                                image = right5;
                                g2.drawImage(sprint_particle3, screenX - 3, screenY + 22, 12, 12, null);
                                break;
                            case 6:
                                image = right6;
                                break;
                            case 7:
                                image = right7;
                                break;
                            case 8:
                                image = right8;
                                break;
                        }
                    break;
                    case "left":
                        switch (spriteNumber) {
                            case 1:
                                image = left1;
                                g2.drawImage(sprint_particle1, screenX + 22, screenY + 22, 12, 12, null);
                                break;
                            case 2:
                                image = left2;
                                break;
                            case 3:
                                image = left3;
                                g2.drawImage(sprint_particle2, screenX + 22, screenY + 22, 12, 12, null);
                                break;
                            case 4:
                                image = left4;
                                break;
                            case 5:
                                image = left5;
                                g2.drawImage(sprint_particle3, screenX + 22, screenY + 22, 12, 12, null);
                                break;
                            case 6:
                                image = left6;
                                break;
                            case 7:
                                image = left7;
                                break;
                            case 8:
                                image = left8;
                                break;
                        }
                    break;
                    case "up":
                        switch (spriteNumber) {
                            case 1:
                                image = up1;
                                g2.drawImage(sprint_particle1, screenX + 10, screenY + 28, 12, 12, null);
                                break;
                            case 2:
                                image = up2;
                                break;
                            case 3:
                                image = up3;
                                g2.drawImage(sprint_particle2, screenX + 10, screenY + 28, 12, 12, null);
                                break;
                            case 4:
                                image = up4;
                                break;
                            case 5:
                                image = up5;
                                g2.drawImage(sprint_particle3, screenX + 10, screenY + 28, 12, 12, null);
                                break;
                            case 6:
                                image = up6;
                                break;
                            case 7:
                                image = up7;
                                break;
                            case 8:
                                image = up8;
                                break;
                        }
                    break;
                    case "down":
                        switch (spriteNumber) {
                            case 1:
                                image = down1;
                                break;
                            case 2:
                                image = down2;
                                break;
                            case 3:
                                image = down3;
                                break;
                            case 4:
                                image = down4;
                                break;
                            case 5:
                                image = down5;
                                break;
                            case 6:
                                image = down6;
                                break;
                            case 7:
                                image = down7;
                                break;
                            case 8:
                                image = down8;
                                break;
                        }
                    break;
                    case "upright": 
                        switch (spriteNumber) {
                            case 1:
                                image = upright1;
                                g2.drawImage(sprint_particle1, screenX - 3, screenY + 28, 12, 12, null);
                                break;
                            case 2:
                                image = upright2;
                                break;
                            case 3:
                                image = upright3;
                                g2.drawImage(sprint_particle2, screenX - 3, screenY + 28, 12, 12, null);
                                break;
                            case 4:
                                image = upright4;
                                break;
                            case 5:
                                image = upright5;
                                g2.drawImage(sprint_particle3, screenX - 3, screenY + 28, 12, 12, null);
                                break;
                            case 6:
                                image = upright6;
                                break;
                            case 7:
                                image = upright7;
                                break;
                            case 8:
                                image = upright8;
                                break;
                        }
                    break;
                    case "upleft":
                        switch (spriteNumber) {
                            case 1:
                                image = upleft1;
                                g2.drawImage(sprint_particle1, screenX + 22, screenY + 28, 12, 12, null);
                                break;
                            case 2:
                                image = upleft2;
                                break;
                            case 3:
                                image = upleft3;
                                g2.drawImage(sprint_particle2, screenX + 22, screenY + 28, 12, 12, null);
                                break;
                            case 4:
                                image = upleft4;
                                break;
                            case 5:
                                image = upleft5;
                                g2.drawImage(sprint_particle3, screenX + 22, screenY + 28, 12, 12, null);
                                break;
                            case 6:
                                image = upleft6;
                                break;
                            case 7:
                                image = upleft7;
                                break;
                            case 8:
                                image = upleft8;
                                break;
                        }
                    break;
                    case "downright":
                        switch (spriteNumber) {
                            case 1:
                                image = downright1;
                                g2.drawImage(sprint_particle1, screenX - 3, screenY + 18, 12, 12, null);
                                break;
                            case 2:
                                image = downright2;
                                break;
                            case 3:
                                image = downright3;
                                g2.drawImage(sprint_particle2, screenX - 3, screenY + 18, 12, 12, null);
                                break;
                            case 4:
                                image = downright4;
                                break;
                            case 5:
                                image = downright5;
                                g2.drawImage(sprint_particle3, screenX - 3, screenY + 18, 12, 12, null);
                                break;
                            case 6:
                                image = downright6;
                                break;
                            case 7:
                                image = downright7;
                                break;
                            case 8:
                                image = downright8;
                                break;
                        }
                    break;
                    case "downleft":
                        switch (spriteNumber) {
                            case 1:
                                image = downleft1;
                                g2.drawImage(sprint_particle1, screenX + 22, screenY + 18, 12, 12, null);
                                break;
                            case 2:
                                image = downleft2;
                                break;
                            case 3:
                                image = downleft3;
                                g2.drawImage(sprint_particle2, screenX + 22, screenY + 18, 12, 12, null);
                                break;
                            case 4:
                                image = downleft4;
                                break;
                            case 5:
                                image = downleft5;
                                g2.drawImage(sprint_particle3, screenX + 22, screenY + 18, 12, 12, null);
                                break;
                            case 6:
                                image = downleft6;
                                break;
                            case 7:
                                image = downleft7;
                                break;
                            case 8:
                                image = downleft8;
                                break;
                        }
                    break;
                }
            }

            if (isIdle) {
                
                if (spriteNumber > 6) spriteNumber = 1;

                switch (direction) {
                    case "right":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_right1;
                                break;
                            case 2:
                                image = idle_right2;
                                break;
                            case 3:
                                image = idle_right3;
                                break;
                            case 4:
                                image = idle_right4;
                                break;
                            case 5:
                                image = idle_right5;
                                break;
                            case 6:
                                image = idle_right6;
                                break;
                        }
                    break;
                    case "left":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_left1;
                                break;
                            case 2:
                                image = idle_left2;
                                break;
                            case 3:
                                image = idle_left3;
                                break;
                            case 4:
                                image = idle_left4;
                                break;
                            case 5:
                                image = idle_left5;
                                break;
                            case 6:
                                image = idle_left6;
                            break;
                        }
                    break;
                    case "up":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_up1;
                                break;
                            case 2:
                                image = idle_up2;
                                break;
                            case 3:
                                image = idle_up3;
                                break;
                            case 4:
                                image = idle_up4;
                                break;
                            case 5:
                                image = idle_up5;
                                break;
                            case 6:
                                image = idle_up6;
                                break;
                        }
                    break;
                    case "down":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_down1;
                                break;
                            case 2:
                                image = idle_down2;
                                break;
                            case 3:
                                image = idle_down3;
                                break;
                            case 4:
                                image = idle_down4;
                                break;
                            case 5:
                                image = idle_down5;
                                break;
                            case 6:
                                image = idle_down6;
                                break;
                        }
                    break;
                    case "upright": 
                        switch (spriteNumber) {
                            case 1:
                                image = idle_upright1;
                                break;
                            case 2:
                                image = idle_upright2;
                                break;
                            case 3:
                                image = idle_upright3;
                                break;
                            case 4:
                                image = idle_upright4;
                                break;
                            case 5:
                                image = idle_upright5;
                                break;
                            case 6:
                                image = idle_upright6;
                                break;
                        }
                    break;
                    case "upleft":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_upleft1;
                                break;
                            case 2:
                                image = idle_upleft2;
                                break;
                            case 3:
                                image = idle_upleft3;
                                break;
                            case 4:
                                image = idle_upleft4;
                                break;
                            case 5:
                                image = idle_upleft5;
                                break;
                            case 6:
                                image = idle_upleft6;
                                break;
                        }
                    break;
                    case "downright":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_downright1;
                                break;
                            case 2:
                                image = idle_downright2;
                                break;
                            case 3:
                                image = idle_downright3;
                                break;
                            case 4:
                                image = idle_downright4;
                                break;
                            case 5:
                                image = idle_downright5;
                                break;
                            case 6:
                                image = idle_downright6;
                                break;
                        }
                    break;
                    case "downleft":
                        switch (spriteNumber) {
                            case 1:
                                image = idle_downleft1;
                                break;
                            case 2:
                                image = idle_downleft2;
                                break;
                            case 3:
                                image = idle_downleft3;
                                break;
                            case 4:
                                image = idle_downleft4;
                                break;
                            case 5:
                                image = idle_downleft5;
                                break;
                            case 6:
                                image = idle_downleft6;
                                break;
                        }
                    break;
                }
            }

            if (invincible == true) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            }

            g2.drawImage(image, screenX, screenY, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        }
    }

    public void interactNPC(int i) {

        if (i != 999) {

            if (gp.npc[i].hasDialogue == true) {   
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
                gp.playSE(4);
            }
            if (gp.npc[i].doesCollectCoins == true && gp.npc[i].npcCoins == 0 && coins > 0) {
                gp.playSE(2);
                gp.npc[i].coins += 1;
                coins --;
                reputation += 1;
                System.out.println("reputation " + reputation);
            }

        }
    }

    public void pickUpObject(int i) {

        if (i != 999) {

            if (coins < maxCoins) {
                gp.playSE(1);
                coins ++ ;
                gp.objectList.remove(i);
            }
        }
    }

    public void contactMonster(int i) {

        if (i != 999) {
            if (invincible == false) {
                life -= gp.monster[i].attack;
                invincible = true;
                gp.playSE(8);
            }
        }

    }

    public void damageMonster(int i, int attack) {

        if (i != 999) {

            gp.monster[i].life -= attack;

            if (gp.monster[i].life > 0) {
                gp.monster[i].damaged = true;
            }

            if (gp.monster[i].life <= 0){
                gp.monster[i].dying = true;
            }

        }

    }

}