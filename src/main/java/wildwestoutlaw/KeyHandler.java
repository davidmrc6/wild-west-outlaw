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
    public boolean aimUp, aimDown, aimLeft, aimRight; // arrow keys: shoot in that direction

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
            aimUp = aimDown = aimLeft = aimRight = false;
        }
    }

    private void setHeld(int code, boolean held) {
        switch (code) {
            case KeyEvent.VK_W -> upPressed = held;
            case KeyEvent.VK_A -> leftPressed = held;
            case KeyEvent.VK_S -> downPressed = held;
            case KeyEvent.VK_D -> rightPressed = held;
            case KeyEvent.VK_SPACE -> spacePressed = held;
            case KeyEvent.VK_UP -> aimUp = held;
            case KeyEvent.VK_DOWN -> aimDown = held;
            case KeyEvent.VK_LEFT -> aimLeft = held;
            case KeyEvent.VK_RIGHT -> aimRight = held;
        }
    }

    private void handlePress(int code) {

        if (code == KeyEvent.VK_F3) {
            gp.showHitboxes = !gp.showHitboxes;
            return;
        }
        if (code == KeyEvent.VK_M) {
            gp.sound.toggleMute();
            return;
        }
        if (code == KeyEvent.VK_F11) {
            gp.toggleFullScreen();
            return;
        }

        switch (gp.gameState) {

            case TITLE -> {
                moveMenuCursor(code);
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(SoundEffect.MENU_SELECT);
                    if (gp.ui.commandNum == 0) {
                        gp.gameState = gp.hasPlayerSeenTutorial ? GameState.PLAY : GameState.TUTORIAL;
                        gp.playMusic();
                    }
                    if (gp.ui.commandNum == 1) {
                        System.exit(0);
                    }
                }
            }

            case TUTORIAL -> {
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(SoundEffect.MENU_SELECT);
                    gp.gameState = GameState.PLAY;
                    gp.hasPlayerSeenTutorial = true;
                }
            }

            case PLAY -> {
                if (code == KeyEvent.VK_P) {
                    gp.playSE(SoundEffect.MENU_SELECT);
                    gp.gameState = GameState.PAUSE;
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.player.interact();
                }
            }

            case PAUSE -> {
                if (code == KeyEvent.VK_P) {
                    gp.playSE(SoundEffect.MENU_SELECT);
                    gp.gameState = GameState.PLAY;
                }
            }

            case DIALOGUE -> {
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.talkingTo != null) {
                        gp.talkingTo.nextLine();
                    } else {
                        gp.gameState = GameState.PLAY;
                    }
                }
            }

            case GAME_OVER -> {
                moveMenuCursor(code);
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(SoundEffect.MENU_SELECT);
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
                    gp.playSE(SoundEffect.MENU_SELECT);
                    gp.gameState = GameState.TITLE;
                    gp.restart();
                }
            }
        }
    }

    // W/S (or the arrow keys) move between the two options of the title and game over menus
    private void moveMenuCursor(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.playSE(SoundEffect.MENU_MOVE);
            gp.ui.commandNum --;
            if (gp.ui.commandNum < 0)
                gp.ui.commandNum = 1;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.playSE(SoundEffect.MENU_MOVE);
            gp.ui.commandNum ++;
            if (gp.ui.commandNum > 1)
                gp.ui.commandNum = 0;
        }
    }
}
