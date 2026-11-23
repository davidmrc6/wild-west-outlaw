package wildwestoutlaw;

import wildwestoutlaw.entity.Entity;
import wildwestoutlaw.tile.Tile;

public class CollisionChecker {

    GamePanel gp;
    
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;
        Tile tileA, tileB;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileCollision.mapTileNum[entityRightCol][entityTopRow];
                tileA = gp.tileCollision.tile[tileNum1];
                tileB = gp.tileCollision.tile[tileNum2];	
                if ((tileA.hitbox.intersects(entity.solidArea) || (tileB.hitbox.intersects(entity.solidArea))) && (tileA.collision == true || tileB.collision == true)) {
                    entity.collisionOn = true;
                }
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileCollision.mapTileNum[entityRightCol][entityBottomRow];
                tileA = gp.tileCollision.tile[tileNum1];
                tileB = gp.tileCollision.tile[tileNum2];
                if ((tileA.hitbox.intersects(entity.solidArea) || (tileB.hitbox.intersects(entity.solidArea))) && (tileA.collision == true || tileB.collision == true)) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileCollision.mapTileNum[entityLeftCol][entityBottomRow];
                tileA = gp.tileCollision.tile[tileNum1];
                tileB = gp.tileCollision.tile[tileNum2];
                if ((tileA.hitbox.intersects(entity.solidArea) || (tileB.hitbox.intersects(entity.solidArea))) && (tileA.collision == true || tileB.collision == true)) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileCollision.mapTileNum[entityRightCol][entityBottomRow];
                tileA = gp.tileCollision.tile[tileNum1];
                tileB = gp.tileCollision.tile[tileNum2];
                if ((tileA.hitbox.intersects(entity.solidArea) || (tileB.hitbox.intersects(entity.solidArea))) && (tileA.collision == true || tileB.collision == true)) {
                    entity.collisionOn = true;
                }
                break;
            case "upleft":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityLeftCol][entityTopRow];
                tileA = gp.tileCollision.tile[tileNum1];
                if (tileA.hitbox.intersects(entity.solidArea) && tileA.collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "upright":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityRightCol][entityTopRow];
                tileA = gp.tileCollision.tile[tileNum1];
                if (tileA.hitbox.intersects(entity.solidArea) && tileA.collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "downleft":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityLeftCol][entityBottomRow];
                tileA = gp.tileCollision.tile[tileNum1];
                if (tileA.hitbox.intersects(entity.solidArea) && tileA.collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "downright":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileCollision.mapTileNum[entityRightCol][entityBottomRow];
                tileA = gp.tileCollision.tile[tileNum1];
                if (tileA.hitbox.intersects(entity.solidArea) && tileA.collision == true) {
                    entity.collisionOn = true;
            }
        }

    }

    public int checkEntityCollision(Entity entity, Entity[] target) {

        int index = 999;

        for (int i = 0; i < target.length; i++) {

            if (target[i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                    case "upleft": entity.solidArea.y -= entity.speed; entity.solidArea.x -= entity.speed; break;
                    case "upright": entity.solidArea.y -= entity.speed; entity.solidArea.x += entity.speed; break;
                    case "downleft": entity.solidArea.y += entity.speed; entity.solidArea.x -= entity.speed; break;
                    case "downright": entity.solidArea.y += entity.speed; entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;

            }
        }
        return index;
    }

    public boolean checkPlayerCollision(Entity entity) {

        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
            case "upleft": entity.solidArea.y -= entity.speed; entity.solidArea.x -= entity.speed; break;
            case "upright": entity.solidArea.y -= entity.speed; entity.solidArea.x += entity.speed; break;
            case "downleft": entity.solidArea.y += entity.speed; entity.solidArea.x -= entity.speed; break;
            case "downright": entity.solidArea.y += entity.speed; entity.solidArea.x += entity.speed; break;
        }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            if (gp.player != entity) {
                entity.collisionOn = true;
                contactPlayer = true;
            }
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

    public int checkObjectCollision(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gp.objectList.size(); i++) {

            if (gp.objectList.get(i) != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gp.objectList.get(i).solidArea.x = gp.objectList.get(i).worldX + gp.objectList.get(i).solidArea.x;
                gp.objectList.get(i).solidArea.y = gp.objectList.get(i).worldY + gp.objectList.get(i).solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                    case "upleft": entity.solidArea.y -= entity.speed; entity.solidArea.x -= entity.speed; break;
                    case "upright": entity.solidArea.y -= entity.speed; entity.solidArea.x += entity.speed; break;
                    case "downleft": entity.solidArea.y += entity.speed; entity.solidArea.x -= entity.speed; break;
                    case "downright": entity.solidArea.y += entity.speed; entity.solidArea.x += entity.speed; break;
                }
                if (entity.solidArea.intersects(gp.objectList.get(i).solidArea)) {
                    if (gp.objectList.get(i).collision == true) {
                        entity.collisionOn = true;
                    }
                    if (player == true) {
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objectList.get(i).solidArea.x = gp.objectList.get(i).solidAreaDefaultX;
                gp.objectList.get(i).solidArea.y = gp.objectList.get(i).solidAreaDefaultY;
            }
        }

        return index;
    }

}
