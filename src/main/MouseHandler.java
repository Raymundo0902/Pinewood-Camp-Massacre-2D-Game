package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {

    public boolean mouseClicked, clickOnPasswordBox, clickOnSignInBox, clickOnAssignButton;
    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) { // mouse button has been clicked (pressed and released) on a component

        // Convert e coordinates to game world coordinates. e.getX() and e.getY() gives coordinates based on the world screen NOT fullscreen. Thus, why it won't work in fullscreen
        // In a nutshell, comparing screen coords with game coords won't work.
        double scaleX = (double) gp.screenWidth2 / gp.screenWidth;
        double scaleY = (double) gp.screenHeight2 / gp.screenHeight;
        int adjustedX = (int)(e.getX() / scaleX);
        int adjustedY = (int)(e.getY() / scaleY);

        if(gp.gameState == gp.computerState) {
            if (gp.ui.pineWButtonBounds.contains(adjustedX, adjustedY)) {
                clickOnPasswordBox = false;
                clickOnSignInBox = false;
                mouseClicked = true;
                gp.playSE(12);
            } else if (gp.ui.exitButton.contains(adjustedX, adjustedY)) {
                clickOnPasswordBox = false;
                clickOnSignInBox = false;
                mouseClicked = true;
                gp.gameState = gp.playState;
                gp.playSE(12);
            } else if (gp.ui.passwordButton.contains(adjustedX, adjustedY)) {
                clickOnPasswordBox = true;
                clickOnSignInBox = false;
                mouseClicked = true;
                gp.playSE(12);
            } else if (gp.ui.signInButton.contains(adjustedX, adjustedY)) {
                clickOnPasswordBox = false;
                clickOnSignInBox = true;
                mouseClicked = true;
                gp.playSE(12);
            } else if(gp.ui.assignButton.contains(adjustedX, adjustedY)) {
                if(gp.ui.osSubState == 1) {
                    clickOnAssignButton = true;
                }
            }
            // DEBUG
//            System.out.println(clickOnAssignButton);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { // pressed on component/hold down a mouse button


    }

    @Override
    public void mouseReleased(MouseEvent e) { // mouse button has been released

    }

    @Override
    public void mouseEntered(MouseEvent e) { // invoked when mouse enters a component like the area of a box


    }

    @Override
    public void mouseExited(MouseEvent e) { // when mouse exits the area of a component
   

    }
}
