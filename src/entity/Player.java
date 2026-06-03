package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;
import tasks.TaskState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{

    KeyHandler keyH; // object reference variable
    public final int screenX; // screenX and Y indicate where we draw player on the screen and never change since its final. player always will be in the center of the camera.
    public final int screenY;
    public int hasKey = 0;
    public int hasRock = 0;
    int standCounter = 0;
    int sprintCounter = 0; // 2 seconds of sprinting till no more stamina
    public BufferedImage rakeUp1, rakeDown1, rakeRight1, rakeLeft1;
    public boolean rakeCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 12;
    public Entity defaultCurrentItem;


    // ITEM ENABLEMENT & INTERACTION - ONLY PLAYER WILL USE SO WE PUT IT HERE INSTEAD OF ITS PARENT
    public boolean rakeSelect = false; // THIS WILL ONLY ALLOW PLAYER TO USE RAKE WHEN IT HAS BEEN SELECTED FROM INVENTORY
    boolean raking = false;
    boolean throwingRock = false;
    public boolean itemDrop = false;
    public boolean lockOfficeDoor = false;

    // TASK MANAGER - Preventing spam, rolling over tasks
    public boolean gotChips = false;
    public boolean gotDrink = false;
    public boolean gotBanana = false;
    public int snacksCollected = 0;
    public final int totalSnacks = 3;

    // Helpers, extra conditional handling for dialogues - call setDialogue() when transitioning to different map
        // For gas station map
    public final int cashierIndex = 0;
    public final int gasStationNpcIndex = 1;
    public final int melissaIndex = 2;

    // For Pinewood camp map
    public final int aydenIndex = 0;
    public final int mainOfficer = 1;
    public final int chadIndex = 2;
    public final int geraldIndex = 3;

    // EXTRA BOOLEANS
    public boolean lightUpdated = false;
    public boolean exitMap = true; // true for testing but final should be default as false
    public boolean slept = false;
    public boolean freezePlayer = false;
    public boolean interactableCollision = false;
    public boolean isStill = false;
    public boolean unlockedGate = false;

    // PLAYER DIALOGUE SYSTEM
    public int pDialogueIndex = 0;
    public int pInnerDialogueIndex = 0;
    public int pConvoIndex = 0;
    public String[][] playerDialogues = new String[20][]; // holds 20 convos
    public int speakIncrement = 0; // Incremented when so the next time it matches for current setting
    public int speakTimer = 0; // how long inner voice stays on screen



    public Player(GamePanel gp, KeyHandler keyH) { // SAME AS (gamePanel Reference, keyH Reference)

        super(gp); // calling the constructor of the superclass of this class -- and passing this gp.
        this.keyH = keyH; // now points to the same KeyHandler object

        screenX = gp.screenWidth/2 - (gp.tileSize/2); // these two return the halfway point of the screen. subtract a half tile length from both screenX and screenY
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(); // values below are what parts of the character will be solid
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x; // reason we create solidAreaDefaultX,Y is so we can recall the default values of solidArea.x and y because we will change solidArea.x and y later.
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        // rakeArea larger values = larger range of rake
        rakeArea.width = 36;
        rakeArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerRakeImage();
        getPlayerThrowImage();
        setItems();
        setDialogue();
    }

    public void setDefaultValues() {
        setDefaultPositionGasStation();
        speed = 20;
        type = TYPE_PLAYER;

        // PLAYER STATUS
        maxLife = 50; // 2 lifes = one full heart
        curLife = maxLife; // players current life
        currentItem = new OBJ_Hands(gp);
        defaultCurrentItem = currentItem;
    }

    // Helpful to use when retrying after "Game Over"
    public void setDefaultPositionGasStation() {
        worldX = gp.tileSize * 44;
        worldY = gp.tileSize * 58;
        direction = "down";
    }
    public void setDefaultPositionPinewood() {
        worldX = gp.tileSize * 40;
        worldY = gp.tileSize * 62;
        direction = "down";
    }
    public void setPosAfterOffice() {
        worldX = gp.tileSize * 42;
        worldY = gp.tileSize * 64;
        direction = "down";
    }

    public void setPosAfterCabin() {
        worldX = gp.tileSize * 14;
        worldY = gp.tileSize * 16;
        direction = "down";
    }
    public void setPosInCabin() {
        worldX = gp.tileSize * 18;
        worldY = gp.tileSize * 16;
    }

    public void restoreLifeAndAttributes() {

        curLife = maxLife;
        currentItem = defaultCurrentItem;
        invincible = false;
    }

    // ITEMS THAT SHOULD ALREADY BE IN INVENTORY AT START OF GAME GOES HERE
    public void setItems() {
        inventory.add(new OBJ_Map(gp));
    }

    public void setDialogue() {
        // Use for internal dialogue
        dialogues[0] = "I need to find the entrance key...\nand escape like now!";
        dialogues[1] = "n/a";
        dialogues[2] = "n/a";
        dialogues[3] = "n/a";

        if(gp.currentMap == gp.GAS_STATION) {

            // Responses - 2d array - have it where player checks which npcIndex it is and return back the convo for that specific npc.
            // for cashier
            playerDialogues[cashierIndex] = new String[]{"[pay]", "Yeah just started at Pinewood Camp", "I see..\nYou think it's cool to try to scare me??", "Thanks\n[exit]"};
            // for npc in gas station - replace gasStationNpcIndex npc with a different npc. its currently using ayden which is only for pinewood camp.
            playerDialogues[gasStationNpcIndex] = new String[]{"What you up to?", "Nice, I like those pants man", "When did I ask.. LOL", "[exit]"};

            playerDialogues[melissaIndex] = new String[]{"What's this about?", "Do you know any useful info?", "I haven't heard much about the killer..", "Eugh, that's creepy.. Well then."};
        }
        else if(gp.currentMap == gp.PINEWOOD_CAMP) {
            // set different stuff - overlap the playerDialogues indexes with a new response for convos for ex (notice aydenIndex is = 0 like cashierIndex so we're just overidding:
             playerDialogues[aydenIndex] = new String[] {"hey!", "where at?"};
            playerDialogues[mainOfficer] = new String[] {"Im sorry...", "okay", "i see", "great", "got it",};

        }
    }

    @Override
    public void speak() {
        // use a sub state so player can still move while showing inner dialogue.
        speakTimer++;

        if(speakTimer < 180) {

        }
    }

    // Gets the correct player response for the current npc player is interacting with.
    public void getResponseForNpc() {
        pConvoIndex = gp.ui.npcIndex;
    }

    public void getPlayerImage() {

        up1 = setup("/girl_player/sally_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/girl_player/sally_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/girl_player/sally_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/girl_player/sally_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/girl_player/sally_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/girl_player/sally_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/girl_player/sally_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/girl_player/sally_right2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerRakeImage() {

        rakeUp1 = setup("/girl_player/sally_rake_up1", gp.tileSize, gp.tileSize*2);
        rakeDown1 = setup("/girl_player/sally_rake_down1", gp.tileSize, gp.tileSize*2);
        rakeLeft1 = setup("/girl_player/sally_rake_left1", gp.tileSize*2, gp.tileSize);
        rakeRight1 = setup("/girl_player/sally_rake_right1", gp.tileSize*2, gp.tileSize);
    }

    public void getPlayerThrowImage() {

        throwLeft1 = setup("/girl_player/throwRockLeft1", gp.tileSize, gp.tileSize);
        throwRight1 = setup("/girl_player/throwRockRight1", gp.tileSize, gp.tileSize);
        throwUp1 = setup("/girl_player/throwRockUp1", gp.tileSize, gp.tileSize);
        throwDown1 = setup("/girl_player/throwRockDown1", gp.tileSize, gp.tileSize);
    }

    public void update() { // this method gets called 60x per second

        if(raking) { // bypass the else if key inputs if player is currently raking
            raking();
        }

        // without this, player will move without stopping && enterPressed here is for the purpose of checking npc collision when we just press enter key for dialogue without having to simultaneously move to npc and press enter.
        else if (keyH.upPressed || keyH.downPressed ||
                keyH.leftPressed || keyH.rightPressed ||
                keyH.ePressed){

            isStill = false;

            if(keyH.upPressed) {
                direction = "up";
            }
            else if(keyH.downPressed) {
                direction = "down";
            }
            else if(keyH.leftPressed) {
                direction = "left";
            }
            else if(keyH.rightPressed) {
                direction = "right";
            }

            // SPRINTING MECHANICS
            if(keyH.shiftPressed) {
                sprintCounter++;
                if(sprintCounter < 180) { // if sprintCounter is less than 3 seconds then sprint
                    speed = 6;
                }
                else if (sprintCounter > 180 && sprintCounter < 360){ // BE TIRED FOR 3 SECONDS
                    speed = 4; // STAYS AT NORMAL SPEED DURING TIRED DURATION
                }
                else if (sprintCounter > 360) { // ONCE PLAYER RESTED FOR 3 SECONDS, START RUNNING
                    sprintCounter = 0;
                }
            }
            else { // IF SHIFT KEY NOT BEING PRESSED PLAYER SHOULD STILL BE RESTING
                if(sprintCounter > 0) { // MAKES SURE SPRINT COUNTER < 0 BECAUSE THAT WOULD BREAK THE SPRINT AND TIRED PERIODS
                    sprintCounter--;
                }
                speed = 20;
            }

            // CHECK TILE COLLISION - collisionOn = false  will be set to true if in the collision methods above detect collision.
            collisionOn = false;
            gp.cChecker.checkTile(this); // since player class is child to its parent class "Entity" checkTile receives this player class as an entity

            // THE VARIABLES THAT HAVE "Index" IN THEIR NAME ARE TO RETRIEVE THE ENTITIES/OBJECT INDEX SO WE CAN USE TO INTERACT, RECEIVE DAMAGE, DO A SPECIFIC EVENT, ETC
            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            interactObject(objIndex);

            // CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monIndex);

            // CHECK OBJ/NPC COLLISION FOR INTERACT BUTTON
            objectInteractable(objIndex, npcIndex);

            // CHECK EVENT COLLISION - (DELETE THIS COMMENT IN PARENTHESIS ONCE YOU ACTUALLY ADD LOGIC TO EVENTH)
            gp.eventH.checkEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE AND WITHOUT THE ENTERPRESSED HERE, PLAYER CAN MOVE WHEN PRESSING ENTER.
            if(!collisionOn && !keyH.ePressed && !freezePlayer) {
                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // statement 1
            if(currentItem.type == TYPE_RAKE) {
                if (keyH.ePressed && !rakeCanceled) {
                    gp.playSE(9); // swinging rake SE
                    raking = true;
                }
            }
            rakeCanceled = false;

            if(!gp.keyH.ePressed) { // stops player to animate when holding down enter key
                spriteCounter++;
                if (spriteCounter > 12) { // this means player image changes every 12 frames
                    if (spriteNum == 1) spriteNum = 2;
                    else if (spriteNum == 2) spriteNum = 1;
                    spriteCounter = 0;
                }
            }
            // RESET IT TO FALSE OR ELSE PLAYER WILL FREEZE BECAUSE IF ENTER WAS PRESSED, THE IF STATEMENT "1" WON'T EVER PASS AGAIN CAUSING PLAYER TO FREEZE.
            gp.keyH.ePressed = false;
        }

        // If player is standing still
        else {

            isStill = true;

            standCounter++; // this starts to increment by one once there's no wasd or arrow key detection.

            if(standCounter == 20) { // standCounter and this if statement helps stop awkward reset when its making the sprite switchover animation--
                spriteNum = 1;      //  looks natural resetting back to normal position. gives it 19 frames to stay on the animation before resetting back to normal sprite state
                standCounter = 0;
            }

            // Check to make sure we remove interact UI text for idle state
            int objIndex = gp.cChecker.checkObject(this, true);
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            objectInteractable(objIndex, npcIndex);
        }

        if (gp.keyH.throwPressed && itemCooldown == 0 &&
            hasRock > 0 && currentItem.type == TYPE_ROCK) { // START COOLDOWN

            throwingRock = true;
            spriteThrowCounter = 16;
            itemCooldown = itemCooldownMax;
            hasRock--;
            Projectile projectile = new OBJ_Rock(gp);

            projectile.setInfo(worldX, worldY, direction);
            gp.projectileList.add(projectile); // PROJECTILE STORES REFERENCE TO OBJ_ROCK ADDRESS IN HEAP

            inventory.remove(currentItem);
            // RESET BACK TO HANDS TO AVOID THROWING THE OBJECT ROCK THAT WE ALREADY REMOVED FROM INVENTORY
            currentItem = defaultCurrentItem;
        }
        // STOPS YOU FROM SPAMMING THROWS
        if(itemCooldown > 0) itemCooldown--;
        // PREVENTS FROM HOLDING DOWN THE THROW SPRITE ANIMATION WITH F KEY - NEED TO ONLY START DECREASING WHEN F PRESSED
        if(spriteThrowCounter > 0) spriteThrowCounter--;
        else throwingRock = false;

        // this needs to be outside key statement so counter increase even when player isn't moving
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        // Game over
        if(curLife <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    public void takeDamage() {
        curLife--;
    }

    public void raking() {

        spriteCounter++;

        if(spriteCounter <= 5) { // peak of raking image (if want more detailed images)
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) { // full raking


            spriteNum = 1; // set to 2 if you have more sprite raking animations

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the rakeArea or else it will need player collision to cut grass even if tip of rake is already colliding with grass.
            switch(direction) {
                case "up": worldY -= rakeArea.height; break; // offset players worldX/Y by the rakeArea
                case "down": worldY += rakeArea.height; break;
                case "left": worldX -= rakeArea.width; break;
                case "right": worldX += rakeArea.width; break;
            }
            // Modify players solidArea to the rakeArea and then check the grass object collision
            solidArea.width = rakeArea.width;
            solidArea.height = rakeArea.height;
            // CHECK GRASS COLLISION WITH THE UPDATED WORLDX, WORLDY AND SOLIDAREA. DEBUG WITH THE CUT GRASS METHOD SO IT CAN WORK
            int objectIndex = gp.cChecker.checkObject(this, true); // left off here
            cutGrass(objectIndex);

            // RESETS PLAYER BACK TO CURRENT COORDINATES OR ELSE PLAYER WILL GO FLYING AROUND
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if(spriteCounter > 25) { // reset back to no raking
            spriteNum = 1;
            spriteCounter = 0;
            raking = false;
        }

    }

    // Update boolean - set to true if colliding with npc or objects below.
    public void objectInteractable(int objIndex, int npcIndex) {

        if(objIndex != 999) {
            if (gp.obj[objIndex] instanceof OBJ_Fridge || gp.obj[objIndex] instanceof OBJ_CabinDesk ||
                gp.obj[objIndex] instanceof OBJ_SnackShelf || gp.obj[objIndex] instanceof OBJ_GlassDoor ||
                gp.obj[objIndex] instanceof OBJ_Bed || gp.obj[objIndex] instanceof OBJ_FruitBox2 ||
                gp.obj[objIndex] instanceof OBJ_Chest || gp.obj[objIndex] instanceof OBJ_Desk ||
                gp.obj[objIndex] instanceof OBJ_CheckoutCounter) {
                if(gp.obj[objIndex].interactable) {
                    interactableCollision = true;
                }
            }
        }
        else if(npcIndex != 999) {
                if(gp.npc[npcIndex] instanceof NPC_OfficerJames || gp.npc[npcIndex] instanceof NPC_Melissa ||
                   gp.npc[npcIndex] instanceof NPC_Ayden) {
                    if(gp.npc[npcIndex].interactable) {
                        interactableCollision = true;
                    }
                }
        }
        // no obj/npc detected
        else {
            interactableCollision = false;
        }
    }

    public void pickUpObject(int i) {

        if(i != 999) { // if this index is 999 then that means we didn't touch any object. Otherwise, then we did touch an object. the reason for 999 is to make sure its not used by the object array's index

            String objectName = gp.obj[i].name; // refers to the index's string name from each obj subclass

            switch(objectName) { // objectName is the one being evaluated at which must be one of the following below. e.g. Key, Door, Chest, Boots, etc

                case "Rake":
                    gp.playSE(15); // find a better sound like pickup a tool sound effect
                    gp.obj[i] = null;
                    if(inventory.size() != maxInventorySize) {
                        inventory.add(new OBJ_Rake(gp));
                    }
                    break;

                case "Rock":
                    gp.playSE(15);
                    hasRock++;
                    gp.obj[i] = null;
                    if(inventory.size() != maxInventorySize) {
                        inventory.add(new OBJ_Rock(gp));
                    }
                    break;

                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    if(inventory.size() != maxInventorySize) {
                        inventory.add(new OBJ_Key(gp));
                    }
                    break;

                case "Door":
                    if(hasKey > 0 && currentItem.type == TYPE_KEY) { // MUST HAVE SELECTED KEY TO OPEN FROM INVENTORY

                        inventory.remove(currentItem); // GOES THROUGH FOR LOOP AND CHECKS IF THIS REFERENCE POINTS TO SAME OBJECT
                        currentItem.use(this);

                        currentItem = defaultCurrentItem; // SET BACK TO HANDS
                        gp.playSE(4);
                        gp.obj[i] = null; // DISAPPEAR FROM MAP
                        hasKey--; // MAKES IT WHERE I CANNOT OPEN DOORS IF hasKey < 1
                    }
                    else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;

                case "Door2": // horizontal door
                    // only can leave door and never go back in.
                    if(gp.currentTask == TaskState.GO_TO_CABIN) {
                        if (lockOfficeDoor == false) {
                                exitMap = true;
                                gp.gameState = gp.transitionState;
                                lockOfficeDoor = true; // prevents going back in ranger office
                                gp.playSE(4);
                        }
                    }
                    break;

                case "k4door":
                    // enter player's cabin
                    if(gp.currentTask == TaskState.GO_TO_CABIN) {
                        hasKey--;
                        // remove key
                        removeFromInventory(OBJ_Key.class);
                        gp.currentTask = TaskState.GO_TO_SLEEP;
                    }
                    exitMap = true;
                    gp.gameState = gp.transitionState;
                    gp.playSE(20);
                    break;
            }
        }
    }

    public void interactObject(int i) {

        if(i != 999 && gp.obj[i] != null) {

            String objName = gp.obj[i].name;

            if(keyH.ePressed) {
                switch (objName) {

                    case "snackShelf":
                        // Prevents grabbing snack when facing back of shelf
                        if(gp.currentTask == TaskState.GET_SNACKS) {

                            if(!gotChips) {
                                for(int e = 0; e < gp.obj.length; e++) {
                                    if(gp.obj[e] instanceof OBJ_SnackShelf) {
                                        gp.obj[e].interactable = false;
                                    }
                                }
                                gp.playSE(18);
                                snacksCollected++;
                                gotChips = true; // stops spam
                                gp.ui.checkmarks[0][1] = gotChips;
                                checkSnackCompletion();
                            }
                        }
                        break;
                    case "fruitBox2":
                        if(gp.currentTask == TaskState.GET_SNACKS) {
                            if(!gotBanana) {
                                gp.playSE(18);
                                gp.obj[i].interactable = false;
                                snacksCollected++;
                                gotBanana = true;
                                gp.ui.checkmarks[0][2] = gotBanana;
                                checkSnackCompletion();
                            }
                        }
                        break;
                    case "fridge1":
                        if(gp.currentTask == TaskState.GET_SNACKS) {
                            if(!gotDrink) {
                                gp.playSE(18);
                                for(int e = 0; e < gp.obj.length; e++) {
                                    if(gp.obj[e] instanceof OBJ_Fridge) {
                                        gp.obj[e].interactable = false;
                                    }
                                }
                                snacksCollected++;
                                gotDrink = true;
                                gp.ui.checkmarks[0][0] = gotDrink;
                                checkSnackCompletion();
                            }
                        }
                        break;
                    case "glassDoor":
                        if(gp.currentTask == TaskState.EXIT_STORE) {
                            gp.playSE(12);
                            exitMap = true;
                            gp.gameState = gp.transitionMapState;
                        }
                        break;
                    case "computer":
                        gp.playSE(19);
                        gp.gameState = gp.computerState;
                        break;
                    case "bed":
                        if(gp.currentTask == TaskState.GO_TO_SLEEP) {
                            slept = true;
                            gp.obj[i].interactable = false;
                            gp.ui.checkmarks[6][0] = true;
                            gp.gameState = gp.transitionState;
                            gp.currentTask = TaskState.INVESTIGATE;
                        }
                        break;
                    case "logbook":
                        gp.gameState = gp.logBookState;
                        break;
                    case "gate2":
                        if(hasKey > 0) {
                            hasKey--;
                            unlockedGate = true;
                        }
                        break;
                }
            }
        }
    }

    public void interactNPC(int i) {

        if(gp.keyH.ePressed) {

            System.out.println(gp.currentTask);
            if (i != 999) { // from the method that has the default index val, it only will change from 999 if collision was detected - NPC to Player
                rakeCanceled = true;
                String name = gp.npc[i].name;

                switch(name) {

                    case "ayden":
                    case "melissa":
                        gp.ui.npcIndex = i;
                        getResponseForNpc();
                        gp.npc[i].speak();
                        gp.playSE(12);
                        gp.gameState = gp.dialogueState;
                        break;
                    case "cashier":
                        if(gp.currentTask == TaskState.TALK_TO_CASHIER) {
                            for(int e = 0; e < gp.obj.length; e++) {
                                if(gp.obj[e] instanceof OBJ_CheckoutCounter) {
                                    gp.obj[e].interactable = false;
                                }
                            }
                            gp.ui.npcIndex = i;
                            getResponseForNpc();
                            gp.npc[i].speak();
                            gp.playSE(12);
                            gp.gameState = gp.dialogueState;
                            gp.currentTask = TaskState.EXIT_STORE;
                        }
                        break;
                    case "james":
                        if(gp.currentTask == TaskState.CHECK_IN_FRONT_OFFICE || gp.currentTask == TaskState.GO_TO_COMPUTER) {
                            gp.ui.npcIndex = i;
                            getResponseForNpc();
                            gp.npc[i].speak();
                            gp.playSE(12);
                            gp.gameState = gp.dialogueState;
                            if(gp.currentTask == TaskState.CHECK_IN_FRONT_OFFICE) {
                                gp.currentTask = TaskState.GO_TO_COMPUTER;
                            }
                        }
                        else if(gp.currentTask == TaskState.GET_CABIN_KEYS) {

                            updateDialogue(i);
                            gp.ui.npcIndex = i;
                            getResponseForNpc();
                            gp.npc[i].speak();
                            gp.playSE(12);
                            gp.gameState = gp.dialogueState;

                            gp.npc[i].interactable = false;
                            gp.currentTask = TaskState.GO_TO_CABIN;
                            hasKey++;

                            if(inventory.size() != maxInventorySize) {
                                inventory.add(new OBJ_Key(gp));
                                inventory.add(new OBJ_Lantern(gp)); // update dialogue to "also giving you a lantern since its dark out."
                            }
                        }
                        break;
                }
            }
        }
    }

    // Use when you cannot use inventory.remove(currentItem)
    private void removeFromInventory(Class<?> itemClass) {
        for(int x = 0; x <= inventory.size(); x++) {
            if(itemClass.isInstance(inventory.get(x))){
                inventory.remove(x);
                break;
            }
        }
    }

    // used to update player's 2d dialogue array & npc's dialogue. makes it memory efficient
    private void updateDialogue(int i) {
        playerDialogues[mainOfficer] = new String[] {"alright cool", "where at", "sounds easy", "okay then"};
        gp.npc[i].dialogues = new String[] {"Alright ma'am, you're all set-- here's your keys.", "And remember, you may not make it out alive....", "HAHAHA THE LOOK ON YOUR FACE! it's just a joke...", "Also, make sure to lock the main gate at 8:00pm\nthe keys are usually in one of the 4 tool sheds here..", ""};
    }


    public void checkSnackCompletion() {
        if(snacksCollected == totalSnacks) {
            gp.currentTask = TaskState.TALK_TO_CASHIER;
            // enable because that's when grabbing all snacks, we can go checkout and interact with cashier.
            for(int e = 0; e <= gp.obj.length; e++) {
                if(gp.obj[e] instanceof OBJ_CheckoutCounter) {
                    gp.obj[e].interactable = true;
                    break;
                }
            }
        }
    }

    public void contactMonster(int i) {

        if (i != 999) {
            if(!invincible) {
                curLife -= 1;
                invincible = true;
            }
        }
    }

    // DEBUG THIS SO IT WORKS GIVES ILLEGAL EXCEPTION
    public void cutGrass(int i) {

        if (i != 999 && gp.obj[i] instanceof OBJ_TallGrass) {
            if(!gp.obj[i].invincible) {// put cutting grass functions here, like it'll take 3 attacks from rake to dissapear

                gp.playSE(10); // enter grass cutting sound here
                gp.obj[i].curLife -= 1;
                gp.obj[i].invincible = true;

                if(gp.obj[i].curLife <= 0) {
                    gp.obj[i] = null;
                }
            }
        }
    }

    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()) { // NOT SELECTING VACANT SLOTS

            Entity selectedItem = inventory.get(itemIndex); // stores a reference to the current object. could be the reference to the Key, Rake, ... object

            if(selectedItem.type == TYPE_KEY || selectedItem.type == TYPE_ROCK) currentItem = selectedItem;

            if(selectedItem.type == TYPE_RAKE) currentItem = selectedItem;

            if(selectedItem.type == TYPE_LIGHT) {
                if(currentLight == selectedItem) {
                    currentLight = null;
                }
                else{
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }

        }

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        // sprite is drawn at top left since shifting player only happens during left or up raking animations. To fix, we offset the draw position.
        // technically the engine still makes the shift but we offset so it looks like theres no visual shifting going on.
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction) { // based on this direction we will pick an image from below
            case "up":
                if(!raking) {
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                }
                if(raking) {
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1) {image = rakeUp1;}
                }
                if(throwingRock) { // USE CONDITION THAT HAS A BOOLEAN VARIABLE THAT DEPENDS ON TIME
                    if(spriteNum == 1) {image = throwUp1;}
                }
                break;
            case "down":
                if(!raking) {
                    if(spriteNum == 1) {image = down1;}
                    if(spriteNum == 2) {image = down2;}
                }
                if(raking) {
                    if(spriteNum == 1) {image = rakeDown1;}
                }
                if(throwingRock) { // USE CONDITION THAT HAS A BOOLEAN VARIABLE THAT DEPENDS ON TIME
                    if(spriteNum == 1) {image = throwDown1;}
                }
                    break;
            case "left":
                if(!raking) {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                }
                if(raking) {
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1) {image = rakeLeft1;}
                }
                if(throwingRock) { // USE CONDITION THAT HAS A BOOLEAN VARIABLE THAT DEPENDS ON TIME
                    if(spriteNum == 1) {image = throwLeft1;}
                }
                break;
            case "right":
                if(!raking) {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                }
                if(raking) {
                    if(spriteNum == 1) {image = rakeRight1;}
                }
                if(throwingRock) { // USE CONDITION THAT HAS A BOOLEAN VARIABLE THAT DEPENDS ON TIME
                    if(spriteNum == 1) {image = throwRight1;}
                }
                break;
        }

        if(invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // makes player look kinda invincible
        }
        if(drawing) {
            g2.drawImage(image, tempScreenX, tempScreenY,null);
        }

        // RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // COLLISION HITBOX VISUAL (DEBUG)
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

    }
}
