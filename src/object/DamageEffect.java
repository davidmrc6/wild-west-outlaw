package object;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.UtilityTool;

import entity.Entity;


public class DamageEffect {
    
    GamePanel gp;
    Entity entity;
    UtilityTool uTool = new UtilityTool();
    
    public BufferedImage image1, image2, image3, image4;
    public int frame;
    int worldX, worldY;
    int delay = 0;

    public DamageEffect(GamePanel gp, Entity entity) {

        this.entity = entity;

        this.gp = gp;

        getImage();

    }

    public void getImage() {
        try {

            image1 = ImageIO.read(getClass().getResourceAsStream("/player/dmgExplosion/explosion 1.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/player/dmgExplosion/explosion 2.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/player/dmgExplosion/explosion 3.png"));
            image4 = ImageIO.read(getClass().getResourceAsStream("/player/dmgExplosion/explosion 4.png"));

            image1 = uTool.scaleImage(image1, gp.tileSize , gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3 = uTool.scaleImage(image3, gp.tileSize , gp.tileSize);
            image4 = uTool.scaleImage(image4, gp.tileSize , gp.tileSize);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        this.worldX = entity.worldX;
        this.worldY = entity.worldY;

        delay++;
        if (delay % 5 == 0) { 
            frame++;
        }
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        switch(frame) {
            case 0: g2.drawImage(image1, screenX, screenY, null); break;
            case 1: g2.drawImage(image2, screenX, screenY, null); break;
            case 2: g2.drawImage(image3, screenX, screenY, null); break;
            case 3: g2.drawImage(image4, screenX, screenY, null); break;
        }
    }
}