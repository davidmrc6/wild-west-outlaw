package wildwestoutlaw;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, rightPressed, leftPressed, spacePressed, enterPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Title state
        if (gp.gameState == gp.titleState) {
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
            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(13);
                if (gp.ui.commandNum == 0) {
                    if (gp.hasPlayerSeenTutorial == false) {
                        enterPressed = true;
                        gp.gameState = gp.tutorialState;
                    }
                    if (gp.hasPlayerSeenTutorial == true) {
                        gp.gameState = gp.playState;
                    }
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }

        // Game State
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }
            if (code == KeyEvent.VK_P) {
                gp.playSE(13);
                gp.gameState = gp.pauseState;
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
        }

        // Pause state
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) {
                gp.playSE(13);
                gp.gameState = gp.playState;
            }
        }

        // Dialogue state
        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }

        // Game over state
        else if (gp.gameState == gp.gameOverState) {
            
            if (code == KeyEvent.VK_W) {
                gp.playSE(12);
                gp.ui.commandNum --;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.playSE(12);
                gp.ui.commandNum ++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(13);
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    gp.restart();
                }
                else if (gp.ui.commandNum == 1) {
                    gp.gameState = gp.titleState;
                    gp.restart();
                }
            }
        }

        // Game won state
        else if (gp.gameState == gp.gameWonState) {

            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(13);
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }

        // Tutorial state
        else if (gp.gameState == gp.tutorialState && enterPressed == false) {

            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(13);
                gp.gameState = gp.playState;
                gp.hasPlayerSeenTutorial = true; 
                enterPressed = false;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }

    }
}
