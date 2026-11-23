package wildwestoutlaw.object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import wildwestoutlaw.GamePanel;

public class OBJ_heart extends SuperObject {
    
    GamePanel gp;
    
    public OBJ_heart(GamePanel gp) {

        this.gp = gp;

        name = "Heart";

        images = new BufferedImage[5];

        try {
            images[0] = ImageIO.read(getClass().getResourceAsStream("/hud/hearts/heart full.png"));
            images[1] = ImageIO.read(getClass().getResourceAsStream("/hud/hearts/heart 3o4.png"));
            images[2] = ImageIO.read(getClass().getResourceAsStream("/hud/hearts/heart 2o4.png"));
            images[3] = ImageIO.read(getClass().getResourceAsStream("/hud/hearts/heart 1o4.png"));
            images[4] = ImageIO.read(getClass().getResourceAsStream("/hud/hearts/heart 0o4.png"));
            images[0] = uTool.scaleImage(images[0], gp.tileSize, gp.tileSize);
            images[1] = uTool.scaleImage(images[1], gp.tileSize, gp.tileSize);
            images[2] = uTool.scaleImage(images[2], gp.tileSize, gp.tileSize);
            images[3] = uTool.scaleImage(images[3], gp.tileSize, gp.tileSize);
            images[4] = uTool.scaleImage(images[4], gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}
