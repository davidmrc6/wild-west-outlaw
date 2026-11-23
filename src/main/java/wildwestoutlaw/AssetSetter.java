package wildwestoutlaw;

import java.util.Random;

import wildwestoutlaw.entity.NPC_bartender;
import wildwestoutlaw.entity.NPC_farmer;
import wildwestoutlaw.entity.NPC_female;
import wildwestoutlaw.entity.MON_cactus;
import wildwestoutlaw.entity.MON_coyote;
import wildwestoutlaw.object.DamageEffect;
import wildwestoutlaw.object.Explosion;

public class AssetSetter {

    GamePanel gp;
    private final Random rand = new Random();

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    // Loads the monster and effect images at startup instead of on the first spawn, hit or kill
    public void preload() {
        new MON_cactus(gp);
        new MON_coyote(gp);
        new Explosion(gp, 0, 0);
        new DamageEffect(gp, gp.player);
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

    // Puts one new monster in the first free slot
    public void spawnMonster() {
        int slot = -1;
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return; // no room for more monsters
        }

        // Generate a random number between 0 and 74
        int chance = rand.nextInt(75);

        // If the random number is less than 10, spawn a MON_coyote
        if (chance < 10) {
            gp.monster[slot] = new MON_coyote(gp);
            gp.monster[slot].worldX = gp.worldWidth - gp.tileSize * 9;
            gp.monster[slot].worldY = gp.worldHeight / 2 - gp.tileSize * 7;
        } else { // Else, spawn a MON_cactus
            gp.monster[slot] = new MON_cactus(gp);
            gp.monster[slot].worldX = gp.tileSize * 10;
            gp.monster[slot].worldY = gp.tileSize * 10 + gp.tileSize / 2;
        }
    }
}
