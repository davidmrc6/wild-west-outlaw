package wildwestoutlaw;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import wildwestoutlaw.ai.PathFinder;
import wildwestoutlaw.entity.Entity;
import wildwestoutlaw.entity.Player;
import wildwestoutlaw.object.DamageEffect;
import wildwestoutlaw.object.Explosion;
import wildwestoutlaw.object.OBJ_coin;
import wildwestoutlaw.object.SuperObject;
import wildwestoutlaw.tile.TileManager;

public class GamePanel extends Main implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 32; 
    public final int scale = 2;

    public final int tileSize = originalTileSize * scale; 

    // WORLD SETTINGS
    public final int maxWorldCol = 48;
    public final int maxWorldRow = 36;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FOR FULL SCREEN
    public int screenWidth = Main.screenWidth;
    public int screenHeight = Main.screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // FPS
    public int FPS = 60;

    AssetSetter aSetter = new AssetSetter(this);
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public Sound sound = new Sound();

    int worldX, worldY;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[25];
    public UI ui = new UI(this);
    public PathFinder pathFinder = new PathFinder(this);
    public boolean monsterSpawning;
    public int spawnTimer = 180;
    public boolean hasPlayerSeenTutorial = false;

    // Game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int gameOverState = 4;
    public final int gameWonState = 5;
    public final int tutorialState = 6;

    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<Entity>();
    private ArrayList<Explosion> explosionList = new ArrayList<>();
    private ArrayList<DamageEffect> damageEffectList = new ArrayList<>();
    public ArrayList<SuperObject> objectList = new ArrayList<>();

    public TileManager tileCollision;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        tileCollision = new TileManager(this, "/tile/hitboxes.txt");
        setupGame();
    }

    public void setupGame() {
        aSetter.setNPC();
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        gameState = titleState;

        //playMusic(0);
    }

    public void restart() {

        player.setDefaultPosition();
        player.restoreLife();
        aSetter.setNPC();
        clearMonsters();
        monsterSpawning = false;
        spawnTimer = 180;
        ui.hasGameOverSoundPlayed = false;
        ui.hasGameOverSoundPlayed = false;
        stopMusic();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread!= null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                drawToTempScreen(); //draw to BufferedImage
                drawToScreen(); //draw BufferedImage to Screen
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                //System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
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

    public void spawnMonsters() {
        if (monsterSpawning == true) {
            if (allMonstersDead() == true) {
                if (spawnTimer <= 0) {
                    aSetter.setMonster((int)(player.wanted));
                    spawnTimer = 450; // Reset the timer to the initial value
                } else {
                    spawnTimer--;
                    if (spawnTimer == 1) {
                        playSE(11);
                    }
                }
            }
        }
    }

    public void update() {

        if (player.reputation == player.maxReputation && allMonstersDead() == true) {
            gameState = gameWonState;
        }

        if (gameState == playState) {

            player.update();
            spawnMonsters();

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {

                    worldX = monster[i].worldX;
                    worldY = monster[i].worldY;

                    if (monster[i].alive == true && monster[i].dying == false) {
                        monster[i].update();
                    }
                    if (monster[i].alive == true && monster[i].damaged == true) {
                        damageEffectList.add(new DamageEffect(this, monster[i]));
                        playSE(7);
                        monster[i].damaged = false;
                    }
                    if (monster[i].alive == false) {
                        if (player.wanted <= player.maxWanted)
                            player.wanted ++;
                        monster[i] = null;
                        explosionList.add(new Explosion(this, worldX, worldY));
                        playSE(3);

                        OBJ_coin newCoin = new OBJ_coin(this);
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

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if (projectileList.get(i).alive == false) {
                        projectileList.remove(i);
                    }
                }
            }

            List<SuperObject> toRemove = new ArrayList<>();
            for (SuperObject object : objectList) {
                object.updateFrame();
                if (object instanceof OBJ_coin) {
                    OBJ_coin coin = (OBJ_coin) object;
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
                if (explosion.frame >= 8) {
                    iterator.remove();
                }
            }
            Iterator<DamageEffect> iterator1 = damageEffectList.iterator();
            while (iterator1.hasNext()) {
                DamageEffect damageEffect = iterator1.next();
                damageEffect.update();
                if (damageEffect.frame >= 4) {
                    iterator1.remove();
                }
            }
        }

        if (gameState == pauseState) {
        }
    }

    public void drawToTempScreen() {

        super.paintComponent(g2);

        // TITLE SCREEN
        if (gameState == titleState)  {
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

            // UI
            ui.draw(g2);
        }

    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth, screenHeight, null);
        g.dispose();
    }

    public void playMusic(int i) {

        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSE(int i) {
        
        sound.setFile(i);
        sound.play();
    }
}