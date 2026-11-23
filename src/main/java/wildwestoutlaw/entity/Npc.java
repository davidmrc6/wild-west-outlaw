package wildwestoutlaw.entity;

import java.awt.Rectangle;
import java.util.Random;

import wildwestoutlaw.GameState;
import wildwestoutlaw.GamePanel;
import wildwestoutlaw.SoundEffect;

// A townsperson standing around. The player talks to them by facing them and pressing Enter.
public class Npc extends Entity {

    private static final Direction[] LOOK_AROUND = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };

    protected String[] dialogue = {};
    private int line = 0;
    private int actionLockCounter = 0;
    private final Random random = new Random();

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

    // Looks in a random direction every two seconds
    @Override
    public void setAction() {
        actionLockCounter ++;
        if (actionLockCounter == 120) {
            direction = LOOK_AROUND[random.nextInt(LOOK_AROUND.length)];
            actionLockCounter = 0;
        }
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
