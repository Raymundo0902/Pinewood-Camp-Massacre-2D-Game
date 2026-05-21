package main;

import entity.PlayerDummy;
import monster.MON_EVILBILL;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    public int drawTimer;

    // Scene num
    public final int NA = 0;
    public final int evilBill = 1;

    public CutsceneManager(GamePanel gp) {

        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch(sceneNum) {
            case evilBill: scene_monEncounter(); break;
        }
    }

    public void scene_monEncounter() {

        if(scenePhase == 0) {
            gp.monChaseOn = true;
            gp.player.drawing = false;
            scenePhase++;

            // instantiate a new dummy in a vacant npc slot
            for(int i = 0; i < gp.npc.length; i++) {

                if(gp.npc[i] == null) {
                    gp.npc[i] = new PlayerDummy(gp);
                    gp.npc[i].worldX = gp.player.worldX;
                    gp.npc[i].worldY = gp.player.worldY;
                    gp.npc[i].direction = gp.player.direction;
                }
            }

        }

        // start moving camera up to monster, stop when it reaches a certain time. - gets called 60 times a second
        if(scenePhase == 1) {
            drawTimer++;
            if(drawTimer > 160) {
                scenePhase = 2;
                drawTimer = 0;
            }
            System.out.println("SCENE PHASE 1");
            gp.player.worldY -= 2;

        }

        // keep lock on monster for half a second
        if(scenePhase == 2) {
            drawTimer++;
            if(drawTimer > 30) {
                scenePhase = 3;
                drawTimer = 0;
            }
        }

        // draw attention back to player creating that "gasp" feeling
        if(scenePhase == 3) {
            System.out.println("SCENE PHASE 3");
            drawTimer++;

            if(drawTimer > 30) {

                // search for a npc that is a player dummy and remove it.
                for(int i = 0; i < gp.npc.length; i++) {
                    if(gp.npc[i] instanceof PlayerDummy) {
                        gp.npc[i] = null;
                    }
                }

                gp.monster[0].sleep = false;
                drawTimer = 0;

                gp.player.drawing = true;
                gp.player.speak();

                sceneNum = NA;
                gp.player.worldX = 18 * gp.tileSize;
                gp.player.worldY = 18 * gp.tileSize;
                gp.gameState = gp.playState;
                gp.drawInnerDialogue = true;
            }

            gp.player.worldY += 10;
        }
    }

}
