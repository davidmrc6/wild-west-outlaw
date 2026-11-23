package wildwestoutlaw.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import wildwestoutlaw.entity.Projectile;
import wildwestoutlaw.GamePanel;
import wildwestoutlaw.UtilityTool;

public class OBJ_bullet extends Projectile {

    GamePanel gp;
    UtilityTool uTool = new UtilityTool();

    public OBJ_bullet(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Bullet";
        speed = 10;
        maxLife = 50;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;

        getImage();
    }
    
    public void getImage() {
        try {

            right1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/LR 1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/LR 2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/LR 1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/LR 2.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/Up 1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/Up 2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/Dn 1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/Dn 2.png"));
            upleft1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/UpL 1.png"));
            upleft2 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/UpL 2.png"));
            upright1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/UpR 1.png"));
            upright2 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/UpR 2.png"));
            downleft1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/DnL 1.png"));
            downleft2 =  ImageIO.read(getClass().getResourceAsStream("/player/projectiles/DnL 2.png"));
            downright1 = ImageIO.read(getClass().getResourceAsStream("/player/projectiles/DnR 1.png"));
            downright2 =  ImageIO.read(getClass().getResourceAsStream("/player/projectiles/DnR 2.png"));

            right1 = uTool.scaleImage(right1, 32, 32);
            right2 = uTool.scaleImage(right2, 32, 32);
            left1 = uTool.scaleImage(left1, 32, 32);
            left2 = uTool.scaleImage(left2, 32, 32);
            up1 = uTool.scaleImage(up1, 32, 32);
            up2 = uTool.scaleImage(up2, 32, 32);
            down1 = uTool.scaleImage(down1, 32, 32);
            down2 = uTool.scaleImage(down2, 32, 32);
            upleft1 = uTool.scaleImage(upleft1, 32, 32);
            upleft2 = uTool.scaleImage(upleft2, 32, 32);
            upright1 = uTool.scaleImage(upright1, 32, 32);
            upright2 = uTool.scaleImage(upright2, 32, 32);
            downleft1 = uTool.scaleImage(downleft1, 32, 32);
            downleft2 = uTool.scaleImage(downleft2, 32, 32);
            downright1 = uTool.scaleImage(downright1, 32, 32);
            downright2 = uTool.scaleImage(downright2, 32, 32);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

