package wildwestoutlaw;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import wildwestoutlaw.ai.PathFinder;
import wildwestoutlaw.entity.Entity;
import wildwestoutlaw.entity.Monster;
import wildwestoutlaw.entity.Npc;
import wildwestoutlaw.entity.Player;
import wildwestoutlaw.object.DamageEffect;
import wildwestoutlaw.object.Explosion;
import wildwestoutlaw.object.Coin;
import wildwestoutlaw.object.SuperObject;
import wildwestoutlaw.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 32;
    public final int scale = 2;

    public final int tileSize = originalTileSize * scale;

    // WORLD SETTINGS
    public final int maxWorldCol = 48;
    public final int maxWorldRow = 36;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // The game is always drawn at this size, then scaled to fit the window
    public final int screenWidth = 1920;
    public final int screenHeight = 1080;
    BufferedImage tempScreen;
    Graphics2D g2;
    // tempScreen is drawn by the game thread and shown by Swing's, so both use this lock
    private final Object frameLock = new Object();
    static final Color SAND = Color.decode("#eeca84"); // colour around the edges of the map

    // FPS
    public final int FPS = 60;

    AssetSetter aSetter = new AssetSetter(this);
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public Sound sound = new Sound();

    int worldX, worldY;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public Npc npc[] = new Npc[10];
    public Monster monster[] = new Monster[25];
    public Npc talkingTo; // who the player is in a conversation with
    public UI ui = new UI(this);
    public PathFinder pathFinder = new PathFinder(this);
    public WaveSpawner spawner = new WaveSpawner(this);
    public boolean hasPlayerSeenTutorial = false;
    public boolean showHitboxes = false; // F3

    // Game state
    public GameState gameState;

    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<Entity>();
    private ArrayList<Explosion> explosionList = new ArrayList<>();
    private ArrayList<DamageEffect> damageEffectList = new ArrayList<>();
    public ArrayList<SuperObject> objectList = new ArrayList<>();

    public TileManager tileCollision;

    public GamePanel() {
        this.setPreferredSize(new Dimension(1280, 720)); // window size when it isn't maximized
        this.setBackground(Color.black); // bars around the game when the window isn't 16:9
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                keyH.focusLost();
            }
        });
        this.setFocusable(true);
        tileCollision = new TileManager(this);
        setupGame();
    }

    public void setupGame() {
        aSetter.preload();
        aSetter.setNPC();
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        g2 = tempScreen.createGraphics();
        gameState = GameState.TITLE;
    }

    public void restart() {

        player.reset();
        spawner.reset();
        aSetter.setNPC();
        clearMonsters();
        projectileList.clear();
        objectList.clear();
        explosionList.clear();
        damageEffectList.clear();
        talkingTo = null;
        ui.commandNum = 0;
        ui.hasGameOverSoundPlayed = false;
        ui.hasGameWonSoundPlayed = false;

        // "Retry" starts the music over, going back to the title screen stops it
        if (gameState == GameState.PLAY) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this, "game loop");
        gameThread.start();
    }

    @Override
    public void run() {

        long frameTime = 1_000_000_000L / FPS; // 0.01666 seconds
        long nextFrame = System.nanoTime();

        while (gameThread != null) {

            update();
            synchronized (frameLock) {
                drawToTempScreen(); // draw to BufferedImage
            }
            repaint(); // ask Swing to show the BufferedImage (see paintComponent)

            // Sleep until the next frame is due
            nextFrame += frameTime;
            long sleepTime = nextFrame - System.nanoTime();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else if (sleepTime < -5 * frameTime) {
                // Fell far behind: carry on from now instead of catching up
                nextFrame = System.nanoTime();
            }
        }
    }

    public void clearMonsters() {
       for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null) {
                monster[i] = null;
            }
        }
    }

    public boolean allMonstersDead() {
        for (int i = 0; i < monster.length; i++) {
            if (monster[i] != null && monster[i].alive) {
                return false;
            }
        }
        return true;
    }

    public void update() {

        keyH.processInput();

        if (player.reputation >= player.maxReputation && allMonstersDead()) {
            gameState = GameState.GAME_WON;
        }

        if (gameState == GameState.PLAY) {

            player.update();
            spawner.update();

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {

                    worldX = monster[i].worldX;
                    worldY = monster[i].worldY;

                    // Also counts down a killed monster's death, even if it is off-screen
                    if (monster[i].alive == true) {
                        monster[i].update();
                    }
                    if (monster[i].alive == true && monster[i].damaged == true) {
                        damageEffectList.add(new DamageEffect(this, monster[i]));
                        playSE(SoundEffect.MONSTER_HIT);
                        monster[i].damaged = false;
                    }
                    if (monster[i].alive == false) {
                        if (player.wanted < player.maxWanted)
                            player.wanted ++;
                        monster[i] = null;
                        explosionList.add(new Explosion(this, worldX, worldY));
                        playSE(SoundEffect.MONSTER_DEATH);

                        Coin newCoin = new Coin(this);
                        newCoin.worldX = worldX;
                        newCoin.worldY = worldY;

                        objectList.add(newCoin);

                    }
                }
            }

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            for (Entity projectile : projectileList) {
                if (projectile.alive == true) {
                    projectile.update();
                }
            }
            projectileList.removeIf(projectile -> projectile.alive == false);

            List<SuperObject> toRemove = new ArrayList<>();
            for (SuperObject object : objectList) {
                object.updateFrame();
                if (object instanceof Coin) {
                    Coin coin = (Coin) object;
                    coin.lifespan--;
                    if (coin.lifespan <= 0) {
                        toRemove.add(coin);
                    }
                }
            }
            objectList.removeAll(toRemove);

            Iterator<Explosion> iterator = explosionList.iterator();
            while (iterator.hasNext()) {
                Explosion explosion = iterator.next();
                explosion.update();
                if (explosion.isFinished()) {
                    iterator.remove();
                }
            }
            Iterator<DamageEffect> iterator1 = damageEffectList.iterator();
            while (iterator1.hasNext()) {
                DamageEffect damageEffect = iterator1.next();
                damageEffect.update();
                if (damageEffect.isFinished()) {
                    iterator1.remove();
                }
            }
        }
    }

    public void drawToTempScreen() {

        g2.setColor(SAND);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // TITLE SCREEN
        if (gameState == GameState.TITLE)  {
            ui.draw(g2);
        }

        else {
            tileCollision.draw(g2);

            for (SuperObject object : objectList) {
                object.draw(g2, this);
            }

            // Add entities to entityList
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null)
                    entityList.add(monster[i]);
            }

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null)
                    entityList.add(projectileList.get(i));
            }

            // Draw entities
            for (int i = 0; i < entityList.size(); i++)
                entityList.get(i).draw(g2);

            // Empty entity list
            entityList.clear();

            for (DamageEffect damageEffect : damageEffectList) {
                damageEffect.draw(g2);
            }

            for (Explosion explosion : explosionList) {
                explosion.draw(g2);
            }


            // PLAYER
            player.draw(g2);

            if (showHitboxes) {
                DebugOverlay.draw(g2, this);
            }

            // UI
            ui.draw(g2);
        }

    }

    // Shows the last finished frame, scaled to fit the window (black bars fill the rest)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double scaleFactor = Math.min((double) getWidth() / screenWidth, (double) getHeight() / screenHeight);
        int width = (int) Math.round(screenWidth * scaleFactor);
        int height = (int) Math.round(screenHeight * scaleFactor);
        int x = (getWidth() - width) / 2;
        int y = (getHeight() - height) / 2;

        // Sharp pixels when scaling up, smooth when scaling down
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, scaleFactor >= 1
                ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                : RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        synchronized (frameLock) {
            g2d.drawImage(tempScreen, x, y, width, height, null);
        }
        Toolkit.getDefaultToolkit().sync(); // smoother animation on Linux
    }

    public void playMusic() {
        sound.playMusic();
    }

    public void stopMusic() {
        sound.stopMusic();
    }

    public void playSE(SoundEffect effect) {
        sound.play(effect);
    }
}
