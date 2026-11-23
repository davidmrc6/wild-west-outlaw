package wildwestoutlaw.entity;

import wildwestoutlaw.CollisionChecker;
import wildwestoutlaw.GamePanel;
import wildwestoutlaw.SoundEffect;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, Direction direction, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
        animation.restart();
        gp.playSE(SoundEffect.GUNSHOT);

    }

    public void update() {

        if (user == gp.player) {

            int monsterIndex = gp.collisionChecker.checkEntityCollision(this, gp.monster);
            if (monsterIndex != CollisionChecker.NONE) {
                gp.player.damageMonster(monsterIndex, attack);
                alive = false;
            }
        }

        worldX += direction.dx * speed;
        worldY += direction.dy * speed;

        // Bullets stop at buildings
        if (gp.collisionChecker.hitsWall(this, 0, 0)) {
            alive = false;
        }

        life --;
        if (life <= 0) {
            alive = false;
        }

        animation.update();
        
    }

}