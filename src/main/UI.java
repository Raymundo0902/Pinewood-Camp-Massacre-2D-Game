package main;

import entity.Entity;
import tasks.TaskState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    BufferedImage menuImage, checkMark, logBook;
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    public boolean messageOn = false;
    public String message = "";
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0; // arrow pointer of selection in main menu
    public int titleScreenState = 0; // 0: the first screen, 1: the second screen
    public int imgSpriteCounter = 0;
    public int imgSpriteNum = 1;
    public int playerType; // 1 for sally, 2 for chad

    // INITIAL DIALOGUE MECHANICS
    public int dialogueIndex = 0; // WHEN PRESSING ENTER IT INCREASES TO GO THROUGH THE ARRAY
    String[] introDialogues; // STORES AN ARRAY OF ALL THE STRINGS OF THE INTRO DIALOGUE
    int wordEnd = 0;
    int nextLine = 0;
    boolean skipDialogue = false;
    int defaultYPosition;
    // USING TILESIZE REAL NUMBERS SINCE IF I PUT GP'S VARIABLES HERE ITS STILL NULL AT THE TIME.
    int introDialogueY = 96;
    public boolean finishedTyping = false;

    // Other Dialogue Mechanics - inner dialogue, etc
    public int npcIndex = 0;
    int innerWordEnd = 0;
    int innerNextLine = 0;
    int innerWordDelay = 0;
    int innerDialogueY = 480;

    // INNER DIALOGUE


    // TIMESTAMP
    int timeWordEnd = 0;
    int timeNextLine = 0;
    int timeWordDelay = 0;




    // Task UI dialogue
    public String[] currentTask = new String[20];
    public boolean[][] checkmarks = new boolean[20][];
    public int taskIndex = 0;

    // Transition screen
    float j = 0f;
    boolean fadingOut = false;
    int blackScreenPause  = 0;

    // INVENTORY DESIGN
    public int slotCol = 0; // indicates the cursors current position on inventory window
    public int slotRow = 0;
    final int MAX_COL = 4;

    // OPTIONS MENU
    int subState = 0;

    // EXTRA BOOLEANS/CONDITIONALS/COUNTERS
    int innerDialogueCounter = 0;

    // COMPUTER OS
    BufferedImage pinewoodIcon, osIcon, recycleIcon, osBackground, speakerIcon, signInIcon, pinewoodHomePage, a1Cabin, j1Cabin, k4Cabin, folderIcon;
    Rectangle pineWButtonBounds, exitButton, passwordButton, signInButton, assignButton;

    // OS WINDOWS STATE - goes from 0 to 1
    int osSubState = 0;

    public UI (GamePanel gp) {
        this.gp = gp;

        // MOUSE INTERACTIVITY - fake os
        pineWButtonBounds = new Rectangle(gp.tileSize * 2, gp.tileSize * 11 + 18, gp.tileSize * 4, 30);
        exitButton = new Rectangle(gp.tileSize * 18,  (int)(gp.tileSize * 9.5), gp.tileSize, gp.tileSize);
        passwordButton = new Rectangle(gp.tileSize * 7, (int) (gp.tileSize * 4.5) + 10, gp.tileSize * 3,27);
        signInButton = new Rectangle(gp.tileSize * 7 + 24, gp.tileSize * 5 + 20, gp.tileSize * 2,27);
        assignButton = new Rectangle(gp.tileSize* 11 + 30, gp.tileSize * 6 + 30, gp.tileSize * 2, 27);

        loadBufferedImage();

        // INITIALIZE THE INTRODUCTORY DIALOGUE
        setIntroArray();
        setTaskList();

        defaultYPosition = gp.tileSize * 2;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        // INITIAL DIALOGUE STATE
        else if(gp.gameState == gp.initialDialogueState) {
            drawIntroDialogueScreen();
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawPlayerStamina();

            if(gp.player.interactableCollision) drawInteractButton();
            if(!gp.closeTaskList) drawCurrentTask();
            if(gp.drawInnerDialogue) drawPlayerInnerDialogue();
        }
        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawPlayerStamina();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        else if(gp.gameState == gp.inventoryState) {
            drawPlayerStamina();
            drawPlayerLife();
            drawInventory();
        }
        // OPTIONS/PAUSE STATE
        else if(gp.gameState == gp.pausedState){
            drawPlayerLife();
            drawPlayerStamina();
            drawPausedMenu();
        }
        // GAME OVER STATE
        else if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        else if(gp.gameState == gp.transitionMapState) {
            drawExitMapScreen();
            drawCurrentTask();
        }
        else if(gp.gameState == gp.computerState) {
            drawOS();
            if(gp.closeTaskList == false) {
                drawCurrentTask();
            }
        }
        else if(gp.gameState == gp.transitionState) {
            transitionScreen();
        }
        else if(gp.gameState == gp.logBookState) {
            drawLogBook();
            drawPlayerStamina();
            drawPlayerLife();
        }
    }

    public void transitionScreen() {

        if(fadingOut == false) {
            g2.setColor(Color.black);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, j));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            g2.setComposite(AlphaComposite.SrcOver);   // switch back to normal mode - prevents other things to get blended
            j += 0.01f;
            // if j = 1f, the screen is entirely black thus, we can now let transitions happen to make smooth transitions, or drawings
            if(j >= 1f) {
                j = 1f;
                if(!gp.drawTimeStamp) {
                    blackScreenPause++;
                    if (blackScreenPause >= 60) {
                        fadingOut = true;
                        blackScreenPause = 0;
                    }
                }
                // extend time so player can see the "3:15 AM" drawing longer
                else {
                    drawTime();
                    blackScreenPause++;
                    if (blackScreenPause >= 480) {
                        fadingOut = true;
                        gp.drawTimeStamp = false;
                        blackScreenPause = 0;
                    }

                }
            }
        }
        else if(fadingOut == true) {

            g2.setColor(Color.black);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, j));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            g2.setComposite(AlphaComposite.SrcOver);   // switch back to normal mode - prevents other things to get blended
            j -= 0.01f;
            if(j <= 0f) {
                j = 0f;
                fadingOut = false; // ready for next transition
                gp.gameState = gp.playState;
                gp.oneTime = false; // can safely put it here that triggers only once
            }
        }
    }

    public void drawPlayerStamina() {
        int x = gp.tileSize;
        int y = gp.tileSize * 2 + 5;

        int barWidth = gp.tileSize * 4;
        int barHeight = 20;

        int maxStamina = 180; // your sprint limit before tired phase

        int stamina = maxStamina - gp.player.sprintCounter;

        if(stamina < 0) {
            stamina = 0;
        }
        if(stamina > maxStamina) {
            stamina = maxStamina;
        }

        int staminaWidth = (int)((double)stamina / maxStamina * barWidth);

        // OUTLINE
        g2.setColor(Color.BLACK);
        g2.fillRect(x - 2, (y / 2) - 2, barWidth + 4, barHeight + 4);

        // EMPTY BAR BACKGROUND
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(x, y / 2, barWidth, barHeight);

        // STAMINA BAR
        g2.setColor(new Color(15, 57, 224));
        g2.fillRect(x, y / 2, staminaWidth, barHeight);


    }

    public void drawPlayerInnerDialogue() {


        innerDialogueCounter++;

        // Styles
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50f));


        String currLine = gp.player.dialogues[gp.player.pInnerDialogueIndex];
        if(currLine == null) {
            gp.drawInnerDialogue = false;
            return;
        }
        String[] lines = currLine.split("\n");
        int middleX;
        int baseY = gp.tileSize * 10;

        // Main Text - keeps text on screen and aligned
        for (int i = 0; i < innerNextLine; i++) {
            middleX = getXforCenteredText(lines[i]);
            drawGlowText(g2, lines[i], middleX, baseY + (i * 40));
        }

        if(innerDialogueCounter < 360) {
            if (innerNextLine < lines.length) {

                String curWord = lines[innerNextLine].substring(0, innerWordEnd);
                middleX = getXforCenteredText(curWord);
                drawGlowText(g2, curWord, middleX, baseY + (innerNextLine * 40));

                if (innerWordEnd < lines[innerNextLine].length()) {
                    innerWordDelay++;
                    if (innerWordDelay == 3) {
                        innerWordEnd++;
                        innerWordDelay = 0;
                    }
                } else {
                    innerNextLine++;
                    innerWordEnd = 0;
                }
            }
            // reached end of array, just hold
            else {
                innerNextLine = lines.length - 1;
                innerWordEnd = lines[lines.length - 1].length();
            }
        }

        // Erase phase
        else {

            // clamp in case we enter erase phase with nextLineTwo out of bounds
            if(innerNextLine >= lines.length) {
                innerNextLine = lines.length - 1;
                innerWordEnd = lines[innerNextLine].length();
            }

            if(innerNextLine >= 0) {

                int clampedWordEnd = Math.min(innerWordEnd, lines[innerNextLine].length());
                String curWord = lines[innerNextLine].substring(0, clampedWordEnd);
                middleX = getXforCenteredText(curWord);
                drawGlowText(g2, curWord, middleX, baseY + (innerNextLine * 40));

                if (innerWordEnd > 0) {
                    innerWordDelay++;
                    if (innerWordDelay == 3) {
                        innerWordEnd--;
                        innerWordDelay = 0;
                    }
                } else {
                    innerNextLine--;
                    if(innerNextLine >= 0) {
                        innerWordEnd = lines[innerNextLine].length();
                    }
                }
            }
            else {
                gp.drawInnerDialogue = false;
                gp.player.pInnerDialogueIndex++;
                innerDialogueY = gp.tileSize * 10;
                innerWordEnd = 0;
                innerNextLine = 0;
            }
        }
    }


    public void drawTime() {

        // STYLES
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80f));
        String timeStamp = "3:15 AM";
        // lines[0] will just contain: ["3:15 AM"]
        String[] lines = timeStamp.split("\n");
        int middleX;

        int y = gp.tileSize * 5;

        // MAIN TEXT - KEEPS TEXT ALIGNED ON SCREEN
        for (int i = 0; i < timeNextLine; i++) {
            middleX = getXforCenteredText(lines[i]);
            drawGlowText(g2, lines[i], middleX, y);
        }

        // ****** MOVING TEXT ANIMATION *******

        if(blackScreenPause < 360) {
            if (timeNextLine < lines.length) {

                String curWord = lines[timeNextLine].substring(0, timeWordEnd);
                middleX = getXforCenteredText(curWord);
                drawGlowText(g2, curWord, middleX, y);
                if (timeWordEnd < lines[timeNextLine].length()) {
                    timeWordDelay++;
                    if (timeWordDelay == 20) {
                        timeWordEnd++;
                        timeWordDelay = 0;
                    }
                }
            }
        }

        // ****** SHORTEN WORD LENGTH ******

        else {
                timeNextLine = 0;

                String curWord = lines[timeNextLine].substring(0, timeWordEnd);
                middleX = getXforCenteredText(curWord);
                drawGlowText(g2, curWord, middleX, y);

                if (timeWordEnd >= 0) {
                    timeWordDelay++;
                    if (timeWordDelay == 20) {
                        timeWordEnd--;
                        timeWordDelay = 0;
                    }
                }
        }
    }

    public void drawInteractButton() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
        // outline
        g2.setColor(Color.black);
        g2.drawString("Press ' E ' to interact", gp.player.screenX + 42, gp.player.screenY + 29);
        g2.drawString("Press ' E ' to interact", gp.player.screenX + 44, gp.player.screenY + 31);

        g2.setColor(Color.WHITE);
        g2.drawString("Press ' E ' to interact", gp.player.screenX + 43, gp.player.screenY + 30);
    }

    public void drawOS() {

        // Main OS screen
        int mainX = 0;
        int mainY = 0;
        int width = gp.screenWidth;
        int height = gp.screenHeight;

            // os wallpaper
        g2.drawImage(osBackground, mainX, mainY, width, height, null);
            // recycle icon
        g2.drawImage(recycleIcon, mainX + 20, mainY + 30, gp.tileSize, gp.tileSize, null);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.darkGray);
        g2.drawString("Recycle bin", mainX + 9, mainY + gp.tileSize * 2 + 1);
        g2.setColor(Color.WHITE);
        g2.drawString("Recycle bin", mainX + 10, mainY + gp.tileSize * 2);
        g2.drawImage(folderIcon, gp.tileSize *  17, gp.tileSize * 6, gp.tileSize, gp.tileSize, null);
        g2.setColor(Color.darkGray);
        g2.drawString("reports", gp.tileSize * 17 + 5, gp.tileSize * 7 + 9);
        g2.setColor(Color.WHITE);
        g2.drawString("reports", gp.tileSize * 17 + 4, gp.tileSize * 7 + 10);
        g2.drawImage(folderIcon, gp.tileSize * 16, gp.tileSize * 8, gp.tileSize, gp.tileSize, null);
        g2.setColor(Color.darkGray);
        g2.drawString("missing items", gp.tileSize * 15 + 30, gp.tileSize * 9 + 9);
        g2.setColor(Color.WHITE);
        g2.drawString("missing items", gp.tileSize * 15 + 29, gp.tileSize * 9 + 10);

        g2.drawImage(folderIcon, gp.tileSize, gp.tileSize * 6, gp.tileSize, gp.tileSize, null);
        g2.setColor(Color.darkGray);
        g2.drawString("cam vids", gp.tileSize + 1, gp.tileSize * 7 + 9);
        g2.setColor(Color.WHITE);
        g2.drawString("cam vids", gp.tileSize, gp.tileSize * 7 + 10);

        // Task bar
        int taskBarX = 0;
        int taskBarY = gp.tileSize * 11 + 18;
        int taskBarWidth = gp.screenWidth;
        int taskBarHeight = 36;
        g2.setColor(new Color(36, 93, 218));
        g2.fillRect(taskBarX,taskBarY,taskBarWidth,taskBarHeight);
            // Start button
        int startButtonX = 0;
        int startButtonY = gp.tileSize * 11 + 18;
        int startButtonWidth = gp.tileSize * 2;
        int startButtonHeight = 36;
        g2.setColor(new Color(0, 150, 0));
        g2.fillRect(startButtonX,startButtonY,startButtonWidth,startButtonHeight);
                // Start button's text
        g2.drawImage(osIcon, startButtonX, startButtonY, null);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.darkGray);
        g2.drawString("Begin", startButtonX + 22, startButtonY + 20);
        g2.setColor(Color.WHITE);
        g2.drawString("Begin", startButtonX + 24, startButtonY + 22);
            // Running apps icon button
        int appsIconX = gp.tileSize * 2;
        int appsIconY = startButtonY;
        int appsIconWidth = gp.tileSize * 4;
        int appsIconHeight = startButtonHeight;
        g2.setColor(new Color(36, 110, 218));
        g2.fillRect(appsIconX,appsIconY,appsIconWidth,appsIconHeight);
        g2.drawImage(pinewoodIcon, appsIconX + 5, appsIconY + 5, null);
                // Running apps icon text
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.darkGray);
        g2.drawString("Pinewood Associates", appsIconX + 28, appsIconY + 20);
        g2.setColor(Color.WHITE);
        g2.drawString("Pinewood Associates", appsIconX + 30, appsIconY + 22);

        // Right sidebar
        g2.setColor(new Color(36, 110, 218));
        g2.fillRect(gp.tileSize * 17 , appsIconY,gp.tileSize * 3, appsIconHeight);
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
        g2.drawString("<", gp.tileSize * 17 + 3, appsIconY+ 25);
        g2.drawImage(speakerIcon, gp.tileSize * 17 + 20, appsIconY - 8, null);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
        g2.drawString("10/13/2000", gp.tileSize * 18 + 28, appsIconY + 20);


        // Exit button
        int exitX = gp.tileSize * 18;
        int exitY = (int) (gp.tileSize * 9.5);
        g2.setColor(Color.black);
        g2.drawRect(exitX -1, exitY -1, gp.tileSize + 1, gp.tileSize + 1);
        g2.setColor(new Color(200,50,50));
        g2.fillRect(exitX, exitY, gp.tileSize, gp.tileSize);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40f));
        g2.setColor(Color.WHITE);
        g2.drawString("->", exitX + 10, exitY + 34);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.drawString("Exit", exitX + 10, exitY + 70);


        // DEBUG: Mouse interactive buttons
//        g2.setColor(Color.red);
//        g2.fillRect(pineWButtonBounds.x, pineWButtonBounds.y, pineWButtonBounds.width, pineWButtonBounds.height);
//        g2.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);


        // Switch between different displays of the opened window.
        switch(osSubState) {
            case 0: drawLoginScreen(); break;
            case 1: drawSelectCabinScreen(); break;
        }
    }

    private void drawLoginScreen() {

        // Window screen
        int windowX = gp.tileSize * 5;
        int windowY = gp.tileSize;
        int windowWidth = gp.tileSize * 7;
        int windowHeight = gp.tileSize * 7;
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(windowX,windowY,windowWidth,windowHeight, 2, 2);

        // Window Title bar
        int titleBarX = windowX;
        int titleBarY = windowY;
        int titleBarWidth = windowWidth;
        int titleBarHeight = 24;
        g2.setColor(new Color(90, 61, 48));
        g2.fillRoundRect(titleBarX,titleBarY,titleBarWidth,titleBarHeight, 2, 2);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(windowX,windowY,windowWidth,windowHeight, 2, 2);
        g2.drawImage(pinewoodIcon, titleBarX + 5, titleBarY, null);
        // Window Title
        g2.setColor(Color.darkGray);
        g2.drawString("Pinewood Associates", titleBarX + 35, titleBarY + 19);
        g2.setColor(Color.WHITE);
        g2.drawString("Pinewood Associates", titleBarX + 37, titleBarY + 18);
        // forgot password text
        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,16f));
        g2.drawString("forgot password?", titleBarX + 35, (int)(gp.tileSize * 6.5));
        g2.drawString("need help signing in?", titleBarX + 35, gp.tileSize * 7 - 5);

        // Window minimize, maximize, close buttons
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(gp.tileSize * 10 + 24, titleBarY + 2, 20, 20, 2, 2);
        g2.drawRoundRect(gp.tileSize * 10 + 48, titleBarY + 2, 20, 20, 2, 2);
        g2.drawRoundRect(gp.tileSize * 10 + 72, titleBarY + 2, 20, 20, 2, 2);
        g2.setColor(new Color(200, 60,0));
        g2.fillRoundRect(gp.tileSize * 10 + 73, titleBarY + 3, 19, 19, 2, 2);
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.drawString("X", gp.tileSize * 10 + 78, titleBarY + 20);
        g2.drawString("_", gp.tileSize * 10 + 28, titleBarY + 16);
        g2.drawString("_", gp.tileSize * 10 + 52, titleBarY + 7);
        g2.drawString("_", gp.tileSize * 10 + 56, titleBarY + 7);

        g2.drawRect(gp.tileSize * 10 + 53, titleBarY + 7, 10,10);

        // Password button
        int x = gp.tileSize * 7;
        int y = (int)(gp.tileSize * 4.5);
        g2.setColor(new Color(90, 61, 48));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y + 10, gp.tileSize * 3,26);
        g2.setColor(Color.black);
        g2.drawRect(x, y + 10, gp.tileSize * 3 + 1,27);
        g2.drawString("Password:", x, y);
        drawPasswordTyped();

        // Sign in button
        x += 24;
        y = gp.tileSize * 5;
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y + 20, gp.tileSize * 2,26);
        g2.setColor(Color.black);
        g2.drawRect(x, y + 20, gp.tileSize * 2 + 1,27);
        g2.setColor(Color.gray);
        g2.fillRect(x, y + 20, gp.tileSize * 2 + 1,27);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.black);
        g2.drawString("Sign in", x + 25, y + 40);
        if(gp.keyH.inputText.toString().equals("password")) {
            if(gp.mouseH.clickOnSignInBox == true) {

                // go to home page
                osSubState = 1;
            }
        }

        // Sign in icon
        g2.drawImage(signInIcon, x, y - gp.tileSize*3, gp.tileSize * 2, gp.tileSize * 2, null);


//        DEBUG
//        g2.setColor(Color.red);
//        g2.fillRect(passwordButton.x, passwordButton.y, passwordButton.width, passwordButton.height);
//        g2.setColor(Color.red);
//        g2.fillRect(signInButton.x, signInButton.y, signInButton.width, signInButton.height);

    }

    private void drawPasswordTyped() {
        int x = gp.tileSize * 7;
        int y = gp.tileSize * 5;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
        g2.setColor(Color.black);

        //type in box results
        String displayText = gp.keyH.inputText.toString();
        g2.drawString(displayText, x + 2, y + 3);

    }

    private void drawSelectCabinScreen() {

        // Window screen
        int windowX = gp.tileSize * 2;
        int windowY = gp.tileSize;
        int windowWidth = gp.tileSize * 13;
        int windowHeight = gp.tileSize * 9;
        g2.setColor(Color.WHITE);
        g2.fillRect(windowX,windowY,windowWidth,windowHeight);

        // Window Title bar
        int titleBarX = windowX;
        int titleBarY = windowY;
        int titleBarWidth = windowWidth;
        int titleBarHeight = 26;
        g2.setColor(new Color(90, 61, 48));
        g2.fillRect(titleBarX,titleBarY,titleBarWidth,titleBarHeight);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(windowX,windowY,windowWidth,windowHeight, 2, 2);
        g2.drawImage(pinewoodIcon, titleBarX + 5, titleBarY, null);
            // Window minimize, maximize, close buttons
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(gp.tileSize * 13 + 24, titleBarY + 2, 20, 20, 2, 2);
        g2.drawRoundRect(gp.tileSize * 13 + 48, titleBarY+ 2, 20, 20, 2, 2);
        g2.drawRoundRect(gp.tileSize * 13 + 72, titleBarY+ 2, 20, 20, 2, 2);
        g2.setColor(new Color(200, 60,0));
        g2.fillRoundRect(gp.tileSize * 13 + 73, titleBarY+ 3, 19, 19, 2, 2);
        g2.setColor(Color.WHITE);
        g2.drawString("X", gp.tileSize * 13 + 78, titleBarY + 20);
        g2.drawString("_", gp.tileSize * 13 + 27, titleBarY + 16);
        g2.drawString("_", gp.tileSize * 13 + 53, titleBarY + 7);
        g2.drawString("_", gp.tileSize * 13 + 57, titleBarY + 7);
        g2.drawRect(gp.tileSize * 13 + 53, titleBarY + 7, 10,10);


        // Window Title
        g2.setColor(Color.darkGray);
        g2.drawString("Pinewood Associates", titleBarX + gp.tileSize, titleBarY + 19);
        g2.setColor(Color.WHITE);
        g2.drawString("Pinewood Associates", titleBarX + gp.tileSize + 2 , titleBarY + 18);

        g2.drawImage(pinewoodHomePage, windowX + 1, windowY + 24, windowWidth - 2, windowHeight - 25, null);

        // Title of home screen
        int titleTextX = gp.tileSize * 5;
        int titleTextY = (int)(gp.tileSize * 2.5);
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
        g2.drawString("!! Available Cabin Assignments !!", titleTextX, titleTextY);

        // Middle: 3 Containers containing cabin image, status, description, availability status button
        g2.setColor(new Color(90, 61, 48,50));
        int interval = 0;
        for(int i = 0; i < 3; i++) {
            g2.fillRect((int)(gp.tileSize*2.3) + interval, gp.tileSize * 3, gp.tileSize * 4, gp.tileSize * 5);
            interval += gp.tileSize * 4 + 10;
        }
        // cabin images
        g2.drawImage(a1Cabin, (gp.tileSize*3) + 10, gp.tileSize * 4 - 15, gp.tileSize * 2, gp.tileSize * 2, null);
        g2.drawImage(j1Cabin, (gp.tileSize*7) + 20, gp.tileSize * 4 - 15, gp.tileSize * 2, gp.tileSize * 2, null);
        g2.drawImage(k4Cabin, (gp.tileSize*11) + 30, gp.tileSize * 4 - 15, gp.tileSize * 2, gp.tileSize * 2, null);
        // cabin names
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
        g2.setColor(Color.YELLOW);
        g2.drawString("Cabin A1", gp.tileSize*3 + 25, (int)(gp.tileSize * 3.5));
        g2.drawString("Cabin J1", gp.tileSize*7 + 35, (int)(gp.tileSize * 3.5));
        g2.drawString("Cabin K4", gp.tileSize*11 + 45, (int)(gp.tileSize * 3.5));
        // cabin description
        g2.drawString(" surrounded with flowers.",(int)(gp.tileSize*2.3), gp.tileSize * 6 + 10);
        g2.drawString(" next to fish pond.",(int)(gp.tileSize*2.3) + gp.tileSize * 4 + 10, gp.tileSize * 6+ 10);
        g2.drawString(" rangers love this one.",(int)(gp.tileSize*2.3) + ((gp.tileSize * 4 + 10) + (gp.tileSize * 4 + 10)), gp.tileSize * 6+ 10);
        // check in buttons
        g2.setStroke(new BasicStroke(2));
        // unavailable #1
        g2.setColor(Color.red);
        g2.fillRect(gp.tileSize*3 + 10, gp.tileSize*6 + 30, gp.tileSize * 2,27);
        g2.setColor(Color.WHITE);
        g2.drawString("Unavailable", gp.tileSize*3 + 11, gp.tileSize*7 + 3);
        // unavailable # 2
        g2.setColor(Color.red);
        g2.fillRect((gp.tileSize*7) + 20, gp.tileSize*6 + 30, gp.tileSize * 2,27);
        g2.setColor(Color.WHITE);
        g2.drawString("Unavailable", gp.tileSize*7 + 21, gp.tileSize*7 + 3);

        // available # 1
        if(gp.mouseH.clickOnAssignButton == false) {
            g2.setColor(Color.GREEN);
            g2.fillRect((gp.tileSize * 11) + 30, gp.tileSize * 6 + 30, gp.tileSize * 2, 27);
            g2.setColor(Color.WHITE);
            g2.drawString("Assign", gp.tileSize * 11 + 50, gp.tileSize * 7 + 3);
        } else {
            g2.setColor(Color.lightGray);
            g2.fillRect((gp.tileSize * 11) + 30, gp.tileSize * 6 + 30, gp.tileSize * 2, 27);
            g2.setColor(Color.WHITE);
            g2.drawString("Success!", gp.tileSize * 11 + 50, gp.tileSize * 7 + 3);
        }
        // Warning message
        g2.setColor(Color.red);
        g2.drawString("WARNING: MISSING 19YR OLD NEARBY. REPORT SUSPICIOUS ACTIVITY & STAY SAFE!", gp.tileSize * 2 + 30, gp.tileSize * 9);

//        // DEBUG
//        g2.setColor(Color.red);
//        g2.fillRect(assignButton.x, assignButton.y, assignButton.width, assignButton.height);
    }

    private void drawLogBook() {

        // Coordinates
        int x = gp.tileSize * 4;
        int y = gp.tileSize;

        g2.drawImage(logBook, x, y, gp.tileSize * 12, gp.tileSize * 10, null);

        // Text styles
        g2.setColor(Color.black);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));

        // Text - headers
        g2.drawString("Guide:", x + gp.tileSize * 2, y + gp.tileSize + 10);
        g2.drawString("Extra:", x + gp.tileSize * 8, y + gp.tileSize + 10);
        // Horizontal line
        g2.fillRect(x + (int)(gp.tileSize * 1.5), y + gp.tileSize + 20, gp.tileSize * 3, 2);
        g2.fillRect(x + (int)(gp.tileSize * 7.5), y + gp.tileSize + 20, gp.tileSize * 3, 2);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20f));
        // Guide page
        y += 70;
        g2.drawString("1. Tools in toolshed left of cabin", x + gp.tileSize, y + gp.tileSize);
        y += 40;
        g2.drawString("2. Assist & interact with campers", x + gp.tileSize, y + gp.tileSize);
        y += 40;
        g2.drawString("3. Lock entry at 8:30pm", x + gp.tileSize, y + gp.tileSize);
        y += 40;
        g2.drawString("4. Protect wildlife", x + gp.tileSize, y + gp.tileSize);
        y += 40;
        g2.drawString("5. Resupply firewood every friday", x + gp.tileSize, y + gp.tileSize);
        y += 40;
        g2.drawString("6. Do daily check-ins of campsites", x + gp.tileSize, y + gp.tileSize);
        y += 40;
        g2.drawString("7. Keys for front entrance/exit of", x + gp.tileSize, y + gp.tileSize);
        g2.drawString("   camp is at toolshed.. I think", x + gp.tileSize, y + gp.tileSize + 15);

        // Extra page
        y = gp.tileSize * 3 + 20;
        x += gp.tileSize * 5 + 53;
        g2.drawString("- If you're hungry, there's a cozy pizza", x, y);
        g2.drawString("  spot 8 min out. Officer James knows.", x, y + 15);
        y += 55;
        g2.drawString("- Btw, don't piss off Lead officer James..", x, y);
        y += 40;
        g2.drawString("- Always carry a light source cause it", x, y);
        g2.drawString("  gets very dark at night", x, y + 15);
    }

    public void drawExitMapScreen() {

        // Window & Decoration
        int x = gp.tileSize * 3;
        int y = gp.tileSize * 7;
        int x2 = 0;
        int x3 = 0;
        int width = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28));
        // where text will start to draw
        String exitPrompt = "Exit store?";
        x = getXforCenteredText(exitPrompt);
        y += gp.tileSize;
        x2 = getXforCenteredText("Yes");
        x3 = getXforCenteredText("No");


        g2.drawString(exitPrompt, x, y);
        y += 40;
        g2.drawString("Yes", x2, y);
        if(commandNum == 0) {
            g2.drawString(">", x2 - 15, y);
            if(gp.keyH.enterPressed == true) {
//                gp.transitionMap();
                gp.gameState = gp.transitionState;
            }
        }
        y += 40;
        g2.drawString("No", x3, y);
        if(commandNum == 1) {
            g2.drawString(">", x3 - 15, y);
            if(gp.keyH.enterPressed == true) {
                System.out.println("GO BACK");
                gp.gameState = gp.playState;

            }
        }
        gp.keyH.enterPressed = false;
    }

    public void drawGameOverScreen() {

        g2.setColor(new Color(0,0,0, 150));
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80f));

        text = "Game Over";
        g2.setColor(Color.red);
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);
        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50f));
        text = "Try again";
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-(gp.tileSize/2), y);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
                gp.retry();
            }
        }

        // Return to title screen
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-(gp.tileSize/2), y);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.titleState;
                titleScreenState = 0;
                gp.restart();

            }
        }

    }

    public void drawPlayerLife() {

        int x = gp.tileSize;
        int y = gp.tileSize;

        // PLAYER'S HEALTH BAR

        // OUTLINE OF HEALTH BAR
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(x - 2, (y / 2) - 1 , (gp.tileSize * 4) + 4, 23);

        if(gp.player.curLife == 4) {
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(x, y / 2, gp.tileSize * 4, 20);
        }
        if(gp.player.curLife == 3) {

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(x, y / 2, gp.tileSize * 3, 20);

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(x*4, y / 2, gp.tileSize, 20);
        }
        else if(gp.player.curLife == 2) {

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(x, y / 2, gp.tileSize * 2, 20);

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(x*3, y / 2, gp.tileSize*2, 20);
        }
        else if (gp.player.curLife == 1){
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(x, y / 2, gp.tileSize, 20);

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(x*2, y / 2, gp.tileSize*3, 20);
        }
    }

    public void drawCurrentTask() {

        // WINDOW & DECORATION
        int x = (gp.tileSize * 15) + 10;
        int y = gp.tileSize / 2;
        int width = (int) (gp.tileSize * 4.5);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));


        // Draws the amount of checkboxes needed depending on what task we're currently on.
        String[] lines = currentTask[taskIndex].split("\n");
        x += gp.tileSize * 2;
        y += gp.tileSize;
        g2.setStroke(new BasicStroke(2));

        // better solution to stop hard coding checkmarks
        for(int i = 0; i < lines.length; i++) {

            g2.drawString(lines[i], x - 40, y);
            g2.drawRect(x - 75 , y - 22, 28, 28); // draws task amount of squares. EX: 3 = 3 squares
            if(checkmarks[taskIndex][i] == true) {
                g2.drawImage(checkMark, x - 75 , y - 22, 28 , 28, null);
            }
            y += 40;
        }
    }

    public void setTaskList() {
        // use this to add all the tasks that will be displayed in the ui task board
        // set boolean array here and since it's indexes will be aligned with the actual task it'll be less painful
        // later, add an identifier such as 'nl' in between a string to represent you want a line break and not set it as another task.

        int i = 0;
        currentTask[i] = "Get Soda\nGet Chips\nGet bananas";
        checkmarks[i] = new boolean[3]; // 3 subtasks
        i++;

        currentTask[i] = "Pay for items";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Exit the store";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Talk to officer James";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Select your cabin";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Get cabin keys";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Find cabin";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Go to sleep";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Investigate the problem";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Find gate key";
        checkmarks[i] = new boolean[1];
        i++;

        currentTask[i] = "Escape";
        checkmarks[i] = new boolean[1];
        i++;
    }

    public void drawTitleScreen(){

        // DRAW MAIN MENU BACKGROUND IMAGE
        g2.drawImage(menuImage,0, 0, gp.screenWidth, gp.screenHeight, null);

        if(titleScreenState == 0) { // MAIN MENU

            // TITLE AND FONT
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 95F));
            String text = "Pinewood Camp";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*2;

            // SHADOW
            g2.setColor(Color.darkGray);
            g2.drawString(text, x+4, y+4);
            // MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            String text2 = "Stalker";
            int x2 = getXforCenteredText(text2);
            int y2 = gp.tileSize*4;

            // SHADOW
            g2.setColor(Color.darkGray);
            g2.drawString(text2, x2+3, y2+3);
            // MAIN COLOR
            g2.setColor(Color.red);
            g2.drawString(text2, x2, y2);

            // MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
            g2.setColor(Color.white);
            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize*4;
            g2.drawString(text, x, y);
            if(commandNum == 0) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "QUIT";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2) {
                g2.drawString(">", x-gp.tileSize, y);
            }

        }

        else if(titleScreenState == 1) { // choose character menu
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,60F));

            String text = "Select your Character";
            int x = gp.tileSize/2;
            int y = gp.tileSize*2;
            g2.drawString(text, x ,y);

            // PLAYER SELECTION
            text = "Sally";
            x = gp.tileSize;
            y += gp.tileSize*2;
            g2.drawString(text, x+gp.tileSize, y);
            if(commandNum == 0) { // IF ARROW POINTING AT SALLY, DO THE BELOW:
                g2.drawString(">", x, y);
                playerType = 1;
                imgSpriteCounter++; // SWITCH SPRITE VARIATIONS EVERY 12 FRAMES
                if(imgSpriteCounter > 12) {
                    if(imgSpriteNum == 1) {
                        imgSpriteNum = 2;
                    }
                    else if(imgSpriteNum == 2) {
                        imgSpriteNum = 1;
                    }
                    imgSpriteCounter = 0;
                }
                drawSpriteVariation(imgSpriteNum);

            }

            text = "Chad";
            x = gp.tileSize;
            y += gp.tileSize+10;
            g2.drawString(text, x+gp.tileSize, y);
            if(commandNum == 1) {
                g2.drawString(">", x, y);
                playerType = 2;
                imgSpriteCounter++;
                if(imgSpriteCounter > 12) {
                    if(imgSpriteNum == 1) {
                        imgSpriteNum = 2;
                    }
                    else if(imgSpriteNum == 2) {
                        imgSpriteNum = 1;
                    }
                    imgSpriteCounter = 0;
                }
                drawSpriteVariation(imgSpriteNum);
            }

            text = "Back";
            x = gp.tileSize;
            y += gp.tileSize*5;
            g2.drawString(text, x, y);
            if(commandNum == 2) {
                g2.drawString(">", x-gp.tileSize, y);
            }

        }

    }

    // the float parameter is optional, only used if fading text is needed.
    public void drawGlowText(Graphics2D g2, String text, int x, int y) {

        Color glowColor = new Color(57, 255, 20); // neon green

        // Glow layers
        for (int i = 3; i > 0; i--) {
            float alpha = 0.02f * i;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(glowColor);

            g2.drawString(text, x - i, y);
            g2.drawString(text, x + i, y);
            g2.drawString(text, x, y - i);
            g2.drawString(text, x, y + i);

            g2.drawString(text, x - i, y - i);
            g2.drawString(text, x + i, y + i);
            g2.drawString(text, x - i, y + i);
            g2.drawString(text, x + i, y - i);
        }

        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Main bright text
        g2.setColor(new Color(0, 255, 100));
        g2.drawString(text, x, y);
    }

    public void drawIntroDialogueScreen() {

        // Decoration
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
        String currentText = introDialogues[dialogueIndex];
        // lines example : ["hello"], \n ["goodbye"]
        String[] lines = currentText.split("\n");
        int middleX = 0;

        int y = defaultYPosition;

        // Draw all completed lines so they stay on screen.
        // nextLine represents how many lines have finished typing.
        // As nextLine increases, this loop redraws more full lines each frame.
        // Glow Layers:

        // Main Text
        for(int i = 0; i < nextLine; i++) {
            middleX = getXforCenteredText(lines[i]);

            drawGlowText(g2, lines[i], middleX, y);
            y += 40;
        }

        // SKIP DIALOGUE TEXT
        int skipX = gp.tileSize * 16;
        int skipY = gp.tileSize * 11;
        drawGlowText(g2, "ESC TO SKIP", skipX, skipY);


        // Skip dialogue
        if(skipDialogue == true) {
            gp.gameState = gp.playState;
            gp.se.stop();
            gp.stopMusic();
            gp.playMusic(17);
            gp.drawBlackScreen = true;
            skipDialogue = false;
        }
        // Moving text animation
        else {
            // Only do typewriter effect when nextLine(next index) is less than the length. Prevents exception errors.
            if (nextLine < lines.length) {

                String curWord = lines[nextLine].substring(0, wordEnd);
                middleX = getXforCenteredText(curWord);
                drawGlowText(g2, curWord, middleX, introDialogueY);

                // ONLY SHOW LONGER WORD WHEN WE ARE LESS THAN THE WORD LENGTH
                if (wordEnd < lines[nextLine].length()) {
                    wordEnd++;
                }
                // ONCE DRAWN FULL WORD, SET BACK TO ZERO FOR SHOWING THE WORD AND GO TO NEXT INDEX OF SENTENCE.
                else if (wordEnd >= lines[nextLine].length()) {
                    wordEnd = 0;
                    nextLine++;
                    introDialogueY += 40;
                }
            }
            // ONCE FINISHED TYPING THE FULL CURRENT PAGE. WE USE THIS TO ALLOW TO PRESS ENTER
            else {
                finishedTyping = true;
                gp.se.stop();
            }
        }

    }

    public void drawSpriteVariation(int spriteNum) {
        int x = gp.tileSize;
        int y = gp.tileSize*4;

        if(playerType == 1) {

            BufferedImage sallydown1 = null;
            try{
                sallydown1 = ImageIO.read(getClass().getResourceAsStream(  "/girl_player/sally_down1.png"));

            }catch (IOException e){
                e.printStackTrace();
            }

            BufferedImage sallydown2 = null;
            try{
                sallydown2 = ImageIO.read(getClass().getResourceAsStream(  "/girl_player/sally_down2.png"));

            }catch (IOException e){
                e.printStackTrace();
            }

            switch(spriteNum) {
                case 1:
                    g2.drawImage(sallydown1, x*9, y-30, gp.tileSize*7,gp.tileSize*7, null);
                    break;
                case 2:
                    g2.drawImage(sallydown2, x*9, y-30, gp.tileSize*7, gp.tileSize*7, null);
            }
        }
        else if(playerType == 2) {
            switch(spriteNum) {
                case 1:
                    g2.drawImage(gp.player.down1, x*9, y, gp.tileSize*6,gp.tileSize*6, null);
                    break;
                case 2:
                    g2.drawImage(gp.player.down2, x*9, y, gp.tileSize*6, gp.tileSize*6, null);
            }
        }

    }

    public void drawInventory() {

        // FRAME WINDOW
        final int frameX = gp.tileSize * 14;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 4;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOTS
        final int slotXStart = frameX + 20; // from the frameX corner and shift right by 20 pixels
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 4;


        // DRAW PLAYER'S CURRENT ITEMS & SLOT BOX
        for(int i = 0; i < gp.player.inventory.size(); i++) {

            if(i % MAX_COL == 0 && i != 0) { // SINCE 4 IS OUT MAX_COL, ANYTHING PERFECTLY DIVISIBLE BY 4 WITH REMAINDER 0 WILL JUMP TO NEW ROW.
                                            // BETTER PRACTICE THAN JUST COMPARING i TO AN INTEGER IN CASE THERE'S LATER CHANGES. 4, 8, 12, 16 ALL WORKS WITH DIVISION BY 4.
                slotY += slotSize;
                slotX = slotXStart;
            }

            // DRAW SLOT BOX
            Color c = new Color(255, 255, 255);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(8)); // setStroke(new BasicStroke(int)) defines the width of outlines of graphics which are rendered with a Graphics 2D
            g2.fillRoundRect(slotX, slotY, slotSize - 5, slotSize -2, 10, 10);

            // DRAW ITEMS
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX += slotSize;
        }

        // CURSOR
        int cursorX = slotXStart + (slotSize * slotCol); // X AND Y GIVES US THE COORDS OF THE CURSOR BOX
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = (gp.tileSize * 5) + 10;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize *3;

        // DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20));
        String interactItem = " 'G' to drop ";


        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()) {

            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight); // BOX ONLY GETS DRAWN WHEN THE CURSOR IS ON AN ITEM

            g2.setColor(Color.white);
            for(String line : gp.player.inventory.get(itemIndex).description.split("\n")) { // .split CONVERTS THE STRING INTO AN ARRAY OF SUBSTRINGS.

                g2.drawString(line, textX, textY);
                textY += 32; // MOVE DOWN TO THE NEXT LINE
            }

            g2.drawString(interactItem, textX, textY);

        }

    }

    public void drawPausedMenu() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // Window

        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 6;
        if(subState == 2) frameHeight += gp.tileSize * 2;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Switch between different screens of the pause menu.
        switch(subState) {
            case 0: pausedMain(frameX, frameY); break;
            case 1: optionsScreen(frameX, frameY); break;
            case 2: drawControlMenu(frameX, frameY); break;
            case 3: fullScreenNotification(frameX, frameY); break;
            case 4: exitGameConfirmation(frameX, frameY); break;
        }

        // Better to put here than in KeyHandler's keyReleased method to avoid distortedness.
        gp.keyH.enterPressed = false;
    }

    public void pausedMain(int frameX, int frameY) {

        int textX;
        int textY;

        // Title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        String title = "Paused";
        textX = getXforCenteredText(title);
        textY = frameY + gp.tileSize;
        g2.drawString(title, textX, textY);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32));

        // Resume Game
        textX = gp.tileSize*7;
        textY += 48;
        g2.drawString("Resume", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
            }
        }

        // Options
        textY += 32;
        g2.drawString("Options", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
            if(gp.keyH.enterPressed) {
                subState = 1;
                commandNum = 0;
            }
        }

        // Controls
        textY += 32;
        g2.drawString("Controls", textX, textY);
        if(commandNum == 2) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }

        // End Game
        textY += 32;
        g2.drawString("End game", textX, textY);
        if(commandNum == 3) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 4;
                commandNum = 0;
            }
        }


    }

    public void optionsScreen(int frameX, int frameY) {


        // Make text uniform.
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        int textX = getXforCenteredText("Options");
        int textY = frameY + gp.tileSize;

        // Title
        g2.drawString("Options", textX, textY);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28));


        textX = gp.tileSize * 7;
        // Fullscreen
        textY += 32;
        g2.drawString("Full Screen", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
            if(gp.keyH.enterPressed == true) {
                commandNum = 0;
                subState = 3;
                if(gp.toggleFullScreen == false) {gp.toggleFullScreen = true;}
                else { gp.toggleFullScreen = false; }

            }

        }

        // Music
        textY += 32;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
        }

        // SE
        textY += 32;
        g2.drawString("SE", textX, textY);
        if(commandNum == 2) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
        }

        // Back
        textY += 32;
        g2.setColor(Color.ORANGE);
        g2.drawString("Back", textX, textY);
        if(commandNum == 3) {
            g2.drawString(">", textX - gp.tileSize/2, textY);
            if(gp.keyH.enterPressed == true) {
                gp.ui.subState = 0;
                commandNum = 0;
            }
        }

        // Call to draw the buttons to the right of menu
        drawOptionMenuButtons();

        // Every time the optionsScreen method gets called, we save the settings in the saveConfig method.
        gp.config.saveConfig();
    }

    public void drawDialogueScreen() {

        // Trackers
        // keep track of the convo size and only should be displayed when pDialogueIndex < pConvoSize
        int pConvoSize = gp.player.playerDialogues[gp.player.pConvoIndex].length;
        int pSpeakIndex = gp.player.pDialogueIndex;

        // Window & Decoration
        int x = gp.tileSize * 3;
        int y = gp.tileSize * 7;
        int defaultYPosition = y;
        int width = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22));
        x += 15; // where text will start to draw
        y += 38;

        // Draw NPC's dialogue - static version ||||   Draw NPC's Dialogue - typewriter version soon??
        for(String line : currentDialogue.split("\n")) { // split method turns this string into a temporary array
            g2.drawString(line, x, y);
            y += 40;
        }


        // Player's response mechanics
        y += 30;
        // if true, draw player responses - conditional handling is in gamepanel's update method.
        if(gp.player.pDialogueIndex < pConvoSize) {

            String playerLine = gp.player.playerDialogues[gp.player.pConvoIndex][pSpeakIndex];
            x = getXforCenteredText(playerLine);

            // draw two selection arrows - next npc speak & player response logic is in gamepanel's update method.
            if(gp.player.playerDialogues[gp.player.pConvoIndex][pSpeakIndex].contains("\n")) {
                if (commandNum == 0) {
                    g2.drawString(">", x - 20, y);
                }
                if (commandNum == 1) {
                    g2.drawString(">", x - 20, y + 30);
                }
            }
            // draw one selection arrow
            else {
                commandNum = 0;
                g2.drawString(">", x - 20, y);
            }
            // Draw the player's response
            for (String line : playerLine.split("\n")) {
                g2.drawString(line, x, y);
                y += 30;
            }
        }
    }

    public void setIntroArray() {

                                        // INDEX 0
        introDialogues = new String[] {"Hello. I decided it's finally time to open up\n" +
                                       "about a story that happened to me\n"+
                                       "in 2003.\n\n" +
                                       "When I was 19, I had wanted to get\n" +
                                       "away from home.. and I mean far away\n\n" +
                                       "So, I found a job as a park ranger in\n" +
                                       "Pinewood Camp. This is where the story turns",
                                        // INDEX 1
                                       "The first few weeks of work was\n" +
                                       "exciting, I made some good\n" +
                                       "friends and explored the wilderness.\n\n" +
                                       "It was therapeutic coming from\n" +
                                       "a chaotic home.\n" +
                                       " It was good until a specific night shift.",
                                        // INDEX 2
                                       "I will never forget about this moment"};

    }

    public int getItemIndexOnSlot() {
        // GETS CORRECT, CURRENT HOVERED ITEM INDEX. STARTS FROM ZERO.
        return slotCol + (slotRow * MAX_COL);
    }

    public void drawOptionMenuButtons() {


        // Coordinates & styling
        int shapeX = gp.tileSize*11;
        int shapeY = (gp.tileSize*4) + 12;
        g2.setStroke(new BasicStroke(3));
        // Fullscreen toggle button
        g2.drawRect(shapeX, shapeY, 20, 20);
        if(gp.toggleFullScreen == true) {
            g2.fillRect(shapeX, shapeY, 20, 20);
        }

        // Music bar
        shapeY += 32;
        g2.drawRect(shapeX, shapeY, 120, 20); // 120/5 = 24
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(shapeX, shapeY, volumeWidth, 20);


        // SE bar
        shapeY += 32;
        g2.drawRect(shapeX, shapeY, 120, 20);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(shapeX, shapeY, volumeWidth, 20);
    }

    public void drawControlMenu(int frameX, int frameY) {



        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24));
        int textX = (frameX + gp.tileSize) - gp.tileSize/2;
        int textY = (frameY + gp.tileSize) - 12;

        g2.drawString("Move cursor", textX, textY);
        g2.drawString("WASD", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Adjust Volume", textX, textY);
        g2.drawString("A & D", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Pause Game", textX, textY);
        g2.drawString("Esc", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("View Map", textX, textY);
        g2.drawString("M", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Interact", textX, textY);
        g2.drawString("E", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Open / Hide Inventory", textX, textY);
        g2.drawString("C", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Select Item", textX, textY);
        g2.drawString("ENTER", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Confirm / Back", textX, textY);
        g2.drawString("ENTER", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Continue Dialogue", textX, textY);
        g2.drawString("ENTER", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Sprint", textX, textY);
        g2.drawString("SHIFT", textX + gp.tileSize * 5, textY);

        textY += 28;
        g2.drawString("Hide tasks", textX, textY);
        g2.drawString("G", textX + gp.tileSize * 5, textY);

        textY += 48;
        g2.setColor(Color.ORANGE);
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 12, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }

    }

    public void exitGameConfirmation(int frameX,int frameY) {

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32));
        currentDialogue = "Quit game and return\nto main screen?";

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 32;
        }

        textY += 16;
        int middleText = getXforCenteredText("Yes");
        // Yes
        g2.drawString("Yes", middleText, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX+gp.tileSize, textY);
            if(gp.keyH.enterPressed == true) {
                // return to title screen
                subState = 0;
                gp.gameState = gp.titleState;
                titleScreenState = 0;
                gp.restart();
            }
        }

        middleText = getXforCenteredText("Back");

        // Back
        textY += 32;
        g2.setColor(Color.ORANGE);
        g2.drawString("Back", middleText, textY);
        if(commandNum == 1){
            g2.drawString(">", textX+gp.tileSize, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 0;
            }
        }

    }

    public void fullScreenNotification(int frameX, int frameY) {

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32));

        currentDialogue = "To apply changes, \nplease restart the game.";

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 32;
        }

        // Back
        textY += 32;
        g2.setColor(Color.ORANGE);
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-20, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 1;
            }
        }
    }

    // HELPER METHOD
    public void drawSubWindow(int x, int y, int width, int height) {

        // OUTERMOST BORDER
        Color c = new Color(0,0,0, 180);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(2)); // setStroke(new BasicStroke(int)) defines the width of outlines of graphics which are rendered with a Graphics 2D
        g2.drawRoundRect(x, y, width+1, height+1, 5, 5);

        // ACTUAL RECTANGLE
        c = new Color(0,0,0, 250);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 5, 5);

        // INSIDE BORDER
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5)); // setStroke(new BasicStroke(int)) defines the width of outlines of graphics which are rendered with a Graphics 2D
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 5, 5);

    }

    // HELPER METHOD
    public int getXforCenteredText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private void loadBufferedImage() {

        // TITLE SCREEN IMAGE
        try{
            menuImage = ImageIO.read(getClass().getResourceAsStream(  "/titleScreenImage/titleScreen.png"));

        }catch (IOException e){
            e.printStackTrace();
        }

        // FONT
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // LOAD CHECKMARK
        try{
            checkMark = ImageIO.read(getClass().getResourceAsStream(  "/images/checkmark.png"));

        }catch (IOException e){
            e.printStackTrace();
        }

        // LOAD OS ICONS

        try{
            osBackground = ImageIO.read(getClass().getResourceAsStream("/images/windowsxp.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }


        try{
            pinewoodIcon = ImageIO.read(getClass().getResourceAsStream("/images/pinewoodassociates.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            osIcon = ImageIO.read(getClass().getResourceAsStream("/images/osIcon.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            recycleIcon = ImageIO.read(getClass().getResourceAsStream("/images/recyclebin.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            signInIcon = ImageIO.read(getClass().getResourceAsStream("/images/signinicon.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            pinewoodHomePage = ImageIO.read(getClass().getResourceAsStream("/images/pinewoodhomepage.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            a1Cabin = ImageIO.read(getClass().getResourceAsStream("/images/A1cabin.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            j1Cabin = ImageIO.read(getClass().getResourceAsStream("/images/J1cabin.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            k4Cabin = ImageIO.read(getClass().getResourceAsStream("/images/K4cabin.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            folderIcon = ImageIO.read(getClass().getResourceAsStream("/images/foldericon.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            speakerIcon = ImageIO.read(getClass().getResourceAsStream("/images/speakerIcon.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            logBook = ImageIO.read(getClass().getResourceAsStream("/images/rangerlogbook.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
