package wildwestoutlaw.entity;

import wildwestoutlaw.GamePanel;

// Townsfolk chat with the player and take back the stolen coins (one per Enter), which raises the player's reputation
public class Townsperson extends Npc {

    private static final String[] THANKS = { "Much obliged!", "Thank you kindly!", "Bless you, stranger!", "That's our money!" };

    private final String[][] lines; // what they say at each reputation level

    public Townsperson(GamePanel gp, String front, String back, String left, String right, String[][] lines) {
        super(gp, front, back, left, right);
        this.lines = lines;
    }

    public static Townsperson farmer(GamePanel gp) {
        return new Townsperson(gp, "farmer front", "farmer back", "farmerNPC left", "farmerNPC right", new String[][] {
            { "Them cacti trampled my corn and ran off with my savings.",
              "Coyotes are quicker than you. Keep shootin' while you back away." },
            { "Folks are startin' to talk about you, stranger.",
              "Guess not every gunslinger is trouble." },
            { "Never thought I'd say it, but this town's lucky to have you." },
            { "Every coin's back where it belongs!",
              "Now run them critters out of town for good." }
        });
    }

    public static Townsperson hotelOwner(GamePanel gp) {
        return new Townsperson(gp, "femaleNPC front", "femaleNPC back", "femaleNPC left", "femaleNPC right", new String[][] {
            { "Don't go causing trouble in front of my hotel, stranger.",
              "Those monsters emptied every safe in town.\nBring some coins back to us townsfolk and we'll think better of you." },
            { "My guests can finally sleep again. Some of them, anyway." },
            { "There's a room upstairs with your name on it, outlaw." },
            { "Free room and board for life!",
              "Once those critters are gone, that is." }
        });
    }

    public static Townsperson neighbour(GamePanel gp) {
        return new Townsperson(gp, "femaleNPC front", "femaleNPC back", "femaleNPC left", "femaleNPC right", new String[][] {
            { "Another gunslinger... The last one ran off when he saw the cacti.",
              "When those things come close, I lock myself in. Can't blame me." },
            { "Hmm. Maybe you're not so bad after all." },
            { "You've done more for this town than the sheriff ever did." },
            { "You did it! Now finish off the rest of them, will you?" }
        });
    }

    @Override
    public String interactHint() {
        return gp.player.canGiveCoin() ? "Give coin" : "Talk";
    }

    @Override
    public void interact() {
        if (gp.player.canGiveCoin()) {
            faceThePlayer();
            gp.player.giveCoin();
            say(THANKS[random.nextInt(THANKS.length)]);
        } else {
            dialogue = lines[reputationLevel()];
            super.interact();
        }
    }
}
