package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
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
        direction = "down";
        isIdle = true;
        type = 1;
        hasDialogue = true;
    }

    public void getNPCImage() {
        try {

            right1 = ImageIO.read(getClass().getResourceAsStream("/npc/barman R.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/npc/barman L.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/npc/barman back.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/npc/barman front.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void speak() {

        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up", "upleft", "upright": direction = "down"; break;
            case "down", "downleft", "downright": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;

        }

        if (dialogueIndex == 8 && hasGameJustStarted == true) {
            gp.monsterSpawning = true;
            gp.player.wanted = 1;
            hasGameJustStarted = false;
        }
    }

}
