package main;

import ai.AStarPathFinder;
import entity.*;
import environment.EnvironmentHandler;

import object.ObjectManager;
import tasks.TaskState;
import tile.Map;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile true tile size that will be displayed on screen. public so any other packages can access it
    public final int maxScreenCol = 20; // 16 tiles horizontally
    public final int maxScreenRow = 12; // 12 tiles vertically
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 60;
    public final int maxWorldRow = 86;
    public int currentMap; // Current map player is in
    public final int GAS_STATION = 0;
    public final int PINEWOOD_CAMP = 1;
        // sub maps
    public final int SUB_GAS_STATION = 0;
    public final int SUB_FRONT_OFFICE = 1;
    public final int SUB_PLAYER_CABIN = 2;
    public final int SUB_MAIN_WORLD = 3;
    public int subMap = SUB_GAS_STATION; // DIFFERENT SUB MAPS INSIDE PINEWOOD

    // FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean toggleFullScreen = false;

    // FPS
    int FPS = 60;

    // "this" as the argument is passing the reference to the exact GamePanel object thats currently running. (NOT THE OBJECT, NOT A COPY, NOT A NEW OBJECT)
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventH = new EventHandler(this);
    Config config = new Config(this);
    public ObjectManager objManager = new ObjectManager(this);
    public AStarPathFinder pFinder = new AStarPathFinder(this);
    public CutsceneManager sceneM = new CutsceneManager(this);
    Thread gameThread; // thread is something you can start/stop. once thread started it keeps the program running
    public TaskState currentTask = TaskState.GET_SNACKS;
    Map map = new Map(this);
    public EnvironmentHandler eHandler = new EnvironmentHandler(this);

    // ENTITIES AND OBJECTS
    public final int maxObj = 30;
    public final int maxNpc = 10;
    public final int maxMonster = 1;
    public Player player = new Player(this,keyH);
    public Entity obj[] = new Entity[maxObj];
    public Entity npc[] = new Entity[maxNpc];
    public Entity monster[] = new Entity[maxMonster];

    public ArrayList<Projectile> projectileList = new ArrayList<>();
    ArrayList<Entity> entityArrList = new ArrayList<>(); // store all entities: players, npc's, obj in this list.

    // GAME STATES
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int initialDialogueState = 5;
    public final int optionsState = 6;
    public final int gameOverState = 7;
    public final int transitionMapState = 8;
    public final int computerState = 9;
    public final int transitionState = 10;
    public final int logBookState = 11;
    public final int cutsceneState = 12;
    // Substate, not a main state like most above.
    public final int innerDialogueState = 13;

    public boolean mapOn = false;

    // CONTROL VARIABLES FOR ONE TIME FUNCTIONS - LOADING SCREEN, DIALOGUE, ETC
    public boolean canTypeSound = true;
    public boolean drawBlackScreen = false;
    float j = 1f;
    public boolean oneTime = false;
    public boolean startDayCycle = false;
    public boolean drawTimeStamp = false;

    // EXTRA
    public boolean closeTaskList;
    public boolean monChaseOn = false;


    public GamePanel () {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // if true, all drawings from this component will be done in an offscreen painting buffer (smoother visual updates & eliminates flickering) in short, improves rendering performance
        this.addKeyListener(keyH); // this GamePanel will recognize the key input
        this.setFocusable(true); // with this, the GamePanel can be "focused" to receive key input.
        this.addMouseListener(mouseH);


    }

    public void setupGame() { // created this method so we can add other setup stuff in the future
        currentMap = GAS_STATION;
        gameState = titleState;
        aSetter.setObject();
        aSetter.setNPC();
        eHandler.setup();
        playMusic(8); // play main menu music -- VHS 80s-90s MUSIC
        eHandler.setup();

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(toggleFullScreen) {
            setFullScreen();
        }
    }

    // maybe use this for if player reaches a checkpoint in game?
    public void retry() {

        player.setDefaultPositionPinewood();
        player.restoreLifeAndAttributes();
        aSetter.setNPC();
    }

    // NEW GAME AGAIN
    public void restart() {

        // better to reset this after everything
        canTypeSound = true;
        drawBlackScreen = true;
        ui.introDialogueY = ui.defaultYPosition;
        ui.dialogueIndex = 0;
        ui.wordEnd = 0;
        ui.nextLine = 0;

        // World
        currentMap = GAS_STATION;

        // ENTITY DEFAULTS
        player.setDefaultPositionGasStation();
        player.restoreLifeAndAttributes();
        player.setItems();
        aSetter.setObject();
        aSetter.setNPC();

        music.stop();
        playMusic(8);

    }

    public void setFullScreen() {
        // Get monitor's resolution ex: 1920 x 1080
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // get width and height
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        // tells window to maximize to fill the screen
        Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        screenWidth2 = (int) width;
        screenHeight2 = (int) height;
    }

    public void startGameThread() {

        gameThread = new Thread(this); // this = the GamePanel class. So we're passing the GamePanel class to the thread constructor. This is how you instantiate a Thread.
        gameThread.start(); // automatically call run method
    }

    // as long the game loop continues it will continue to call update and then repaint
    @Override
    public void run() { // when we start the gameThread the run method is automatically called. in this run method we will create the game loop which is the core of the game.

        double drawInterval = 1000000000/FPS; // 0.01666 seconds - how many nanoseconds one frame should take
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while(gameThread != null) { // as-long as gameThread exists, it will repeat the process thats in the while loop

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval; // measures how far behind or on time the loop is -- ensures update() and movement are consistent, prevents slow frames from slowing down the entire game world
            timer += (currentTime - lastTime); // add how many nanoseconds passed during each frame.
            lastTime = currentTime;


            if(delta >= 1) {
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) { // this condition only happens once per second -- happens when the while loop has iterated roughly around 60 times
                //System.out.println("FPS:"+ drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // Transition to maps
    public void transitionMap() {

        // for transitioning out of front office & cabin
        if (currentMap == PINEWOOD_CAMP && subMap == SUB_PLAYER_CABIN || subMap == SUB_FRONT_OFFICE) { // if transitioning map inside pinewood camp
            // remove cabin/office objects
            aSetter.removeAssets();

            // setting positions
            if(subMap == SUB_PLAYER_CABIN) {
                player.setPosAfterCabin();
            } else if(subMap == SUB_FRONT_OFFICE) {
                playSE(22);
                player.setPosAfterOffice();
            }

            // load into main map
            tileM.loadMap("/maps/world01.txt");

            subMap = SUB_MAIN_WORLD;

            // this means we've exited cabin to investigate -- need to make it where it does get escape key only after short cutscene of
            // revealing monster
            if(currentTask == TaskState.INVESTIGATE) {
                aSetter.spawnMon();
            }
        }
        // for transitioning into cabin
        else if (currentMap == PINEWOOD_CAMP && subMap == SUB_MAIN_WORLD) { // if transitioning map inside pinewood camp
            // load in cabin objects
            aSetter.reseatAssets();
            player.setPosInCabin();
            tileM.loadMap("/maps/playercabin.txt"); // load into player cabin
            subMap = SUB_PLAYER_CABIN;
        }
        // this else is executed when it's leaving the gas station - SHOULD BE THE FIRST EVER ONE TO BE EXECUTED
        else if (currentMap == GAS_STATION && subMap == SUB_GAS_STATION) {
            currentMap = PINEWOOD_CAMP;
            player.setDefaultPositionPinewood();
            // call a clear array method in asset setter
            aSetter.clearArray();
            aSetter.setObject();
            aSetter.setNPC();
            player.setDialogue();
            stopMusic();
            playMusic(6);
            tileM.loadMap("/maps/frontoffice.txt");
            currentTask = TaskState.CHECK_IN_FRONT_OFFICE;
            subMap = SUB_FRONT_OFFICE;
        }
    }


    public void update() {


        if(gameState == initialDialogueState) {

            if(canTypeSound) {
                playSE(16);
                canTypeSound = false;
            }

            if (keyH.enterPressed) {

                if(ui.finishedTyping) {

                    // IF FULL DIALOGUE HASN'T FINISHED WE CAN TURN PAGE
                    if (ui.dialogueIndex < ui.introDialogues.length - 1) {
                        ui.finishedTyping = false;
                        ui.dialogueIndex++;
                        ui.nextLine = 0;
                        ui.introDialogueY = tileSize * 2;
                        playSE(16);

                    }

                    // DIALOGUE FINISHED
                    else {
                        ui.wordEnd = 0;
                        ui.finishedTyping = false;
                        gameState = playState;
                        drawBlackScreen = true;
                        stopMusic();
                        playMusic(17);
                    }
                }
            }
            keyH.enterPressed = false;
        }

        if(gameState == playState) {

            // TASK UPDATE
            if(currentTask == TaskState.GET_SNACKS) {
                ui.taskIndex = 0;
            }
            if(currentTask == TaskState.TALK_TO_CASHIER) {
                ui.taskIndex = 1;
            }
            if(currentTask == TaskState.EXIT_STORE) {
                ui.taskIndex = 2;
            }
            if(currentTask == TaskState.CHECK_IN_FRONT_OFFICE) {
                ui.taskIndex = 3;
            }
            if(currentTask == TaskState.GO_TO_COMPUTER) {
                ui.taskIndex = 4;
            }
            if(currentTask == TaskState.GET_CABIN_KEYS) {
                ui.taskIndex = 5;
            }
            if(currentTask == TaskState.GO_TO_CABIN) {
                ui.taskIndex = 6;
            }
            if(currentTask == TaskState.GO_TO_SLEEP) {
                ui.taskIndex = 7;
            }
            if(currentTask == TaskState.INVESTIGATE) {
                ui.taskIndex = 8;
            }
            if(currentTask == TaskState.GET_ESCAPE_KEYS) {
                ui.taskIndex = 9;
            }
            if(currentTask == TaskState.ESCAPE) {
                ui.taskIndex = 10;
            }

            if(mapOn) {player.freezePlayer = true;}
            else {player.freezePlayer = false;}


            player.update(); // it's like a nested updates, when this main update method is called it calls the player update method so the player can be updated thus more organized clean code.

            // NPC
            for(int i = 0; i < npc.length; i++ ){
                if(npc[i] != null) {
                    npc[i].update();
                }
            }


            // MONSTER
            for(int i = 0; i < monster.length; i++) {
                if(monster[i] != null) {
                    monster[i].update();
                }
            }

            // PROJECTILES
            Iterator<Projectile> cursor = projectileList.iterator(); // CURSOR HOLDS A REFERENCE TO PROJECTILE LIST. NOT THE LIST ITSELF. NEEDS TO RECREATE EVERY FRAME SO IT STARTS THE WALK FROM BEGINNING OF THE LIST.

            while(cursor.hasNext()) { // ITERATOR STARTS BEFORE THE FIRST ELEMENT

                Projectile p = cursor.next(); // GETS THE NEXT PROJECTILE REFERENCE IN THE ARRAYLIST. AT THIS POINT CURSOR TAKES ONE STEP FORWARD.

                if(p.alive) p.update();
                else cursor.remove(); // IF IT ISN'T ALIVE, REMOVE IT FROM THE CURRENT INDEX CURSOR IS IN
            }

            // OBJECTS - arranged code so the obj doesnt change animations like players and npcs.
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    obj[i].update();
                }
            }

            eHandler.update();
        }

        if(gameState == computerState) {

            // extra handling
            if(currentTask == TaskState.GO_TO_COMPUTER) {
                if (mouseH.clickOnAssignButton) { // last possible task for computer task
                    ui.checkmarks[4][0] = true;
                    currentTask = TaskState.GET_CABIN_KEYS;
                }
            }
        }

        if(gameState == dialogueState) {

            if(player.pDialogueIndex >= player.playerDialogues[player.pConvoIndex].length) { // player has reached end of responses for current convo
                // go back to play state and reset pConvoIndex, reset pDialogueIndex = 0 | reset pConvoIndex to promote speaking with npc's as much as you want
                player.pDialogueIndex = 0;
                npc[ui.npcIndex].dialogueIndex = 0; // reset back to zero so npc's responses starts back at where the convo started
                gameState = playState;
                if(npc[ui.npcIndex] instanceof NPC_OfficerJames && currentTask == TaskState.GO_TO_CABIN) {

                    // insert grabbing key SE here
                    playSE(21);
                }
                if(npc[ui.npcIndex] instanceof NPC_Melissa) { // works for first time
                    npc[ui.npcIndex].resetPosition = true;
                }
            }
            if(keyH.enterPressed == true) {

                player.pDialogueIndex++;
                npc[ui.npcIndex].speak();
                keyH.enterPressed = false; // Reset back to false to prevent enterPressed always true.
            }
        }

        if(gameState == transitionState) {
            // Only transition map when exiting a map. Otherwise, just a normal transition screen - handle this when screen is totally black
            if(ui.j >= 1f) {
                if (player.exitMap) {
                    if (!oneTime) {
                        transitionMap();
                        oneTime = true;
                    }
                    player.exitMap = false;
                }
                // set all variables so time stamp of 3:15 AM can draw, and day cycle begins.
                if (player.slept) {
                    drawTimeStamp = true;
                    startDayCycle = true;
                    ui.setToDay = true;
                    player.slept = false;
                }
            }
        }

        if(gameState == pauseState) {
            // nothing, no updating player info while paused
        }
    }

    public void drawToTempScreen() {

        // we must manually clear previous frame/states by the three lines below
        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setStroke(new BasicStroke(1));

        // DEBUG
        long drawStart = 0;
        if(keyH.checkDebugText) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if(gameState == titleState || gameState == initialDialogueState) {
            ui.draw(g2);
        }

        // OTHER GAME STATES. START THE MAIN DIALOGUE HERE:
        else{

            // TILE
            tileM.draw(g2); // put this above player because if not, background tiles will hide the player character


            // ADD ALL ENTITIES TO THE ARRAYLIST
            entityArrList.add(player);

            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    entityArrList.add(npc[i]);
                }
            }

            for(int i = 0; i < monster.length; i++) {
                if(monster[i] != null) {
                    entityArrList.add(monster[i]);
                }
            }

            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityArrList.add(projectileList.get(i));
                }
            }

            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    entityArrList.add(obj[i]);
                }
            }

            // SORT - " new Comparator<Entity>() means to create a new object whose type is Comparator<Entity> and since Comparator is an interface, we must state its method here. We basically skipped making a new class page.
            // In simplist form, its saying Collections.sort(entityArrList, Object)
            Collections.sort(entityArrList, new Comparator<Entity>() { // COMPUTER DOESN'T LOOK AT LIST ALL AT ONCE. 2 ENTITIES AT A TIME THEN ASKS COMPARATOR WHICH SHOULD BE DRAWN FIRST
                // compare is a method of the Comparator interface.
                @Override
                public int compare(Entity e1, Entity e2) { // acts as referee. returns: -1 if e1 is less than e2(put e1 earlier in list/draw first),
                    // 1 if e1 is greater than e2(put e1 later in list/draw last), 0 if e1 is equal to e2-- in this case, relative order doesn't change.
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            // DRAW ENTITIES
            for(int i = 0; i < entityArrList.size(); i++) {

                entityArrList.get(i).draw(g2); // EX: if i = 0 and in the entityArrList.get(0) points to player, it essentially simplifies to player.draw(g2). if npc/object, then its basically saying npc[0].draw(g2).
            }

            // EMPTY ENTITY LIST - OTHERWISE THE entityArrList GETS LARGER IN EVERY LOOP.
            entityArrList.clear();

            if(currentMap == PINEWOOD_CAMP) {
                eHandler.draw(g2);
            }

            // UI - SET IT BELOW tiles and player draw methods so it doesn't get covered
            ui.draw(g2);

            sceneM.draw(g2);

           // show map
            if (mapOn == true) {
                map.drawMap(g2);
            }

            // Intro dialogue transition to game.
            if(drawBlackScreen == true) {
                if(j > 0) {
                    g2.setColor(Color.black);

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, j));
                    g2.fillRect(0, 0, screenWidth, screenHeight);
                    g2.setComposite(AlphaComposite.SrcOver);   // switch back to normal mode - prevents other things to get blended

                    j -= 0.005f;
                }
                else {
                    j = 1;
                    drawBlackScreen = false;
                }
            }
        }

        // DEBUG
        if(keyH.checkDebugText == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 100;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("ScreenX " + player.screenX, x, y); y += lineHeight;
            g2.drawString("ScreenY " + player.screenY, x, y); y += lineHeight;
            g2.drawString("WorldX " + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY " + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col " + (player.worldX + player.solidArea.x) / tileSize, x, y); y += lineHeight;
            g2.drawString("Row " + (player.worldY + player.solidArea.y) / tileSize, x, y); y += lineHeight;
            g2.drawString("Draw Time: " + passed, x, y); // shows how much time has passed
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }


    public void playMusic(int i) { // for music we use loop because it is obviously a continuous sound
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {

        music.stop();
    }
    public void playSE(int i) { // for sound effects we dont call loop cause its just a one time occurance

        se.setFile(i);
        se.play();
    }
}

