package wildwestoutlaw.tile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import wildwestoutlaw.GamePanel;
import wildwestoutlaw.UtilityTool;

public class TileManager {
    
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    BufferedImage backgroundImage;


    public TileManager(GamePanel gp, String path) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap(path);

    }

    public void getTileImage() {
        setup (0, "empty_tile", false, 0, 0, 32, 32);
        setup (1, "empty_tile", true, 0, 0, 32, 32);
        try {
            BufferedImage hitboxes = ImageIO.read(getClass().getResourceAsStream("/environment/MAIN.png"));
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/environment/Background.png"));
            Image scaledBackground = originalImage.getScaledInstance(gp.worldWidth, gp.worldHeight, Image.SCALE_DEFAULT);
            Image scaledHitboxes = hitboxes.getScaledInstance(gp.worldWidth, gp.worldHeight, Image.SCALE_DEFAULT);
            backgroundImage = new BufferedImage(gp.worldWidth, gp.worldHeight, BufferedImage.TYPE_INT_ARGB);
            // try {
            // writeHitboxes("/tile/hitboxes.txt", hitboxes);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }        
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.drawImage(scaledBackground, 0, 0, null);
            g2d.drawImage(scaledHitboxes, 0, 0, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup (int index, String imageName, boolean collision, int x, int y, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/environment/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
            tile[index].hitbox = new Rectangle(x, y, width, height);            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader (new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        // Draw the background image
        drawBackgroundImage(g2);

        // Draw the tiles
        int playerWorldX = gp.player.worldX;
        int playerWorldY = gp.player.worldY;
        int playerScreenX = gp.player.screenX;
        int playerScreenY = gp.player.screenY;
        int tileSize = gp.tileSize;
        int maxWorldCol = gp.maxWorldCol;
        int maxWorldRow = gp.maxWorldRow;
        Tile[] tiles = tile;
        int[][] mapTileNums = mapTileNum;
        for (int worldRow = 0; worldRow < maxWorldRow; worldRow++) {
            for (int worldCol = 0; worldCol < maxWorldCol; worldCol++) {
                int tileNum = mapTileNums[worldCol][worldRow];
                int worldX = worldCol * tileSize;
                int worldY = worldRow * tileSize;
                int screenX = worldX - playerWorldX + playerScreenX;
                int screenY = worldY - playerWorldY + playerScreenY;

                //only draw screensize
                if (worldX + tileSize > playerWorldX - playerScreenX && worldX - tileSize < playerWorldX + playerScreenX
                        && worldY + tileSize > playerWorldY - playerScreenY && worldY - tileSize < playerWorldY + playerScreenY) {
                    g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
                }
            }
        }
    }

    public void drawBackgroundImage(Graphics2D g2) {
        // Calculate the position of the background image
        int bgX = gp.player.screenX - gp.player.worldX;
        int bgY = gp.player.screenY - gp.player.worldY;

        // Calculate the visible area of the screen
        int visibleWidth = Math.max(gp.getWidth(), backgroundImage.getWidth() - bgX);
        int visibleHeight = Math.max(gp.getHeight(), backgroundImage.getHeight() - bgY);
        
        Rectangle visibleRect = new Rectangle(visibleWidth, visibleHeight);
        visibleRect.translate(gp.player.screenX - visibleWidth / 2, gp.player.screenY - visibleHeight / 2);

        // Calculate the intersection of the visible area and the background image
        Rectangle bgRect = new Rectangle(bgX, bgY, backgroundImage.getWidth(), backgroundImage.getHeight());
        Rectangle intersection = visibleRect.intersection(bgRect);

        // Draw the visible portion of the background image
        if (!intersection.isEmpty()) {
            BufferedImage visibleImage = backgroundImage.getSubimage(intersection.x - bgX, intersection.y - bgY, intersection.width, intersection.height);
            g2.drawImage(visibleImage, intersection.x, intersection.y, null);
        }
    }

    // private static final int TILE_SIZE = 32;

    // public static void writeHitboxes(String filename, BufferedImage image) throws IOException {
    //     int width = image.getWidth();
    //     int height = image.getHeight();
    //     int rows = height / TILE_SIZE;
    //     int cols = width / TILE_SIZE;

    //     try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
    //         for (int row = 0; row < rows; row++) {
    //             for (int col = 0; col < cols; col++) {
    //                 boolean hasPixel = false;
    //                 for (int y = 0; y < TILE_SIZE; y++) {
    //                     for (int x = 0; x < TILE_SIZE; x++) {
    //                         int pixel = image.getRGB(col * TILE_SIZE + x, row * TILE_SIZE + y);
    //                         if ((pixel & 0xff000000) != 0) {
    //                             hasPixel = true;
    //                             break;
    //                         }
    //                     }
    //                     if (hasPixel) {
    //                         break;
    //                     }
    //                 }
    //                 if (col != cols-1)
    //                 writer.write(hasPixel ? "1 " : "0 ");
    //                 else {
    //                 writer.write(hasPixel ? "1" : "0");
    //                 }
    //             }
    //             writer.newLine(); // add this line to write each row on a new line
    //         }
    //     }
    // }
}
