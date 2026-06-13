package main;

import org.w3c.dom.ls.LSOutput;
import tasks.TaskState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener { // must add the key: typed, pressed and released methods when implementing KeyListener

    public boolean upPressed, downPressed, leftPressed, rightPressed, shiftPressed, enterPressed, ePressed;
    public StringBuilder inputText;
    // DEBUG
    boolean checkDebugText = false;

    GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
        inputText = new StringBuilder();
    }

    @Override
    public void keyTyped(KeyEvent e) { // don't use this method but for the bottom two we will.

        // For computer state
        if(gp.mouseH.clickOnPasswordBox) {

            char c = e.getKeyChar();

            // only allow normal characters
            if(gp.currentTask == TaskState.GO_TO_COMPUTER) {
                if (Character.isLetterOrDigit(c) && inputText.length() < 17) {
                    inputText.append(c);
                }
            }
        }
    }

    // RUNS ONCE FOR EVERY KEY INPUT
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); // returns integer KeyCode associated with the key in this event. (returns num of the key that was pressed) google the cheat sheet if you need help-- for ex: A is VK_A

        if(gp.gameState == gp.titleState) titleState(code);
        else if(gp.gameState == gp.initialDialogueState) initDialogueState(code);
        else if(gp.gameState == gp.playState) playState(code);
        else if(gp.gameState == gp.dialogueState) dialogueState(code);
        else if(gp.gameState == gp.inventoryState) inventoryState(code);
        else if(gp.gameState == gp.pausedState) pausedState(code);
        else if(gp.gameState == gp.gameOverState) gameOverState(code);
        else if(gp.gameState == gp.transitionMapState) transitionMapState(code);
        else if(gp.gameState == gp.computerState) computerState(code);
        else if(gp.gameState == gp.logBookState) logBookState(code);
        else if(gp.gameState == gp.finishedGame) finishedGameState(code);
    }

    public void titleState(int code) {

        // **** MAIN MENU POINTERS ****
        if(gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) { // keeps arrow from disappearing from select menu
                    gp.ui.commandNum = 2;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER) {
                // NEW GAME
                if (gp.ui.commandNum == 0) {
                    gp.playSE(6); // SOUND EFFECT WHEN PRESSING ENTER
                    gp.gameState = gp.initialDialogueState;
                }
                // CONTROLS
                if (gp.ui.commandNum == 1) {
                    gp.playSE(6); // SOUND EFFECT WHEN PRESSING ENTER
                    gp.ui.titleScreenState = 1;
                    gp.ui.commandNum = 0;
                }
                // QUIT
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        //  **** POINTERS FOR CONTROLS MENU ****
        else if(gp.ui.titleScreenState == 1) {
            if(code == KeyEvent.VK_ENTER) {
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 0;
            }
        }

    }

    public void finishedGameState(int code) {
    }

    public void initDialogueState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            gp.ui.skipDialogue = true;
        }
    }

    public void transitionMapState(int code) {

        if(code == KeyEvent.VK_W) {
            if(gp.ui.commandNum > 0) {
                gp.ui.commandNum--;
            }
            else {
                gp.ui.commandNum = 1;
            }
        }
        else if(code == KeyEvent.VK_S) {
            if(gp.ui.commandNum < 1) {
                gp.ui.commandNum++;
            }
            else {
                gp.ui.commandNum = 0;
            }
        }
        else if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    public void computerState(int code) {

        // delete characters typed in password box
        if(code == KeyEvent.VK_BACK_SPACE) {

            if(gp.mouseH.clickOnPasswordBox == true) {
                if (inputText.length() > 0) {
                    inputText.deleteCharAt(inputText.length() - 1);
                }
            }
        }
        if(code == KeyEvent.VK_G) {

            if(gp.closeTaskList == false) {
                gp.closeTaskList = true;
            } else {
                gp.closeTaskList = false;
            }
        }
    }

    public void logBookState(int code) {
        if(code == KeyEvent.VK_ESCAPE) gp.gameState = gp.playState;
    }


    public void playState(int code) {

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SHIFT) { // SPRINTING
            shiftPressed = true;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.inventoryState;
        }
        if(code == KeyEvent.VK_E) {
            ePressed = true;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.pausedState;
        }
        if(code == KeyEvent.VK_M) {

            if (gp.mapOn == false) {
                gp.mapOn = true;
            } else {
                gp.mapOn = false;
            }
        }
        if(code == KeyEvent.VK_G) {

            if(gp.closeTaskList == false) {
                gp.closeTaskList = true;
            } else {
                gp.closeTaskList = false;
            }
        }

        // DEBUG
        if(code == KeyEvent.VK_T) {
            if(checkDebugText == false) {
                checkDebugText = true;
            }
            else if(checkDebugText == true) {
                checkDebugText = false;
            }
        }


        // DELETE NOW THAT ITS NOT GONNA BE USED!!
        // FAST REAL-TIME MAP EDIT
        if(code == KeyEvent.VK_B) {

            switch(gp.currentMap) {
                case 0: gp.tileM.loadMap("/maps/gasStation.txt"); break;
                case 1: gp.tileM.loadMap("/maps/world01.txt"); break;
            }
        }
    }

    public void gameOverState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
    }

    public void dialogueState(int code) {

        if(code == KeyEvent.VK_W) {
            if(gp.ui.commandNum > 0) {
                gp.ui.commandNum--;
            }
            else {
                gp.ui.commandNum = 1;
            }
        }
        else if(code == KeyEvent.VK_S) {
            if(gp.ui.commandNum < 1) {
                gp.ui.commandNum++;
            }
            else {
                gp.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

    }


    public void inventoryState(int code) {
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W) {
            if(gp.ui.slotRow != 0) {
                gp.playSE(16);
                gp.ui.slotRow--;
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.slotCol != 0) {
                gp.playSE(16);
                gp.ui.slotCol--;
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gp.ui.slotRow != 2) {
                gp.playSE(16);
                gp.ui.slotRow++;
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.slotCol != 3) {
                gp.playSE(16);
                gp.ui.slotCol++;
            }
        }

        if(code == KeyEvent.VK_ENTER) { // SELECTING AN ITEM FROM INVENTORY - NEED TO FIND A BETTER WAY TO HANDLE THE IF STATEMENTS
                                        // INSTEAD OF JUST USING ITEM POSITION. PERHAPS A BOOLEAN ENABLED IN FOR LOOP WHERE INVENTORY IS DRAWN?
            gp.playSE(12);
            gp.player.selectItem();
        }

    }


    public void pausedState(int code) {

        // For main window
        int topCommandNum = 0;
        int bottomCommandNum = 3;

        // Main window
        if(gp.ui.subState == 0) {

            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if (code == KeyEvent.VK_W) {
                if (gp.ui.commandNum > 0) {
                    gp.ui.commandNum--;
                } else {
                    gp.ui.commandNum = bottomCommandNum;
                }
            }
            if (code == KeyEvent.VK_S) {
                if (gp.ui.commandNum < 3) {
                    gp.ui.commandNum++;
                } else {
                    gp.ui.commandNum = topCommandNum;
                }
            }
        }

        // Options menu
        else if(gp.ui.subState == 1) {

            if(code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if(code == KeyEvent.VK_W) {

                if(gp.ui.commandNum > 0) { gp.ui.commandNum--; }
                else { gp.ui.commandNum = bottomCommandNum; }
            }
            if(code == KeyEvent.VK_S) {

                if(gp.ui.commandNum < 3) { gp.ui.commandNum++; }
                else { gp.ui.commandNum = topCommandNum; }
            }
            // Adjust volume keys - for Music and SE
            if(code == KeyEvent.VK_A) {

                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                }
            }
            if(code == KeyEvent.VK_D) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                }
            }
        }

        // Controls menu
        else if(gp.ui.subState == 2) {

            if(code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
        }

        // Fullscreen notification
        else if(gp.ui.subState == 3) {
            if(code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
        }

        // Exit game
        else if(gp.ui.subState == 4) {

            if(code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if(code == KeyEvent.VK_W){
                if(gp.ui.commandNum > 0) {
                    gp.ui.commandNum--;
                }
                else gp.ui.commandNum = 1;
            }
            if(code == KeyEvent.VK_S){
                if(gp.ui.commandNum < 1){
                    gp.ui.commandNum++;
                }
                else gp.ui.commandNum = 0;
            }

        }

    }


    @Override
    public void keyReleased(KeyEvent e) { // the boolean variables are set to false because it's telling the computer that no key is being detected thus is the goal of the keyReleased method.

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }


    }



}
