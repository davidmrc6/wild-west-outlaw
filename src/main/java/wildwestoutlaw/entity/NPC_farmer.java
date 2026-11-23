package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

import java.awt.Rectangle;
import java.util.Random;

public class NPC_farmer extends Entity {
    
    public NPC_farmer(GamePanel gp) {

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
        direction = Direction.DOWN;
        type = 1;
        doesCollectCoins = true;
    }

    public void getNPCImage() {
        sprites.put(Direction.RIGHT, still("/npc/farmerNPC right.png"));
        sprites.put(Direction.LEFT, still("/npc/farmerNPC left.png"));
        sprites.put(Direction.UP, still("/npc/farmer back.png"));
        sprites.put(Direction.DOWN, still("/npc/farmer front.png"));
    }

    public void setAction() {
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
