package wildwestoutlaw;

// Sends the monsters in waves (wanted level = wave size), one monster per second
public class WaveSpawner {

    private static final int FIRST_WAVE_DELAY = 180; // frames (3 seconds)
    private static final int WAVE_DELAY = 450;       // frames after a wave is cleared
    private static final int SPAWN_INTERVAL = 60;    // frames between two monsters
    private static final int BANNER_TIME = 50;       // frames the banner stays up after a wave

    private final GamePanel gp;
    private boolean active = false;
    private int waveTimer = FIRST_WAVE_DELAY;
    private int monstersToSpawn = 0;
    private int spawnTimer = 0;

    public WaveSpawner(GamePanel gp) {
        this.gp = gp;
    }

    // Called once the bartender has finished explaining the game
    public void start() {
        active = true;
    }

    public void reset() {
        active = false;
        waveTimer = FIRST_WAVE_DELAY;
        monstersToSpawn = 0;
        spawnTimer = 0;
    }

    public void update() {
        if (active == false) {
            return;
        }

        // A wave is still coming in
        if (monstersToSpawn > 0) {
            spawnTimer--;
            if (spawnTimer <= 0) {
                gp.aSetter.spawnMonster();
                monstersToSpawn--;
                spawnTimer = SPAWN_INTERVAL;
            }
            return;
        }

        // Wait for the wave to be cleared, then count down to the next one
        if (gp.allMonstersDead()) {
            if (waveTimer <= 0) {
                monstersToSpawn = gp.player.wanted;
                spawnTimer = 0; // the first monster comes right away
                waveTimer = WAVE_DELAY;
            } else {
                waveTimer--;
                if (waveTimer == 1) {
                    gp.playSE(11); // warning just before the next wave
                }
            }
        }
    }

    // True while there is fighting to do (used for the "Kill Enemies!" banner)
    public boolean isWaveInProgress() {
        return active && (monstersToSpawn > 0 || gp.allMonstersDead() == false || waveTimer >= WAVE_DELAY - BANNER_TIME);
    }
}
