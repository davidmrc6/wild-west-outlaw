package wildwestoutlaw.entity;

import java.awt.Rectangle;
import java.util.Random;

import javax.imageio.ImageIO;

import wildwestoutlaw.GamePanel;

public class MON_coyote extends Entity {

    public MON_coyote(GamePanel gp) {
        super(gp);

        name = "coyote";
        speed = 5;
        direction = "down";
        defense = 0;
        life = 6;
        maxLife = life;
        alive = true;
        dying = false;
        type = 2;
        attack = 2;

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
            
            right1 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 4.png"));
            right5 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 5.png"));
            right6 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 6.png"));
            right7 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 7.png"));
            right8 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 8.png"));
            right9 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 9.png"));
            right10 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/right 10.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 4.png"));
            left5 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 5.png"));
            left6 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 6.png"));
            left7 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 7.png"));
            left8 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 8.png"));
            left9 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 9.png"));
            left10 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/left 10.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 4.png"));
            up5 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 5.png"));
            up6 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 6.png"));
            up7 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 7.png"));
            up8 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 8.png"));
            up9 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 9.png"));
            up10 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/back 10.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 4.png"));
            down5 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 5.png"));
            down6 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 6.png"));
            down7 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 7.png"));
            down8 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 8.png"));
            down9 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 9.png"));
            down10 = ImageIO.read(getClass().getResourceAsStream("/mobs/coyote/front 10.png"));

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
