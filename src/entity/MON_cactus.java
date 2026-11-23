package entity;

import java.awt.Rectangle;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;

public class MON_cactus extends Entity {

    public MON_cactus(GamePanel gp) {
        super(gp);

        name = "cactus";
        speed = 3;
        direction = "down";
        defense = 0;
        life = 4;
        maxLife = life;
        alive = true;
        dying = false;
        type = 2;
        attack = 1;

        solidArea = new Rectangle();
        solidArea.x = 18;
        solidArea.y = 12;
        solidArea.width = 28;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        onPath = true;

        getImage();
    }

    public void getImage() {
        try {
            
            right1 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 4.png"));
            right5 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 5.png"));
            right6 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 6.png"));
            right7 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 7.png"));
            right8 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 8.png"));
            right9 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 9.png"));
            right10 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/right 10.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 4.png"));
            left5 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 5.png"));
            left6 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 6.png"));
            left7 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 7.png"));
            left8 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 8.png"));
            left9 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 9.png"));
            left10 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/left 10.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 4.png"));
            up5 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 5.png"));
            up6 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 6.png"));
            up7 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 7.png"));
            up8 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 8.png"));
            up9 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 9.png"));
            up10 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/back 10.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 4.png"));
            down5 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 5.png"));
            down6 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 6.png"));
            down7 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 7.png"));
            down8 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 8.png"));
            down9 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 9.png"));
            down10 = ImageIO.read(getClass().getResourceAsStream("/mobs/cactus/front 10.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setAction() {

        if (onPath == true) {

            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;

            searchPath(goalCol, goalRow);
        }

        else {

            actionLockCounter ++;
            
            if (actionLockCounter == 120) {

                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25)
                    direction = "up";
                if (i > 25 && i <= 100)
                    direction = "down";
                if (i > 50 && i <= 75)
                    direction = "left";
                if (i > 75 && i <= 100)
                    direction = "right";

                actionLockCounter = 0;
            }

        }

    }
}
