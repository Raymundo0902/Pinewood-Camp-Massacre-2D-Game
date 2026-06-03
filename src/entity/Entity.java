package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// THIS CLASS STORES VARIABLES THAT WILL BE USED IN PLAYER, MONSTER AND NPC CLASSES.
public class Entity {

    protected GamePanel gp;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, throwLeft1, throwRight1, throwUp1, throwDown1; // BufferedImage describes an Image with an accessible buffer of image data. (we use this to store our image files)
    public boolean collision = false;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // create a invisible or abstract rectangle and store data like x,y width and height. is the default solidArea but can be changed within the children classes
    public Rectangle rakeArea = new Rectangle(0, 0, 0, 0); // rake grass
    public int solidAreaDefaultX, solidAreaDefaultY; // the blueprint and the subclasses will have their own values
    public String dialogues[] = new String[20]; // each entity gets their own copy
    public BufferedImage image;

    // STATE - collision, location, defaults
    public boolean collisionOn = false;
    public boolean invincible = false;
    public int worldX, worldY;
    public int spriteNum = 1;
    public int dialogueIndex = 0;
    public String direction = "down";
    public boolean path = false; // for trackingPath (pathfinding)
    public boolean resetPosition = false; // for resetting npc position
    public boolean interactable = true;
    public boolean sleep = false;
    public boolean drawing = true;

    // COUNTER - sprite animations
    public int spriteCounter = 0;
    public int actionLockCounter = 0; // USUALLY FOR NPC, MAKE THEM LOCK INTO A IMAGE FOR A SPECIFIC AMOUNT OF FRAMES
    public int invincibleCounter = 0; // after taking damage, player becomes invisible for a bit
    public int itemCooldown = 0; // AFTER USING ITEM, COOLDOWN BEFORE USING AGAIN
    public int itemCooldownMax = 60; // AFTER 1 SECONDS USE ITEM AGAIN
    public int spriteThrowCounter = 8; // SHOW THROWING SPRITE ANIMATION FOR 8 FRAMES

    // TYPE
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int TYPE_PLAYER = 0;
    public final int TYPE_NPC = 1;
    public final int TYPE_MONSTER = 2;
    public final int TYPE_RAKE = 3;
    public final int TYPE_KEY = 4;
    public final int TYPE_HANDS = 5;
    public final int TYPE_ROCK = 6;
    public final int TYPE_LIGHT = 7;


    // CHARACTER ATTRIBUTES
    public String name;
    public int maxLife;
    public int curLife;
    public int speed;
    public Entity currentLight;

    // Entity dialogue's array size (the size of the array excluding empty slots)
    public int dialogueSize = 0;

    // ITEM/PROJECTILE ATTRIBUTES
    public Entity currentItem; // store reference to rake, key, flashlight, etc
    public String description = "";
    public Projectile projectile;
    public boolean alive = false;
    public int lightRadius;


    public Entity(GamePanel gp) {
        this.gp = gp;
    }


    public void setAction() {

    }

    public void speak() {

        if(dialogues[dialogueIndex] == null) { // if there's no more text, go back to first dialogue to prevent "NullPointerException"
            System.out.println("RESET");
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void checkCollision() {

        collisionOn = false;
        gp.cChecker.checkTile(this); // if finds wall collision = true
        gp.cChecker.checkObject(this, false); // if finds specific solid object, collision = true
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this); // if hits player, set collision = true

        if(this.type == TYPE_MONSTER && contactPlayer) { attackPlayer(); }
    }

    // Gets called 60x a second when path = true.
    public void searchPath(int goalCol, int goalRow) {
        // dividing by the tile size gives the col/row number.
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search() == true) {
            // store the upcoming coordinates - get(0) because other indexes aren't used since this is called 60x a second. With that,
            // each index zero as the path list changes per frame, so will 0 which will act as the next step.
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            // Entity's solid hitbox position - we need to get them here because there's no variables of calculations of their current hitbox in their normal classes. So we do it here.
            int entityLeftX = worldX + solidArea.x;
            int entityRightX = worldX + solidArea.x + solidArea.width; // the .width adding shifts to the right edge of hit box
            int entityTopY = worldY + solidArea.y;
            int entityBottomY = worldY + solidArea.y + solidArea.height;

            // If match conditions, entity can go up,down,left, or right
            if(entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize) {
                direction = "up";
            }
            else if(entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + gp.tileSize) {
                direction = "down";
            }
            else if(entityTopY >= nextY && entityBottomY < nextY + gp.tileSize) {

                // left or right
                if(entityLeftX > nextX) {
                    direction = "left";
                }
                else if(entityLeftX < nextX) {
                    direction = "right";
                }
            }

            // Cases where entity gets stuck - check for objects as well
            else if(entityTopY > nextY && entityLeftX > nextX) {
                // up or left
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(entityTopY > nextY && entityLeftX < nextX) {
                // up or right
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }
            else if(entityTopY < nextY && entityLeftX > nextX) {
                // Down or left
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(entityTopY < nextY && entityLeftX < nextX) {
                // Down or right
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }


            // THIS IS ONLY FOR WHEN ENTITY PATHFINDING ISN'T TO FOLLOW YOU.
            // If entity reaches the goal, stop the search
            // We compare tiles instead of pixels to have a broader area rather than pinpoint area.
//            int nextCol = gp.pFinder.pathList.get(0).col;
//            int nextRow = gp.pFinder.pathList.get(0).row;
//
//            if(nextCol == goalCol && nextRow == goalRow) {
//                path = false;
//            }
        }

    }



    public void use(Entity entity) { } // USE THIS METHOD IN CHILDREN CLASSES FOR WHEN USING KEYS, DRINKING SOMETHING, ETC

    public void update() {

        if(!sleep) {
            setAction();
            checkCollision();


            // IF COLLISON IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false) {
                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            animate();
            invincible();
        }

    }

    public void animate() {

        spriteCounter++;
        if (spriteCounter > 12) { // this means Entity's image changes every 12 frames

            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void invincible() { // DONT TAKE DAMAGE DURING THIS PERIOD
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void attackPlayer() {

        if(!gp.player.invincible) {
            // GIVE PLAYER DAMAGE
            gp.playSE(11);
            gp.player.curLife -= 1;
            gp.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX; // find out its screenX and Y and if its in the camera frame then draw it
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // player.worldX - player.screenX = left edge of camera.      objectRight > cameraLeft
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && // player.worldX + player.screenX = right edge of camera.    objectLeft < cameraRight
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && // player.worldY - player.screenY = top edge of camera.      objectBottom > cameraTop
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { // player.worldY + player.screenY = bottom edge of camera.   objectTop < cameraBottom

            switch(direction) { // based on this direction we will pick an image from below
                case "up":
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                    break;
                case "down":
                    if(spriteNum == 1) {image = down1;}
                    if(spriteNum == 2) {image = down2;}
                    break;
                case "left":
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                    break;
                case "right":
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                    break;
            }

            if(invincible) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f)); // makes Entity look kinda invincible
            } else{
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // makes Entity back to normal transparency
            }

            g2.drawImage(image, screenX, screenY, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // makes Entity back to normal transparency
            // COLLISION VISUALS (DEBUG)

            g2.setColor(Color.red);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        }

    }

    public BufferedImage setup(String imagePath, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream( imagePath +".png"));
            image = uTool.scaleImage(image, width, height);

        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
