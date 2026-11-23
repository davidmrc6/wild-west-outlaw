package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

import java.awt.Rectangle;
import java.util.Random;

public class NPC_bartender extends Entity {
    
    public NPC_bartender(GamePanel gp) {

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
        setDialogue();

    }

    public void setDefaultValues() {
        worldX = gp.worldWidth / 2 - gp.tileSize / 2;
        worldY = gp.worldHeight / 2 - gp.tileSize / 2;
        speed = 2;
        direction = Direction.DOWN;
        type = 1;
        hasDialogue = true;
    }

    public void getNPCImage() {
        sprites.put(Direction.RIGHT, still("/npc/barman R.png"));
        sprites.put(Direction.LEFT, still("/npc/barman L.png"));
        sprites.put(Direction.UP, still("/npc/barman back.png"));
        sprites.put(Direction.DOWN, still("/npc/barman front.png"));
    }

    public void setDialogue() {
        dialogues[0] = "Have you got any coins, traveler?";
        dialogues[1] = "I think giving some away would really improve your reputation in \nthis town...";
        dialogues[2] = "The cacti have stolen our money...";
        dialogues[3] = "If you need coins, you know who to take out.";
        dialogues[4] = "Here, take this gun. Press SPACE to shoot";
        dialogues[5] = "You get coins by killing the villain cacti and coyotes.";
        dialogues[6] = "Once you get some coins back, approach any of the townsfolk.";
        dialogues[7] = "If you get us back all the coins, you win by max reputation.";
        dialogues[8] = "Good luck!";
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

    public void speak() {

        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case UP, UP_LEFT, UP_RIGHT -> direction = Direction.DOWN;
            case DOWN, DOWN_LEFT, DOWN_RIGHT -> direction = Direction.UP;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }

        if (dialogueIndex == 8 && hasGameJustStarted == true) {
            gp.spawner.start();
            gp.player.wanted = 1;
            hasGameJustStarted = false;
        }
    }

}
