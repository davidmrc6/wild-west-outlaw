package wildwestoutlaw.object;

import java.awt.image.BufferedImage;

import wildwestoutlaw.Animation;
import wildwestoutlaw.Assets;
import wildwestoutlaw.GamePanel;

public class OBJ_coin extends SuperObject {

    public OBJ_coin(GamePanel gp) {
        lifespan = 5 * gp.FPS;
        name = "coin";
        images = new BufferedImage[] {
            Assets.image("/hud/coins/coin.png", gp.tileSize),
            Assets.image("/hud/coins/coin 2.png", gp.tileSize),
            Assets.image("/hud/coins/coin 3.png", gp.tileSize),
            Assets.image("/hud/coins/coin 4.png", gp.tileSize),
            Assets.image("/hud/coins/coin 5.png", gp.tileSize)
        };
        animation = new Animation(images.length, 10, true);
    }
}
