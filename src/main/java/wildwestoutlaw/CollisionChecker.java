package wildwestoutlaw;

import java.awt.Rectangle;

import wildwestoutlaw.entity.Entity;
import wildwestoutlaw.object.SuperObject;

public class CollisionChecker {

    // Returned when nothing was hit
    public static final int NONE = -1;

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // True if the entity's hitbox, moved by (dx, dy), would overlap a wall
    public boolean hitsWall(Entity entity, int dx, int dy) {
        Rectangle box = entity.worldHitbox();
        box.translate(dx, dy);
        return gp.tileCollision.collisionMap.isBlocked(box);
    }

    // The first solid target the entity's hitbox, moved by (dx, dy), would overlap, or NONE
    public int hitEntity(Entity entity, Entity[] targets, int dx, int dy) {
        Rectangle box = entity.worldHitbox();
        box.translate(dx, dy);
        for (int i = 0; i < targets.length; i++) {
            if (targets[i] != null && targets[i] != entity && targets[i].isSolid() && box.intersects(targets[i].worldHitbox())) {
                return i;
            }
        }
        return NONE;
    }

    // The checks below look one step ahead in the direction the entity is moving

    public void checkTile(Entity entity) {
        if (hitsWall(entity, entity.direction.dx * entity.speed, entity.direction.dy * entity.speed)) {
            entity.collisionOn = true;
        }
    }

    public int checkEntityCollision(Entity entity, Entity[] targets) {
        int index = hitEntity(entity, targets, entity.direction.dx * entity.speed, entity.direction.dy * entity.speed);
        if (index != NONE) {
            entity.collisionOn = true;
        }
        return index;
    }

    public boolean checkPlayerCollision(Entity entity) {
        Rectangle box = entity.worldHitbox();
        box.translate(entity.direction.dx * entity.speed, entity.direction.dy * entity.speed);
        if (entity != gp.player && box.intersects(gp.player.worldHitbox())) {
            entity.collisionOn = true;
            return true;
        }
        return false;
    }

    // The object (e.g. a coin) the entity is standing on, or NONE
    public int checkObjectCollision(Entity entity) {
        Rectangle box = entity.worldHitbox();
        for (int i = 0; i < gp.objectList.size(); i++) {
            SuperObject object = gp.objectList.get(i);
            Rectangle objectBox = new Rectangle(object.worldX + object.solidArea.x, object.worldY + object.solidArea.y, object.solidArea.width, object.solidArea.height);
            if (box.intersects(objectBox)) {
                return i;
            }
        }
        return NONE;
    }
}
