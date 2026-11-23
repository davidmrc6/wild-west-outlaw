package wildwestoutlaw.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;
import wildwestoutlaw.SoundEffect;
import wildwestoutlaw.GameState;
import wildwestoutlaw.KeyHandler;
import wildwestoutlaw.CollisionChecker;

public class Player extends Entity {

    private static final int SHOT_COOLDOWN = 30; // frames between two shots
    private static final int MUZZLE_FLASH_FRAME = 1; // the bullet leaves the gun on this frame of the shooting animation
    private static final int REACH = 16; // how far in front of the player someone can be to talk to them

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int reputation;
    public int maxReputation;
    public int wanted;
    public int maxWanted;
    public int coins;
    public int maxCoins;
    public boolean invincible = false;
    public int invincibleCounter;

    public Projectile projectile;
    public int shotAvailableCounter = 0;
    public boolean isIdle = true;
    public boolean isShooting = false;
    private boolean bulletFired = false;

    // Walking frames are in Entity.sprites
    private final Map<Direction, BufferedImage[]> idleSprites = new EnumMap<>(Direction.class);
    private final Map<Direction, BufferedImage[]> shootingSprites = new EnumMap<>(Direction.class);
    private BufferedImage[] dust;

    private final Animation walkAnimation = new Animation(8, 5, true);
    private final Animation idleAnimation = new Animation(6, 5, true);
    private final Animation shootAnimation = new Animation(5, 5, false);

    public Player (GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.keyH = keyH;

        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        // The solidArea (aka the hitbox of the player) is slightly smaller than the player character
        solidArea = new Rectangle();
        solidArea.x = 16;
        solidArea.y = 24;
        solidArea.width = 31;
        solidArea.height = 42;

        setDefaultValues();
        getPlayerImage();
        setDefaultPosition();
    }

    public void setDefaultPosition() {

        worldX = gp.worldWidth / 2 - gp.tileSize / 2;
        worldY = gp.worldHeight - (gp.tileSize * 8);
    }

    public void setDefaultValues() {

        speed = 4;
        direction = Direction.DOWN;

        maxLife = 12;
        life = maxLife;

        maxReputation = 12;
        reputation = 0;

        maxWanted = 12;
        wanted = 0;

        coins = 0;
        maxCoins = 3;

        projectile = new Bullet(gp);
    }

    // Puts the player back in the state they start a new game in
    public void reset() {
        setDefaultPosition();
        direction = Direction.DOWN;
        isIdle = true;
        isShooting = false;
        idleAnimation.restart();
        life = maxLife;
        reputation = 0;
        wanted = 0;
        coins = 0;
        invincible = false;
        invincibleCounter = 0;
        shotAvailableCounter = 0;
        projectile.alive = false;
    }

    public void getPlayerImage() {

        loadDirections(sprites, "walking", "walking/Angle", 8);
        loadDirections(idleSprites, "idle", "idle", 6);

        // The first shooting frame is just the standing pose, so the animation starts at frame 2
        for (Direction d : Direction.values()) {
            BufferedImage[] frames = Assets.frames("/player/shooting/" + shootingName(d) + " %d.png", 6, gp.tileSize);
            shootingSprites.put(d, Arrays.copyOfRange(frames, 1, 6));
        }

        dust = new BufferedImage[] {
            Assets.image("/player/particles/sprint1 12x12.png"),
            Assets.image("/player/particles/sprint2 12x12.png"),
            Assets.image("/player/particles/sprint3 12x12.png")
        };
    }

    // Straight directions are in <folder>/Back, Front and Side, diagonals in <angleFolder>/Angle 1 and Angle 2
    private void loadDirections(Map<Direction, BufferedImage[]> set, String folder, String angleFolder, int frames) {
        String path = "/player/" + folder + "/";
        String anglePath = "/player/" + angleFolder + "/";
        set.put(Direction.UP, Assets.frames(path + "Back/%d.png", frames, gp.tileSize));
        set.put(Direction.DOWN, Assets.frames(path + "Front/%d.png", frames, gp.tileSize));
        set.put(Direction.LEFT, Assets.frames(path + "Side/L%d.png", frames, gp.tileSize));
        set.put(Direction.RIGHT, Assets.frames(path + "Side/R%d.png", frames, gp.tileSize));
        set.put(Direction.DOWN_LEFT, Assets.frames(anglePath + "Angle 1/DL%d.png", frames, gp.tileSize));
        set.put(Direction.DOWN_RIGHT, Assets.frames(anglePath + "Angle 1/DR%d.png", frames, gp.tileSize));
        set.put(Direction.UP_LEFT, Assets.frames(anglePath + "Angle 2/UL%d.png", frames, gp.tileSize));
        set.put(Direction.UP_RIGHT, Assets.frames(anglePath + "Angle 2/UR%d.png", frames, gp.tileSize));
    }

    private static String shootingName(Direction d) {
        return switch (d) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
            case UP_LEFT -> "upleft";
            case UP_RIGHT -> "upright";
            case DOWN_LEFT -> "downleft";
            case DOWN_RIGHT -> "downright";
        };
    }

    // Where the end of the barrel is in each shooting sprite (in pixels of the 32x32 artwork)
    private static Point gunTip(Direction d) {
        return switch (d) {
            case RIGHT -> new Point(31, 15);
            case LEFT -> new Point(0, 15);
            case UP -> new Point(23, 0);
            case DOWN -> new Point(10, 30);
            case UP_RIGHT -> new Point(30, 4);
            case UP_LEFT -> new Point(1, 4);
            case DOWN_RIGHT -> new Point(25, 30);
            case DOWN_LEFT -> new Point(6, 30);
        };
    }

    @Override
    public void update() {

        if (keyH.leftPressed == true || keyH.rightPressed == true ||
                keyH.upPressed == true || keyH.downPressed == true) {

            if (keyH.upPressed == true && keyH.rightPressed == true) {
                direction = Direction.UP_RIGHT;
            } else if (keyH.upPressed == true && keyH.leftPressed == true) {
                direction = Direction.UP_LEFT;
            } else if (keyH.downPressed == true && keyH.rightPressed == true) {
                direction = Direction.DOWN_RIGHT;
            } else if (keyH.downPressed == true && keyH.leftPressed == true) {
                direction = Direction.DOWN_LEFT;
            } else if (keyH.downPressed == true) {
                direction = Direction.DOWN;
            } else if (keyH.rightPressed == true) {
                direction = Direction.RIGHT;
            } else if (keyH.leftPressed == true) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.UP;
            }

            if (isIdle) {
                isIdle = false;
                walkAnimation.restart();
            }

            // Diagonal steps are shorter, so walking diagonally isn't faster than walking straight
            int step = (direction.dx != 0 && direction.dy != 0) ? (int) Math.round(speed / Math.sqrt(2)) : speed;
            int dx = direction.dx * step;
            int dy = direction.dy * step;

            // Check monster collision
            int monsterIndex = gp.collisionChecker.hitEntity(this, gp.monster, dx, dy);
            contactMonster(monsterIndex);

            // Move along x and y separately, so walking diagonally into a wall slides along it
            if (canMove(dx, 0)) {
                worldX += dx;
            }
            if (canMove(0, dy)) {
                worldY += dy;
            }

            walkAnimation.update();
        }
        else {
            if (!isIdle) {
                isIdle = true;
                idleAnimation.restart();
            }
            idleAnimation.update();
        }

        // Check object collision (also when standing still)
        int objIndex = gp.collisionChecker.checkObjectCollision(this);
        pickUpObject(objIndex);

        updateShooting();

        if (invincible == true) {
            invincibleCounter ++;
            if (invincibleCounter == 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (life <= 0) {
            gp.gameState = GameState.GAME_OVER;
        }
    }

    private void updateShooting() {

        if (isShooting) {
            shootAnimation.update();
            if (!bulletFired && shootAnimation.frame() >= MUZZLE_FLASH_FRAME) {
                fireBullet();
            }
            if (shootAnimation.isFinished()) {
                isShooting = false;
            }
        }

        if (keyH.spacePressed == true && !isShooting && projectile.alive == false && shotAvailableCounter == SHOT_COOLDOWN) {
            isShooting = true;
            bulletFired = false;
            shootAnimation.restart();
            shotAvailableCounter = 0;
        }
        if (shotAvailableCounter < SHOT_COOLDOWN) {
            shotAvailableCounter++;
        }
    }

    // Starts the bullet at the end of the barrel (the bullet sprite is a whole tile with the bullet in the middle)
    private void fireBullet() {
        Point tip = gunTip(direction);
        int x = worldX + tip.x * gp.scale - gp.tileSize / 2;
        int y = worldY + tip.y * gp.scale - gp.tileSize / 2;
        projectile.set(x, y, direction, true, this);
        gp.projectileList.add(projectile);
        bulletFired = true;
    }

    @Override
    public void draw(Graphics2D g2) {

        BufferedImage image;

        if (isShooting) {
            image = shootingSprites.get(direction)[shootAnimation.frame()];
        } else if (isIdle) {
            image = idleSprites.get(direction)[idleAnimation.frame()];
        } else {
            image = sprites.get(direction)[walkAnimation.frame()];
            drawDust(g2, walkAnimation.frame());
        }

        // Blinks for a moment after being hit
        if (invincible == true && (invincibleCounter / 3) % 2 == 0) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // A little dust cloud behind the boots on every other step (none when walking towards the camera)
    private void drawDust(Graphics2D g2, int frame) {

        if (frame > 4 || frame % 2 != 0 || direction == Direction.DOWN) {
            return;
        }

        int x = switch (direction) {
            case RIGHT, UP_RIGHT, DOWN_RIGHT -> screenX - 3;
            case UP -> screenX + 10;
            default -> screenX + 22;
        };
        int y = switch (direction) {
            case LEFT, RIGHT -> screenY + 22;
            case UP, UP_LEFT, UP_RIGHT -> screenY + 28;
            default -> screenY + 18;
        };

        g2.drawImage(dust[frame / 2], x, y, 12, 12, null);
    }

    // Walls, townsfolk and monsters block the way
    private boolean canMove(int dx, int dy) {
        return (dx != 0 || dy != 0)
            && !gp.collisionChecker.hitsWall(this, dx, dy)
            && gp.collisionChecker.hitEntity(this, gp.npc, dx, dy) == CollisionChecker.NONE
            && gp.collisionChecker.hitEntity(this, gp.monster, dx, dy) == CollisionChecker.NONE;
    }

    // Enter was pressed: talk to (or give a coin to) whoever the player is facing
    public void interact() {
        Npc npc = npcInFront();
        if (npc != null) {
            npc.interact();
        }
    }

    // The character right in front of the player, or null
    public Npc npcInFront() {
        Rectangle reach = worldHitbox();
        reach.translate(direction.dx * REACH, direction.dy * REACH);
        for (Npc npc : gp.npc) {
            if (npc != null && reach.intersects(npc.worldHitbox())) {
                return npc;
            }
        }
        return null;
    }

    // Coins only buy reputation until it is maxed out, so it can't overshoot the win condition
    public boolean canGiveCoin() {
        return coins > 0 && reputation < maxReputation;
    }

    public void giveCoin() {
        gp.playSE(SoundEffect.COIN_GIVEN);
        coins --;
        reputation += 1;
    }

    public void pickUpObject(int i) {

        if (i != CollisionChecker.NONE) {

            if (coins < maxCoins) {
                gp.playSE(SoundEffect.COIN_PICKUP);
                coins ++ ;
                gp.objectList.remove(i);
            }
        }
    }

    public void contactMonster(int i) {

        if (i != CollisionChecker.NONE) {
            takeDamage(gp.monster[i].attack);
        }

    }

    // Used both when the player walks into a monster and when a monster walks into the player
    public void takeDamage(int damage) {

        if (invincible == false) {
            life -= damage;
            invincible = true;
            gp.playSE(SoundEffect.PLAYER_HURT);
        }
    }

    public void damageMonster(int i, int attack) {

        if (i != CollisionChecker.NONE) {

            gp.monster[i].life -= attack;

            if (gp.monster[i].life > 0) {
                gp.monster[i].damaged = true;
            }

            if (gp.monster[i].life <= 0){
                gp.monster[i].startDying();
            }

        }

    }

}
