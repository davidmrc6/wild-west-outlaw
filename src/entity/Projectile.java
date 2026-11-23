package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
        gp.playSE(9);

    }

    public void update() {

        if (user == gp.player) {

            //collision checker between projectile and structures - is buggy because the hitboxes are buggy
            // gp.collisionChecker.checkTile(this);
            // if (collisionOn == true) {
            //     alive = false;
            //     collisionOn = false;
            // }

            int monsterIndex = gp.collisionChecker.checkEntityCollision(this, gp.monster);
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack);
                alive = false;
            }
        }
        if (user != gp.player) {

        }

        switch (direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
            case "upright": worldY -= speed; worldX += speed; break;
            case "upleft": worldY -= speed; worldX -= speed; break;
            case "downright": worldY += speed; worldX += speed; break;
            case "downleft": worldY += speed; worldX -= speed; break;
        }

        life --;
        if (life <= 0) {
            alive = false;
        }

        spriteCounter ++;
        if (spriteCounter > 12) {
            if (spriteNumber == 1)
                spriteNumber = 2;
            else if (spriteNumber == 2)
                spriteNumber = 1;
            spriteCounter = 0;
        }
        
    }

}