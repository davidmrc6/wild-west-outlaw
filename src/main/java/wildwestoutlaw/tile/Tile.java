package wildwestoutlaw.tile;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Tile {
    
    public BufferedImage image;
    public boolean collision = false;
    public Rectangle hitbox;
    public boolean visible = true; // false if the image is fully transparent

}
