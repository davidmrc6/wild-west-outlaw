package wildwestoutlaw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import wildwestoutlaw.entity.Entity;
import wildwestoutlaw.object.SuperObject;
import wildwestoutlaw.tile.CollisionMap;

// Toggled with F3: shows the collision map built from the buildings layer, and every hitbox
public class DebugOverlay {

    private static final Color WALL = new Color(255, 0, 0, 70);

    public static void draw(Graphics2D g2, GamePanel gp) {

        // Where the map's top-left corner is on screen
        int offsetX = gp.player.screenX - gp.player.worldX;
        int offsetY = gp.player.screenY - gp.player.worldY;

        CollisionMap map = gp.tileCollision.collisionMap;
        int cell = map.getCellSize();
        int firstCol = Math.max(0, -offsetX / cell);
        int firstRow = Math.max(0, -offsetY / cell);
        int lastCol = Math.min(map.getColumns() - 1, (gp.screenWidth - offsetX) / cell);
        int lastRow = Math.min(map.getRows() - 1, (gp.screenHeight - offsetY) / cell);

        g2.setColor(WALL);
        for (int col = firstCol; col <= lastCol; col++) {
            for (int row = firstRow; row <= lastRow; row++) {
                if (map.isSolid(col, row)) {
                    g2.fillRect(col * cell + offsetX, row * cell + offsetY, cell, cell);
                }
            }
        }

        outline(g2, gp.player.worldHitbox(), Color.GREEN, offsetX, offsetY);
        for (Entity monster : gp.monster) {
            if (monster != null) {
                outline(g2, monster.worldHitbox(), Color.RED, offsetX, offsetY);
            }
        }
        for (Entity npc : gp.npc) {
            if (npc != null) {
                outline(g2, npc.worldHitbox(), Color.CYAN, offsetX, offsetY);
            }
        }
        for (Entity projectile : gp.projectileList) {
            outline(g2, projectile.worldHitbox(), Color.YELLOW, offsetX, offsetY);
        }
        for (SuperObject object : gp.objectList) {
            Rectangle box = new Rectangle(object.worldX + object.solidArea.x, object.worldY + object.solidArea.y, object.solidArea.width, object.solidArea.height);
            outline(g2, box, Color.ORANGE, offsetX, offsetY);
        }

        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        g2.setColor(Color.white);
        g2.drawString("F3: hitboxes", 20, gp.screenHeight - 20);
    }

    private static void outline(Graphics2D g2, Rectangle box, Color color, int offsetX, int offsetY) {
        g2.setColor(color);
        g2.drawRect(box.x + offsetX, box.y + offsetY, box.width - 1, box.height - 1);
    }
}
