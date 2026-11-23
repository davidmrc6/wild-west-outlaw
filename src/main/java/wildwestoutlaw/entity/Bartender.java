package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

public class Bartender extends Npc {

    private static final String[] BRIEFING = {
        "Have you got any coins, traveler?",
        "I think giving some away would really improve your reputation in \nthis town...",
        "The cacti have stolen our money...",
        "If you need coins, you know who to take out.",
        "Here, take this gun. Press SPACE or the ARROW KEYS to shoot.",
        "You get coins by killing the villain cacti and coyotes.",
        "Once you get some coins back, approach any of the townsfolk.",
        "If you get us back all the coins, you win by max reputation.",
        "Good luck!"
    };

    // What he says after the briefing, at each reputation level
    private static final String[][] LATER = {
        { "Bring the coins back to the townsfolk, partner.\nWalk up to them and press ENTER." },
        { "Word gets around fast. Folks are warming up to you." },
        { "Almost there. A few more coins and this town is yours." },
        { "That's every coin! Clear out the last of those monsters and the town is safe." }
    };

    private boolean hasGameJustStarted = true;

    public Bartender(GamePanel gp) {
        super(gp, "barman front", "barman back", "barman L", "barman R");
        dialogue = BRIEFING;
    }

    @Override
    public void interact() {
        if (!hasGameJustStarted) {
            dialogue = LATER[reputationLevel()];
        }
        super.interact();
    }

    // The monsters start coming once he has explained everything the first time
    @Override
    protected void dialogueFinished() {
        if (hasGameJustStarted) {
            gp.spawner.start();
            gp.player.wanted = 1;
            hasGameJustStarted = false;
        }
    }
}
