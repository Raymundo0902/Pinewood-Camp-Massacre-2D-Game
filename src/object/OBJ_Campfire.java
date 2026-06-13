package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OBJ_Campfire extends Entity {

    BufferedImage fire1, fire2;
    public int lightRadius;


    public OBJ_Campfire(GamePanel gp) {

        super(gp);

        // DEFAULTS & STATES
        name = "Campfire";
        collision = true;
        type = TYPE_LIGHT;

        fire1 = setup("/objects/fire1", gp.tileSize, gp.tileSize);
        fire2 = setup("/objects/fire2", gp.tileSize, gp.tileSize);
        lightRadius = 150;

    }

    @Override
    public void update() {

        spriteCounter++;
        if(spriteCounter > 30) { // CHANGE ANIMATION EVERY .5 SECONDS

            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX; // find out its screenX and Y and if its in the camera frame then draw it
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // WE ADD/SUBTRACT BY TILESIZE TO HELP PREVENT OBJECTS TO DISAPPEAR EARLY
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // player.worldX - player.screenX = left edge of camera.      objectRight > cameraLeft
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && // player.worldX + player.screenX = right edge of camera.    objectLeft < cameraRight
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && // player.worldY - player.screenY = top edge of camera.      objectBottom > cameraTop
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { // player.worldY + player.screenY = bottom edge of camera.   objectTop < cameraBottom

            switch(spriteNum) {

                case 1: image = fire1; break;
                case 2: image = fire2; break;
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            // COLLISION HITBOX VISUAL (DEBUG)
//            g2.setColor(Color.red);
//            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        }
    }



}
