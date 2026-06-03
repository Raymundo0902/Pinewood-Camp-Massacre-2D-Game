package monster;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class MON_EVILBILL extends Entity {

    GamePanel gp;
    int invisibleLock = 0;
    int randNum = 0;
    Random rand = new Random();
    boolean cloak = false;


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
        int detectionRange = gp.tileSize * 10;

        // detection range for chase (pathfinding)
        if(xDistance <= detectionRange && yDistance <= detectionRange) {
            path = true;
            if(!sleep) { // only activates when monster is actually awake and active.
                gp.chaseMusic = true;
                gp.stopChaseMusic = false;
            }
        }
        else {
            path = false;
            if(!sleep) {
                gp.stopChaseMusic = true;
                gp.chaseMusic = false;
            }
        }

        invisibilityCloak();
    }

    public void setAction() {

        if(path) {

            int tileDx = (worldX - gp.player.worldX) / gp.tileSize;
            int tileDy = (worldY - gp.player.worldY) / gp.tileSize;


            // FAR AWAY = USE A* (use tile based here)
            if(Math.abs(tileDx) > 1 || Math.abs(tileDy) > 1) {
//                System.out.println(Math.abs(tileDx) + " " + Math.abs(tileDy));
                System.out.println("GO TO PLAYER");
                // CHASE PLAYER
                chasePlayer();
            }
            else {
                System.out.println("TOGGLE LOCK");
                // DISABLE PATHFINDING HERE TO MITIGATE JITTERING WHEN CLOSE TO PLAYER.
                // LOCK ON PLAYER (use pixel based here)

                int dx = gp.player.worldX - worldX;
                int dy = gp.player.worldY - worldY;

                // only move if not already overlapping
                if(Math.abs(dx) > 4 || Math.abs(dy) > 4) {
                    if(Math.abs(dx) >= Math.abs(dy)) {
                        direction = dx > 0 ? "right" : "left";
                    } else {
                        direction = dy > 0 ? "down" : "up";
                    }

                    // if that direction is blocked, fall back to A*
                    checkCollision();
                    if(collisionOn) {
                        chasePlayer();
                    }
                }
            }
        }

        // WANDER
        else {

            actionLockCounter++;
            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; // get random number from 1-100. +1 because it picks from 0-99.

                if (i <= 25) direction = "up";
                if (i > 25 && i <= 50) direction = "down";
                if (i > 50 && i <= 75) direction = "left";
                if (i > 75 && i <= 100) direction = "right";
                actionLockCounter = 0;
            }
        }
    }

    private void chasePlayer(){
        // gets called 60x a second
//                System.out.println("CALL SEARCHPATH");
        int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
        int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
        searchPath(goalCol,goalRow);
    }

    private void invisibilityCloak() {

        // do run / invisibility abilities every 10+ seconds.
        if(path) {
            // only when there's no lock activated.
            if(!cloak) {
                randNum = rand.nextInt(240);
                if(randNum == 120) {
                    cloak = true;
                    System.out.println("ACTIVATE CLOAK");
                }
            }
            else{
                invisibleLock++;
                // do invisibility cloak here
                invincible = true;
                speed = 6;
                if(invisibleLock >= 40) {
                    invisibleLock = 0;
                    invincible = false;
                    randNum = 0;
                    cloak = false;
                    speed = 4;
                }
            }
        }
        else {
            if(cloak)
            invisibleLock++;
            // do invisibility cloak here
            invincible = true;
            speed = 6;
            if(invisibleLock >= 40) {
                invisibleLock = 0;
                invincible = false;
                randNum = 0;
                cloak = false;
                speed = 4;
            }
        }
    }


}
