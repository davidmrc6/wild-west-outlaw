package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_reputation extends SuperObject {
    
    GamePanel gp;

    public OBJ_reputation(GamePanel gp) {

        this.gp = gp;
        
        name = "star";
        
        images = new BufferedImage[3];

        try {
            
            images[0] = ImageIO.read(getClass().getResourceAsStream("/hud/reputation/reputation_full.png"));
            images[1] = ImageIO.read(getClass().getResourceAsStream("/hud/reputation/reputation_half.png"));
            images[2] = ImageIO.read(getClass().getResourceAsStream("/hud/reputation/reputation_empty.png"));

            images[0] = uTool.scaleImage(images[0], gp.tileSize , gp.tileSize);
            images[1] = uTool.scaleImage(images[1], gp.tileSize, gp.tileSize);
            images[2] = uTool.scaleImage(images[2], gp.tileSize , gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
