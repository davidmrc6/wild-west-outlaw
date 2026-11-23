package wildwestoutlaw.entity;

public enum MonsterType {
    CACTUS("cactus", 3, 4, 1, "/mobs/cactus/death animation.png"),
    COYOTE("coyote", 5, 6, 2, null);

    final String folder;
    final int speed;
    final int life;
    final int attack;
    final String deathSprite; // null: the coyote has no death image

    MonsterType(String folder, int speed, int life, int attack, String deathSprite) {
        this.folder = folder;
        this.speed = speed;
        this.life = life;
        this.attack = attack;
        this.deathSprite = deathSprite;
    }
}
