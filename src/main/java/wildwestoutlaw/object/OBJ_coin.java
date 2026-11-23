package wildwestoutlaw.object;

import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import wildwestoutlaw.GamePanel;

public class OBJ_coin extends SuperObject {
    
    GamePanel gp;

    public OBJ_coin(GamePanel gp) {
        this.gp = gp;
        lifespan = 5 * gp.FPS;
        name = "coin";
        images = new BufferedImage[5];
        try {
            images[0] = ImageIO.read(getClass().getResourceAsStream("/hud/coins/coin.png"));
            images[1] = ImageIO.read(getClass().getResourceAsStream("/hud/coins/coin 2.png"));
            images[2] = ImageIO.read(getClass().getResourceAsStream("/hud/coins/coin 3.png"));
            images[3] = ImageIO.read(getClass().getResourceAsStream("/hud/coins/coin 4.png"));
            images[4] = ImageIO.read(getClass().getResourceAsStream("/hud/coins/coin 5.png"));
            images[0] = uTool.scaleImage(images[0], gp.tileSize/gp.scale, gp.tileSize/gp.scale);
            images[1] = uTool.scaleImage(images[1], gp.tileSize/gp.scale, gp.tileSize/gp.scale);
            images[2] = uTool.scaleImage(images[2], gp.tileSize/gp.scale, gp.tileSize/gp.scale);
            images[3] = uTool.scaleImage(images[3], gp.tileSize/gp.scale, gp.tileSize/gp.scale);
            images[4] = uTool.scaleImage(images[4], gp.tileSize/gp.scale, gp.tileSize/gp.scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}