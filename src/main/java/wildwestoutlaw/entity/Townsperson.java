package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

// Townsfolk take back the stolen coins, one per Enter, and the player's reputation goes up
public class Townsperson extends Npc {

    public Townsperson(GamePanel gp, String front, String back, String left, String right) {
        super(gp, front, back, left, right);
    }

    @Override
    public String interactHint() {
        return gp.player.canGiveCoin() ? "Give coin" : null;
    }

    @Override
    public void interact() {
        if (gp.player.canGiveCoin()) {
            faceThePlayer();
            gp.player.giveCoin();
        }
    }
}
