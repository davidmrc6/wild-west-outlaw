package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class NPC_female extends Entity {
    
    public NPC_female(GamePanel gp) {

        super(gp);

        solidArea = new Rectangle();
        solidArea.x = 18;
        solidArea.y = 12;
        solidArea.width = 28;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getNPCImage();

    }

    public void setDefaultValues() {
        worldX = gp.worldWidth / 2 - gp.tileSize / 2;
        worldY = gp.worldHeight / 2 - gp.tileSize / 2;
        speed = 2;
        direction = "down";
        isIdle = true;
        type = 1;
        doesCollectCoins = true;
    }

    public void getNPCImage() {
        try {

            right1 = ImageIO.read(getClass().getResourceAsStream("/npc/femaleNPC right.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/npc/femaleNPC left.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/npc/femaleNPC back.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/npc/femaleNPC front.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
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
