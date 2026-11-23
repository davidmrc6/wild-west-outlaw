package wildwestoutlaw.object;

import wildwestoutlaw.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import wildwestoutlaw.UtilityTool;

import javax.imageio.ImageIO;


public class Explosion {
    
    GamePanel gp;

    UtilityTool uTool = new UtilityTool();
    
    public BufferedImage image1, image2, image3, image4, image5, image6, image7, image8;
    public int frame;
    int worldX, worldY;
    int delay = 0;

    public Explosion(GamePanel gp, int worldX, int worldY) {

        this.gp = gp;

        this.worldX = worldX;
        this.worldY = worldY;

        getExplosionImage();

    }

    public void getExplosionImage() {
        try {

            image1 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 1.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 2.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 3.png"));
            image4 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 4.png"));
            image5 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 5.png"));
            image6 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 6.png"));
            image7 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 7.png"));
            image8 = ImageIO.read(getClass().getResourceAsStream("/player/explosion/explosion 8.png"));

            image1 = uTool.scaleImage(image1, gp.tileSize , gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3 = uTool.scaleImage(image3, gp.tileSize , gp.tileSize);
            image4 = uTool.scaleImage(image4, gp.tileSize , gp.tileSize);
            image5 = uTool.scaleImage(image5, gp.tileSize , gp.tileSize);
            image6 = uTool.scaleImage(image6, gp.tileSize, gp.tileSize);
            image7 = uTool.scaleImage(image7, gp.tileSize , gp.tileSize);
            image8 = uTool.scaleImage(image8, gp.tileSize , gp.tileSize);
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        delay++;
        if (delay % 6 == 0) { // Change frame every 3 updates
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
            case 4: g2.drawImage(image5, screenX, screenY, null); break;
            case 5: g2.drawImage(image6, screenX, screenY, null); break;
            case 6: g2.drawImage(image7, screenX, screenY, null); break;
            case 7: g2.drawImage(image8, screenX, screenY, null); break;
        }
    }

}
