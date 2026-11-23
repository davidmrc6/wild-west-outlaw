package wildwestoutlaw;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    BufferedImage heart_full, heart_3o4, heart_2o4, heart_1o4, heart_0o4,
                    star_full, star_half, star_empty,
                    reputation_full, reputation_half, reputation_empty,
                    coin_img,
                    A_key, S_key, D_key, W_key;
    Font Carnevalee;
    Font pixeloid;
    public String currentDialogue;
    public int commandNum = 0;
    public boolean hasGameOverSoundPlayed = false;
    public boolean hasGameWonSoundPlayed = false;
    private BufferedImage menuBackdrop;
    private int backdropWorldX, backdropWorldY;
    private final BufferedImage[] titleScreens = new BufferedImage[2]; // one per cursor position

    public UI(GamePanel gp) {
        this.gp = gp;

        Carnevalee = Assets.font("/font/Carnevalee Freakshow.ttf");
        pixeloid = Assets.font("/font/PixeloidSansBold-PKnYd.ttf");

        // HUD images
        heart_full = Assets.image("/hud/hearts/heart full.png", gp.tileSize);
        heart_3o4 = Assets.image("/hud/hearts/heart 3o4.png", gp.tileSize);
        heart_2o4 = Assets.image("/hud/hearts/heart 2o4.png", gp.tileSize);
        heart_1o4 = Assets.image("/hud/hearts/heart 1o4.png", gp.tileSize);
        heart_0o4 = Assets.image("/hud/hearts/heart 0o4.png", gp.tileSize);

        star_full = Assets.image("/hud/stars/star_full.png", gp.tileSize);
        star_half = Assets.image("/hud/stars/star_half.png", gp.tileSize);
        star_empty = Assets.image("/hud/stars/star_empty.png", gp.tileSize);

        reputation_full = Assets.image("/hud/reputation/reputation_full.png", gp.tileSize);
        reputation_half = Assets.image("/hud/reputation/reputation_half.png", gp.tileSize);
        reputation_empty = Assets.image("/hud/reputation/reputation_empty.png", gp.tileSize);

        coin_img = Assets.image("/hud/coins/coin.png", gp.tileSize);

        getKeyImages();
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(Carnevalee);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // TITLE STATE
        if (gp.gameState == GameState.TITLE) {
            drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == GameState.PLAY) {
            if (gp.spawner.isWaveInProgress()) {
                drawZombieKillCounter();
            }
            drawPlayerLife();
            drawWantedStars();
            drawReputation();
            drawCoinCounter();
        }
        // PAUSE STATE
        if (gp.gameState == GameState.PAUSE) {
            drawPauseScreen();
            drawPlayerLife();
            drawWantedStars();
            drawReputation();
            drawCoinCounter();
        }
        // GAME OVER STATE
        if (gp.gameState == GameState.GAME_OVER) {
            drawGameOverScreen();
        }

        // GAME WON STATE
        if (gp.gameState == GameState.GAME_WON) {
            drawGameWonScreen();
        }
        // DIALOGUE STATE
        if (gp.gameState == GameState.DIALOGUE) {
            drawPlayerLife();
            drawWantedStars();
            drawReputation();
            drawCoinCounter();
            drawDialogueScreen();
        }

        // TUTORIAL STATE
        if (gp.gameState == GameState.TUTORIAL) {
            drawTutorialScreen();
        }

    }

    // Map + dark overlay behind the title and tutorial screens, drawn once and reused
    private void drawMenuBackdrop() {
        if (menuBackdrop == null || backdropWorldX != gp.player.worldX || backdropWorldY != gp.player.worldY) {
            menuBackdrop = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D b = menuBackdrop.createGraphics();
            b.setColor(GamePanel.SAND);
            b.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            gp.tileCollision.draw(b);
            b.setColor(new Color(0, 0, 0, 240));
            b.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            b.dispose();
            backdropWorldX = gp.player.worldX;
            backdropWorldY = gp.player.worldY;
        }
        g2.drawImage(menuBackdrop, 0, 0, null);
    }

    public void drawTutorialScreen() {

        drawMenuBackdrop();

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 90f));

        String text = "Press";

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        int y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize / 2;
        y += gp.tileSize;
        g2.drawImage(W_key, x, y, gp.tileSize, gp.tileSize, null);

        y += gp.tileSize + gp.tileSize / 4;
        g2.drawImage(S_key, x, y, gp.tileSize, gp.tileSize, null);

        x -= gp.tileSize + gp.tileSize / 8;
        g2.drawImage(A_key, x, y, gp.tileSize, gp.tileSize, null);

        x += 2 * gp.tileSize + gp.tileSize / 4;
        g2.drawImage(D_key, x, y, gp.tileSize, gp.tileSize, null);

        text = "To move.";
        length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y = gp.tileSize * 8;
        g2.drawString(text, x, y);

        text = "Talk to the bartender to start the game.";
        length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y = gp.tileSize * 10;
        g2.drawString(text, x, y);

        text = "Continue";
        length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y = gp.tileSize * 14;
        g2.drawString(text, x, y);
        g2.drawString(">", x - gp.tileSize, y);
    }

    public void getKeyImages() {
        W_key = Assets.image("/hud/keys/Wkey.png");
        A_key = Assets.image("/hud/keys/Akey.png");
        S_key = Assets.image("/hud/keys/Skey.png");
        D_key = Assets.image("/hud/keys/Dkey.png");
    }

    // The title screen only changes when the menu cursor moves, so each version of it is drawn once and
    // then reused
    public void drawTitleScreen() {

        if (backdropWorldX != gp.player.worldX || backdropWorldY != gp.player.worldY) {
            titleScreens[0] = null;
            titleScreens[1] = null;
        }

        if (titleScreens[commandNum] == null) {
            BufferedImage image = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D imageGraphics = image.createGraphics();
            imageGraphics.setFont(g2.getFont());
            imageGraphics.setRenderingHints(g2.getRenderingHints());

            Graphics2D screen = g2;
            g2 = imageGraphics;
            drawTitleScreenContents();
            g2 = screen;

            imageGraphics.dispose();
            titleScreens[commandNum] = image;
        }

        g2.drawImage(titleScreens[commandNum], 0, 0, null);
    }

    private void drawTitleScreenContents() {

        drawMenuBackdrop();

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 120F));

        String text = "Wild West Outlaw";

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x+5, y+5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize * 8;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.frontSprite(), x, y, gp.tileSize * 8, gp.tileSize * 8, null);

        x = gp.screenWidth / 2 + gp.tileSize * 1;
        g2.drawImage(gp.npc[0].frontSprite(), x, y, gp.tileSize * 8, gp.tileSize * 8, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));

        text = "Play";
        x = gp.screenWidth / 2 - length/16;
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize / 2, y);
        }

        text = "Quit";
        x = gp.screenWidth / 2 - length/16;
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize / 2, y);
        }
    }

    public void drawGameWonScreen() {

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 120f));

        text = "You win!";
        // Shadow
        g2.setColor(Color.black);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        if (hasGameWonSoundPlayed == false) {
            gp.playSE(SoundEffect.GAME_WON);
            hasGameWonSoundPlayed = true;
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));

        text = "Title screen";
        length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y += gp.tileSize * 10;
        g2.drawString(text, x, y);
        g2.drawString(">", x - gp.tileSize / 2, y);
    }

    public void drawGameOverScreen() {

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 120f));

        text = "You died!";
        // Shadow
        g2.setColor(Color.black);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60f));
        text = "Retry";
        length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        // Back to title screen
        text = "Title screen";
        length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        if (hasGameOverSoundPlayed == false) {
            gp.playSE(SoundEffect.GAME_OVER);
            hasGameOverSoundPlayed = true;
        }
    }

    public void drawZombieKillCounter() {

        String text = "Kill Enemies!";

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.black);

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        int y = gp.screenHeight / 12;

        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);
    }

    public void drawDialogueScreen() {

        g2.setFont(pixeloid);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));

        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 5;

        drawSubWindow(x, y, width, height);

        x += gp.tileSize / 2;
        y += gp.tileSize;

        for (String line: currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += gp.tileSize;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color color = new Color(0, 0, 0, 220);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }

    public void drawPauseScreen() {
        String text = "PAUSED";

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.black);

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);
    }

    public void drawCoinCounter() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.setColor(Color.white);

        int x = gp.tileSize / 2;
        int y = 7 * gp.tileSize / 4;


        g2.drawImage(coin_img, x , y, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.coins + " / 3", 7*gp.tileSize / 4, 5*gp.tileSize / 2);

    }

    public void drawPlayerLife() {

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // DRAW BLANK HEART
        while (i < gp.player.maxLife/4) {
            g2.drawImage(heart_0o4, x, y, null);
            i++;
            x += gp.tileSize + 3;
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heart_1o4, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_2o4, x, y, null);
            }
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_3o4, x, y, null);
            }
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize + 3;
        }

    }

    public void drawWantedStars() {

        int x = gp.screenWidth - 4 * gp.tileSize;
        int y = gp.tileSize/2;
        int i = 0;

        // DRAW BLANK STARS
        while (i < gp.player.maxWanted/4) {
            g2.drawImage(star_empty, x, y, null);
            i++;
            x += gp.tileSize + 3;
        }

        x = gp.screenWidth - 4 * gp.tileSize;
        y = gp.tileSize/2;
        i = 0;

        // DRAW CURRENT PLAYER WANTED
        while (i < gp.player.wanted && i <= gp.player.maxWanted) {

            if ((i+1) % 2 == 0 && !((i+1) % 4 == 0)) {
                g2.drawImage(star_half, x, y, null);
                i++;
            }

            else if ((i+1) % 4 == 0) {
                g2.drawImage(star_full, x, y, null);
                i++;
                x += gp.tileSize + 3;
            }

            i++;
        }
    }

    public void drawReputation() {

        int x = gp.screenWidth - 4 * gp.tileSize;
        int y = 7 * gp.tileSize / 4;
        int i = 0;

        // DRAW BLANK STARS
        while (i < gp.player.maxReputation/4) {
            g2.drawImage(reputation_empty, x, y, null);
            i++;
            x += gp.tileSize + 3;
        }

        x = gp.screenWidth - 4 * gp.tileSize;
        y = gp.tileSize/2 + gp.tileSize + 16;
        i = 0;

        // DRAW CURRENT PLAYER REPUTATION
        while (i < gp.player.reputation && i <= gp.player.maxReputation) {

            if ((i+1) % 2 == 0 && !((i+1) % 4 == 0)) {
                g2.drawImage(reputation_half, x, y, null);
                i++;
            }

            else if ((i+1) % 4 == 0) {
                g2.drawImage(reputation_full, x, y, null);
                i++;
                x += gp.tileSize + 3;
            }

            i++;
        }
    }
}
