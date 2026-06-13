package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC_OfficerJames extends Entity{
    public NPC_OfficerJames(GamePanel gp) {
        super(gp);
        direction = "left";
        speed = 2;
        getNPCImage();
        setDialogue();
        name = "james";
        type = TYPE_NPC;

        solidArea = new Rectangle(); // values below are what parts of the character will be solid
        solidArea.x = -50;
        solidArea.y = -10;
        solidAreaDefaultX = solidArea.x; // reason we create solidAreaDefaultX,Y is so we can recall the default values of solidArea.x and y because we will change solidArea.x and y later.
        solidAreaDefaultY = solidArea.y;
        solidArea.width = gp.tileSize;
        solidArea.height = (gp.tileSize * 2);
    }

    public void getNPCImage() {
        left1 = setup("/npc/mainOfficer", gp.tileSize, gp.tileSize + 10);
    }


    public void setDialogue() {
        // Dialogue
        dialogues[0] = "First day here and YOU'RE LATE?!?";
        dialogueSize++;
        dialogues[1] = "I'm just bursting your bubble, calm down lol";
        dialogueSize++;
        dialogues[2] = "Anywho, welcome to Pinewood Camp!";
        dialogueSize++;
        dialogues[3] = "Before giving you the keys to your cabin, sign in to the computer to\nthe top left of you. Password is.. password..";
        dialogueSize++;
        dialogues[4] = "Once you login, you'll select the cabin available and i'll hand you the keys.";
        dialogueSize++;
    }

    @Override
    public void update() {
    }


    // Had to declare its own draw method from entity because the bed is gp.tileSize*2 long so it needs to show bed from its top/bottom edge of camera view
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX; // find out its screenX and Y and if its in the camera frame then draw it
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // player.worldX - player.screenX = left edge of camera.      objectRight > cameraLeft
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && // player.worldX + player.screenX = right edge of camera.    objectLeft < cameraRight
                worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY && // player.worldY - player.screenY = top edge of camera.      objectBottom > cameraTop
                worldY - (gp.tileSize * 2) < gp.player.worldY + gp.player.screenY) { // player.worldY + player.screenY = bottom edge of camera.   objectTop < cameraBottom

            switch(direction) { // based on this direction we will pick an image from below
                case "up":
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left1;}
                    break;
                case "down":
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left1;}
                    break;
                case "left":
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left1;}
                    break;
                case "right":
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left1;}
                    break;
            }

            if(invincible == true) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // makes Entity look kinda invincible
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // makes Entity back to normal transparency
            g2.drawImage(image, screenX, screenY, null);

            // COLLISION VISUALS (DEBUG)
//            g2.setColor(Color.red);
//            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

    }

    public void setAction() {
    }

    public void speak() {
        // DO THIS CHARACTER SPECIFIC STUFF
        super.speak();

    }
}
