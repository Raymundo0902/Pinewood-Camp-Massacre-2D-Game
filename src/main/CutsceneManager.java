package main;

import entity.PlayerDummy;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;

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

            // instantiate a new dummy at a vacant npc slot
            for(int i = 0; i < gp.npc.length; i++) {

                if(gp.npc[i] == null) {
                    gp.npc[i] = new PlayerDummy(gp);
                    gp.npc[i].worldX = gp.player.worldX;
                    gp.npc[i].worldY = gp.player.worldY;
                    gp.npc[i].direction = gp.player.direction;
                }
            }

        }


        if(scenePhase == 1) {
            gp.player.worldY -= 2;
        }
    }
}
