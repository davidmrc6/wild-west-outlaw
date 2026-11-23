package wildwestoutlaw;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    // Keys currently held down (the player reads these every frame)
    public boolean upPressed, downPressed, rightPressed, leftPressed, spacePressed;

    private final Queue<KeyEvent> events = new ConcurrentLinkedQueue<>();
    private final Set<Integer> heldKeys = new HashSet<>();
    private volatile boolean focusLost = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        events.add(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        events.add(e);
    }

    // The window lost focus, so we won't hear about any keys being let go
    public void focusLost() {
        focusLost = true;
    }

    // Called by the game thread at the start of every update
    public void processInput() {

        KeyEvent e;
        while ((e = events.poll()) != null) {
            int code = e.getKeyCode();

            if (e.getID() == KeyEvent.KEY_PRESSED) {
                // Holding a key down makes it repeat: only the first press counts
                if (heldKeys.add(code)) {
                    setHeld(code, true);
                    handlePress(code);
                }
            } else {
                heldKeys.remove(code);
                setHeld(code, false);
            }
        }

        if (focusLost) {
            focusLost = false;
            heldKeys.clear();
            upPressed = downPressed = leftPressed = rightPressed = spacePressed = false;
        }
    }

    private void setHeld(int code, boolean held) {
        switch (code) {
            case KeyEvent.VK_W -> upPressed = held;
            case KeyEvent.VK_A -> leftPressed = held;
            case KeyEvent.VK_S -> downPressed = held;
            case KeyEvent.VK_D -> rightPressed = held;
            case KeyEvent.VK_SPACE -> spacePressed = held;
        }
    }

    private void handlePress(int code) {

        switch (gp.gameState) {

            case TITLE -> {
                moveMenuCursor(code);
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(13);
                    if (gp.ui.commandNum == 0) {
                        gp.gameState = gp.hasPlayerSeenTutorial ? GameState.PLAY : GameState.TUTORIAL;
                        gp.playMusic(0);
                    }
                    if (gp.ui.commandNum == 1) {
                        System.exit(0);
                    }
                }
            }

            case TUTORIAL -> {
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(13);
                    gp.gameState = GameState.PLAY;
                    gp.hasPlayerSeenTutorial = true;
                }
            }

            case PLAY -> {
                if (code == KeyEvent.VK_P) {
                    gp.playSE(13);
                    gp.gameState = GameState.PAUSE;
                }
            }

            case PAUSE -> {
                if (code == KeyEvent.VK_P) {
                    gp.playSE(13);
                    gp.gameState = GameState.PLAY;
                }
            }

            case DIALOGUE -> {
                if (code == KeyEvent.VK_ENTER) {
                    gp.gameState = GameState.PLAY;
                }
            }

            case GAME_OVER -> {
                moveMenuCursor(code);
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(13);
                    if (gp.ui.commandNum == 0) {
                        gp.gameState = GameState.PLAY; // Retry
                    }
                    else if (gp.ui.commandNum == 1) {
                        gp.gameState = GameState.TITLE; // Title screen
                    }
                    gp.restart();
                }
            }

            case GAME_WON -> {
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(13);
                    gp.gameState = GameState.TITLE;
                    gp.restart();
                }
            }
        }
    }

    // W and S move between the two options of the title and game over menus
    private void moveMenuCursor(int code) {
        if (code == KeyEvent.VK_W) {
            gp.playSE(12);
            gp.ui.commandNum --;
            if (gp.ui.commandNum < 0)
                gp.ui.commandNum = 1;
        }
        if (code == KeyEvent.VK_S) {
            gp.playSE(12);
            gp.ui.commandNum ++;
            if (gp.ui.commandNum > 1)
                gp.ui.commandNum = 0;
        }
    }
}
