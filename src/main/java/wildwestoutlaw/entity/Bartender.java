package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

public class Bartender extends Npc {

    private boolean hasGameJustStarted = true;

    public Bartender(GamePanel gp) {

        super(gp, "barman front", "barman back", "barman L", "barman R");

        dialogue = new String[] {
            "Have you got any coins, traveler?",
            "I think giving some away would really improve your reputation in \nthis town...",
            "The cacti have stolen our money...",
            "If you need coins, you know who to take out.",
            "Here, take this gun. Press SPACE to shoot",
            "You get coins by killing the villain cacti and coyotes.",
            "Once you get some coins back, approach any of the townsfolk.",
            "If you get us back all the coins, you win by max reputation.",
            "Good luck!"
        };
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
