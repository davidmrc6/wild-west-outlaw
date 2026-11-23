package wildwestoutlaw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wildwestoutlaw.entity.Bartender;
import wildwestoutlaw.entity.Monster;
import wildwestoutlaw.entity.MonsterType;
import wildwestoutlaw.entity.Townsperson;
import wildwestoutlaw.object.Coin;
import wildwestoutlaw.object.DamageEffect;
import wildwestoutlaw.object.Explosion;

public class AssetSetter {

    // Open spots around the edge of town where monsters appear (column, row in tiles)
    private static final int[][] SPAWN_POINTS = {
        {10, 11}, {39, 11}, {16, 6}, {34, 5}, {28, 12}, {6, 13},
        {41, 13}, {6, 22}, {41, 25}, {9, 30}, {40, 30}
    };
    private static final int MIN_SPAWN_DISTANCE = 10; // tiles away from the player, when possible
    private static final int COYOTE_CHANCE = 30; // percent of the monsters that are coyotes

    GamePanel gp;
    private final Random rand = new Random();

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    // Loads the monster and effect images at startup instead of on the first spawn, hit or kill
    public void preload() {
        new Monster(gp, MonsterType.CACTUS);
        new Monster(gp, MonsterType.COYOTE);
        new Explosion(gp, 0, 0);
        new DamageEffect(gp, gp.player);
        new Coin(gp);
    }

    public void setNPC() {
        gp.npc[0] = new Bartender(gp);
        gp.npc[0].worldX = gp.worldWidth / 2 - gp.tileSize / 2 + gp.tileSize;
        gp.npc[0].worldY = gp.worldHeight - (gp.tileSize * 8) - gp.tileSize / 2;

        gp.npc[1] = new Townsperson(gp, "farmer front", "farmer back", "farmerNPC left", "farmerNPC right");
        gp.npc[1].worldX = gp.worldWidth - gp.tileSize * 10;
        gp.npc[1].worldY = gp.worldHeight / 2 + 2 * gp.tileSize;

        gp.npc[2] = new Townsperson(gp, "femaleNPC front", "femaleNPC back", "femaleNPC left", "femaleNPC right");
        gp.npc[2].worldX = 7 * gp.tileSize;
        gp.npc[2].worldY = gp.worldHeight - (gp.tileSize * 8) - gp.tileSize / 4;

        gp.npc[3] = new Townsperson(gp, "femaleNPC front", "femaleNPC back", "femaleNPC left", "femaleNPC right");
        gp.npc[3].worldX = gp.worldWidth / 2 - 6 * gp.tileSize;
        gp.npc[3].worldY = gp.worldHeight / 2 + 2 * gp.tileSize;
    }

    // Puts one new monster in the first free slot. Returns false if there is no room right now.
    public boolean spawnMonster() {
        int slot = -1;
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] == null) {
                slot = i;
                break;
            }
        }
        int[] point = pickSpawnPoint();
        if (slot == -1 || point == null) {
            return false;
        }

        MonsterType type = rand.nextInt(100) < COYOTE_CHANCE ? MonsterType.COYOTE : MonsterType.CACTUS;

        Monster monster = new Monster(gp, type);
        monster.worldX = point[0] * gp.tileSize;
        monster.worldY = point[1] * gp.tileSize;
        gp.monster[slot] = monster;
        return true;
    }

    // A random free spawn point far from the player, or the furthest free one if none is far enough
    private int[] pickSpawnPoint() {
        List<int[]> farEnough = new ArrayList<>();
        int[] furthest = null;
        double furthestDistance = -1;

        for (int[] point : SPAWN_POINTS) {
            int x = point[0] * gp.tileSize;
            int y = point[1] * gp.tileSize;
            if (isOccupied(x, y)) {
                continue;
            }
            double distance = Math.hypot(x - gp.player.worldX, y - gp.player.worldY);
            if (distance >= MIN_SPAWN_DISTANCE * gp.tileSize) {
                farEnough.add(point);
            }
            if (distance > furthestDistance) {
                furthestDistance = distance;
                furthest = point;
            }
        }
        return farEnough.isEmpty() ? furthest : farEnough.get(rand.nextInt(farEnough.size()));
    }

    // True if a monster is still standing on (or right next to) this spot
    private boolean isOccupied(int x, int y) {
        for (Monster monster : gp.monster) {
            if (monster != null && Math.abs(monster.worldX - x) < gp.tileSize * 3 / 2 && Math.abs(monster.worldY - y) < gp.tileSize * 3 / 2) {
                return true;
            }
        }
        return false;
    }
}
