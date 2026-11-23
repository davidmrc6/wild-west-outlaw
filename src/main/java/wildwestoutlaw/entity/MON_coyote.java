package wildwestoutlaw.entity;

import java.awt.Rectangle;
import java.util.Random;

import wildwestoutlaw.Animation;
import wildwestoutlaw.GamePanel;

public class MON_coyote extends Entity {

    public MON_coyote(GamePanel gp) {
        super(gp);

        name = "coyote";
        speed = 5;
        direction = Direction.DOWN;
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
        animation = new Animation(10, 5, true);

        getImage();
    }

    public void getImage() {
        loadSprites("/mobs/coyote", 10);
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
                    direction = Direction.UP;
                if (i > 25 && i <= 100)
                    direction = Direction.DOWN;
                if (i > 50 && i <= 75)
                    direction = Direction.LEFT;
                if (i > 75 && i <= 100)
                    direction = Direction.RIGHT;

                actionLockCounter = 0;
            }

        }

    }
}
