package wildwestoutlaw;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import wildwestoutlaw.entity.NPC_bartender;
import wildwestoutlaw.entity.NPC_farmer;
import wildwestoutlaw.entity.NPC_female;
import wildwestoutlaw.entity.MON_cactus;
import wildwestoutlaw.entity.MON_coyote;

public class AssetSetter {
    
    GamePanel gp;
    public int spawnCounter = 0;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setNPC() {
        gp.npc[0] = new NPC_bartender(gp);
        gp.npc[0].hasGameJustStarted = true;
        gp.npc[0].worldX = gp.worldWidth / 2 - gp.tileSize / 2 + gp.tileSize;
        gp.npc[0].worldY = gp.worldHeight - (gp.tileSize * 8) - gp.tileSize / 2;

        gp.npc[1] = new NPC_farmer(gp);
        gp.npc[1].worldX = gp.worldWidth - gp.tileSize * 10;
        gp.npc[1].worldY = gp.worldHeight / 2 + 2 * gp.tileSize;

        gp.npc[2] = new NPC_female(gp);
        gp.npc[2].worldX = 7 * gp.tileSize;
        gp.npc[2].worldY = gp.worldHeight - (gp.tileSize * 8) - gp.tileSize / 4;

        gp.npc[3] = new NPC_female(gp);
        gp.npc[3].worldX = gp.worldWidth / 2 - 6 * gp.tileSize;
        gp.npc[3].worldY = gp.worldHeight / 2 + 2 * gp.tileSize;
    }

    public void setMonster(int numOfMonsters) {
        Timer timer = new Timer();
        Random rand = new Random(); // Create a Random object for generating random numbers
    
        timer.schedule(new TimerTask() {
            int count = 0;
            public void run() {
                if (count >= numOfMonsters) {
                    timer.cancel();
                } else {
                    // Generate a random number between 0 and 100
                    int chance = rand.nextInt(75);
    
                    // If the random number is less than 10, spawn a MON_coyote
                    if (chance < 10) {
                        int worldX = gp.worldWidth - gp.tileSize * 9;
                        int worldY = gp.worldHeight / 2 - gp.tileSize * 7;
    
                        gp.monster[count] = new MON_coyote(gp);
                        gp.monster[count].worldX = worldX;
                        gp.monster[count].worldY = worldY;
                    } else { // Else, spawn a MON_cactus
                        int worldX = gp.tileSize * 10;
                        int worldY = gp.tileSize * 10 + gp.tileSize / 2;
    
                        gp.monster[count] = new MON_cactus(gp);
                        gp.monster[count].worldX = worldX;
                        gp.monster[count].worldY = worldY;
                    }
                    count++;
                }
            }
        }, 0, 1000); // Delay of 0 ms and period of 1000 ms (1 second)
    }
}
