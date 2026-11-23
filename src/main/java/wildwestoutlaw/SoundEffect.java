package wildwestoutlaw;

public enum SoundEffect {
    COIN_PICKUP("coin get"),
    COIN_GIVEN("coin given"),
    MONSTER_DEATH("death explosion"),
    DIALOGUE("dialogue"),
    GAME_OVER("game over"),
    GAME_WON("game won"),
    MONSTER_HIT("hit explosion"),
    PLAYER_HURT("player damaged"),
    GUNSHOT("player shot"),
    WAVE_WARNING("kill monsters"),
    MENU_MOVE("menu button"),
    MENU_SELECT("choose button");

    final String path;

    SoundEffect(String fileName) {
        this.path = "/sound/" + fileName + ".wav";
    }
}
