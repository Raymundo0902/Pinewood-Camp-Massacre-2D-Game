package monster;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class MON_EVILBILL extends Entity {

    GamePanel gp;
    int pathTimer = 0;

    public MON_EVILBILL(GamePanel gp) {
        super(gp);

        this.gp = gp; // did it only on this class since its in a different package than the entity one

        name = "Evil Bill";
        speed = 4;
        maxLife = 4;
        curLife = maxLife;
        type = TYPE_MONSTER;
        sleep = true;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 25;
        solidArea.height = 25;
        solidAreaDefaultX = solidArea.x; // reason we create solidAreaDefaultX,Y is so we can recall the default values of solidArea.x and y because we will change solidArea.x and y later.
        solidAreaDefaultY = solidArea.y;

        getImage();

    }

    public void getImage() { // load and scale monster images

        up1 = setup("/monster/monster_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/monster_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/monster_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/monster_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/monster_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/monster_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/monster_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/monster_right2", gp.tileSize, gp.tileSize);
    }

    public void update() {
        super.update();

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int detectionRange = gp.tileSize * 7;

        if(xDistance <= detectionRange && yDistance <= detectionRange) {
            path = true;
        }
        else { path = false; }
    }

    public void setAction() {

        // For testing, this toggles to true when player takes damage
        if(path == true) {
            // follow player
                int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
                int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

                // Better?
//            int goalCol = (gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width/2) / gp.tileSize;
//            int goalRow = (gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height/2) / gp.tileSize;

                // gets called 60x a second
                System.out.println("CALL SEARCHPATH");
                searchPath(goalCol, goalRow);


        }
        else {
            actionLockCounter++;
            if (actionLockCounter == 120) {

                Random random = new Random();
                int i = random.nextInt(100) + 1; // get random number from 1-100. +1 because it picks from 0-99.

                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }


}
