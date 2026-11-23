package wildwestoutlaw.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wildwestoutlaw.CollisionChecker;
import wildwestoutlaw.GameState;
import wildwestoutlaw.GamePanel;
import wildwestoutlaw.SoundEffect;
import wildwestoutlaw.ai.Node;

// A townsperson: strolls around near home, hides indoors from monsters, and talks when the player presses Enter
public class Npc extends Entity {

    private enum State { IDLE, WALKING, RUNNING_INSIDE, GOING_IN, INSIDE, COMING_OUT }

    private static final Direction[] LOOK_AROUND = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
    private static final String[] SCARED = { "Monsters!", "Help!", "Run!", "Eek!" };
    private static final int WANDER_RADIUS = 3;     // tiles from home
    private static final int WALK_SPEED = 1;
    private static final int RUN_SPEED = 3;
    private static final int SCARE_DISTANCE = 4;    // tiles: a monster this close sends them indoors
    private static final int NOTICE_DISTANCE = 100; // pixels: they stop and look at a player this close
    private static final int FADE_TIME = 15;        // frames to go through the door
    private static final int GIVE_UP_TIME = 30;     // frames stuck before trying another way
    private static final int BUBBLE_TIME = 100;

    protected String[] dialogue = {};
    private int line = 0;
    protected final Random random = new Random();

    private int homeX, homeY;
    private Point door; // where they stand to go inside
    private State state = State.IDLE;
    private int timer = 60; // frames left to stand around, or to stay inside
    private final List<Point> path = new ArrayList<>(); // spots to walk through, in order
    private int stuckFrames = 0;
    private int steps = 0; // frames spent walking, for the step bob
    private boolean moved = false;
    private int fade = 0;

    private String speech;
    private int speechTimer = 0;

    // Image names in /npc/, e.g. "farmer front"
    public Npc(GamePanel gp, String front, String back, String left, String right) {

        super(gp);

        solidArea = new Rectangle(18, 12, 28, 44);
        direction = Direction.DOWN;

        sprites.put(Direction.DOWN, still("/npc/" + front + ".png"));
        sprites.put(Direction.UP, still("/npc/" + back + ".png"));
        sprites.put(Direction.LEFT, still("/npc/" + left + ".png"));
        sprites.put(Direction.RIGHT, still("/npc/" + right + ".png"));
    }

    // Where they live: they wander around this spot
    public void placeAt(int x, int y) {
        worldX = homeX = x;
        worldY = homeY = y;
    }

    // The free tile in front of their door, where they go inside to hide
    public void setDoor(int col, int row) {
        door = tileSpot(col, row);
    }

    @Override
    public boolean isSolid() {
        return state != State.INSIDE;
    }

    @Override
    public void update() {

        moved = false;
        if (speechTimer > 0) {
            speechTimer--;
        }

        switch (state) {
            case INSIDE -> {
                // Come back out a little while after the wave is over
                if (!gp.spawner.isWaveInProgress() && --timer <= 0 && canMove(0, 0)) {
                    state = State.COMING_OUT;
                    direction = Direction.DOWN;
                    fade = 0;
                }
            }
            case GOING_IN -> {
                if (++fade == FADE_TIME) {
                    state = State.INSIDE;
                    timer = 30 + random.nextInt(90);
                }
            }
            case COMING_OUT -> {
                if (++fade == FADE_TIME) {
                    walkTo(homeX, homeY);
                }
            }
            case RUNNING_INSIDE -> {
                followPath(RUN_SPEED);
                if (path.isEmpty()) {
                    state = State.GOING_IN;
                    direction = Direction.UP;
                    fade = 0;
                } else if (stuckFrames > GIVE_UP_TIME && !planPath(door.x, door.y)) {
                    standAround();
                }
            }
            case IDLE, WALKING -> {
                if (door != null && monsterNearby()) {
                    say(SCARED[random.nextInt(SCARED.length)]);
                    if (planPath(door.x, door.y)) {
                        state = State.RUNNING_INSIDE;
                    }
                } else if (playerNearby()) {
                    lookAtPlayer(); // stop to look at the player (and wait to be talked to)
                } else if (state == State.WALKING) {
                    followPath(WALK_SPEED);
                    if (path.isEmpty() || stuckFrames > GIVE_UP_TIME) {
                        standAround();
                    }
                } else if (--timer <= 0) {
                    wander();
                } else if (timer % 90 == 0) {
                    direction = LOOK_AROUND[random.nextInt(LOOK_AROUND.length)];
                }
            }
        }
    }

    private void standAround() {
        path.clear();
        state = State.IDLE;
        timer = 120 + random.nextInt(240);
    }

    // Walks to a random free spot near home (or back home)
    private void wander() {
        int homeCol = tileOf(homeX + solidArea.x + solidArea.width / 2);
        int homeRow = tileOf(homeY + solidArea.y + solidArea.height / 2);
        if (random.nextInt(3) == 0) {
            walkTo(homeX, homeY);
            return;
        }
        for (int tries = 0; tries < 10; tries++) {
            int col = homeCol + random.nextInt(2 * WANDER_RADIUS + 1) - WANDER_RADIUS;
            int row = homeRow + random.nextInt(2 * WANDER_RADIUS + 1) - WANDER_RADIUS;
            if (col < 0 || row < 0 || col >= gp.maxWorldCol || row >= gp.maxWorldRow || gp.tileCollision.isTileBlocked(col, row)) {
                continue;
            }
            Point spot = tileSpot(col, row);
            // Short walks only, not all the way around a building
            if (planPath(spot.x, spot.y) && path.size() <= 2 * WANDER_RADIUS) {
                state = State.WALKING;
                return;
            }
        }
        standAround();
    }

    private void walkTo(int x, int y) {
        if (planPath(x, y)) {
            state = State.WALKING;
        } else {
            standAround();
        }
    }

    // Finds the way to (x, y) with the path finder. False if there is none.
    private boolean planPath(int x, int y) {
        path.clear();
        stuckFrames = 0;
        int startCol = tileOf(worldX + solidArea.x + solidArea.width / 2);
        int startRow = tileOf(worldY + solidArea.y + solidArea.height / 2);
        int goalCol = tileOf(x + solidArea.x + solidArea.width / 2);
        int goalRow = tileOf(y + solidArea.y + solidArea.height / 2);
        if (startCol != goalCol || startRow != goalRow) {
            gp.pathFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
            if (!gp.pathFinder.search()) {
                return false;
            }
            for (Node node : gp.pathFinder.pathList) {
                path.add(tileSpot(node.col, node.row));
            }
            path.remove(path.size() - 1); // the exact spot instead of the middle of its tile
        }
        path.add(new Point(x, y));
        return true;
    }

    // Steps towards the next spot on the path, along one axis at a time (they only have 4 directions)
    private void followPath(int speed) {
        Point next = path.get(0);
        int dx = next.x - worldX;
        int dy = next.y - worldY;
        boolean xFirst = Math.abs(dx) >= Math.abs(dy);
        if (!(xFirst ? stepX(dx, speed) || stepY(dy, speed) : stepY(dy, speed) || stepX(dx, speed))) {
            stuckFrames++;
            return;
        }
        stuckFrames = 0;
        moved = true;
        steps++;
        if (worldX == next.x && worldY == next.y) {
            path.remove(0);
        }
    }

    private boolean stepX(int dx, int speed) {
        int step = Integer.signum(dx) * Math.min(speed, Math.abs(dx));
        if (step == 0 || !canMove(step, 0)) {
            return false;
        }
        worldX += step;
        direction = step > 0 ? Direction.RIGHT : Direction.LEFT;
        return true;
    }

    private boolean stepY(int dy, int speed) {
        int step = Integer.signum(dy) * Math.min(speed, Math.abs(dy));
        if (step == 0 || !canMove(0, step)) {
            return false;
        }
        worldY += step;
        direction = step > 0 ? Direction.DOWN : Direction.UP;
        return true;
    }

    // Walls, the player, monsters and other townsfolk block the way
    private boolean canMove(int dx, int dy) {
        Rectangle box = worldHitbox();
        box.translate(dx, dy);
        return !gp.collisionChecker.hitsWall(this, dx, dy)
            && !box.intersects(gp.player.worldHitbox())
            && gp.collisionChecker.hitEntity(this, gp.npc, dx, dy) == CollisionChecker.NONE
            && gp.collisionChecker.hitEntity(this, gp.monster, dx, dy) == CollisionChecker.NONE;
    }

    private boolean monsterNearby() {
        for (Monster monster : gp.monster) {
            if (monster != null && !monster.dying && distanceTo(monster) < SCARE_DISTANCE * gp.tileSize) {
                return true;
            }
        }
        return false;
    }

    private boolean playerNearby() {
        return distanceTo(gp.player) < NOTICE_DISTANCE;
    }

    private void lookAtPlayer() {
        Rectangle me = worldHitbox();
        Rectangle player = gp.player.worldHitbox();
        double dx = player.getCenterX() - me.getCenterX();
        double dy = player.getCenterY() - me.getCenterY();
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            direction = dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    private double distanceTo(Entity other) {
        Rectangle me = worldHitbox();
        Rectangle them = other.worldHitbox();
        return Math.hypot(them.getCenterX() - me.getCenterX(), them.getCenterY() - me.getCenterY());
    }

    private int tileOf(int pixel) {
        return pixel / gp.tileSize;
    }

    // The position that puts them in the middle of a tile
    private Point tileSpot(int col, int row) {
        return new Point(col * gp.tileSize + gp.tileSize / 2 - solidArea.x - solidArea.width / 2,
                         row * gp.tileSize + gp.tileSize / 2 - solidArea.y - solidArea.height / 2);
    }

    // A short line in a speech bubble above their head
    protected void say(String text) {
        speech = text;
        speechTimer = BUBBLE_TIME;
    }

    // What the speech bubble says right now (null = no bubble)
    public String speech() {
        return speechTimer > 0 && state != State.INSIDE ? speech : null;
    }

    // 0 to 2 as the player's reputation grows, 3 once every coin is back
    protected int reputationLevel() {
        return gp.player.reputation >= gp.player.maxReputation ? 3 : gp.player.reputation * 3 / gp.player.maxReputation;
    }

    @Override
    public void draw(Graphics2D g2) {

        if (state == State.INSIDE || !isOnScreen()) {
            return;
        }

        // Fades through the door, and bobs up and down while walking (the sprites have no walking frames)
        float alpha = switch (state) {
            case GOING_IN -> 1f - (float) fade / FADE_TIME;
            case COMING_OUT -> (float) fade / FADE_TIME;
            default -> 1f;
        };
        int stepLength = state == State.RUNNING_INSIDE ? 5 : 10;
        int bob = moved && (steps / stepLength) % 2 == 1 ? -2 : 0;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawImage(currentImage(), screenX(), screenY() + bob, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // Shown above the character when the player is facing them (null = nothing to do)
    public String interactHint() {
        return dialogue.length > 0 ? "Talk" : null;
    }

    // Enter was pressed while the player is facing this character
    public void interact() {
        if (dialogue.length > 0) {
            faceThePlayer();
            line = 0;
            gp.talkingTo = this;
            showLine();
        }
    }

    // Enter was pressed during the conversation
    public void nextLine() {
        line++;
        if (line < dialogue.length) {
            showLine();
        } else {
            gp.talkingTo = null;
            gp.gameState = GameState.PLAY;
            dialogueFinished();
        }
    }

    protected void dialogueFinished() {}

    private void showLine() {
        gp.ui.currentDialogue = dialogue[line];
        gp.gameState = GameState.DIALOGUE;
        gp.playSE(SoundEffect.DIALOGUE);
    }

    protected void faceThePlayer() {
        switch (gp.player.direction) {
            case UP, UP_LEFT, UP_RIGHT -> direction = Direction.DOWN;
            case DOWN, DOWN_LEFT, DOWN_RIGHT -> direction = Direction.UP;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }
}
