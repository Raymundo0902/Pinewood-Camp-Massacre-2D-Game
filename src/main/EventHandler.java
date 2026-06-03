package main;

import tasks.TaskState;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();
        eventRect.x = 0;
        eventRect.y = 0;
        eventRect.width = gp.tileSize;
        eventRect.height = gp.tileSize;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {

        if(hit(20, 56, "any")) {
            gp.player.takeDamage();
        }
        else if (cutsceneTrigger() && gp.subMap == gp.SUB_MAIN_WORLD && gp.currentTask == TaskState.INVESTIGATE) {
            monsterAI();
        }
//        else if(hit(24, 17, "any") && gp.subMap == gp.SUB_PLAYER_CABIN) {
//            gp.player.exitMap = true;
//            gp.gameState = gp.transitionState;
//        }



    }

    private boolean cutsceneTrigger() {
        return  hit(12, 16, "any") || hit(12, 17, "any") ||
                hit(13, 17, "any") || hit(14, 17, "any") ||
                hit(15, 17, "any") || hit(16, 17, "any") ||
                hit(16, 16, "any");
    }

    public boolean hit(int eventCol, int eventRow, String reqDirection) { // checks event collision

        boolean hit = false;
        // GETTING PLAYER'S CURRENT SOLIDAREA POSITIONS
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        // GETTING EVENTRECT'S SOLIDAREA POSITION
        eventRect.x = eventCol * gp.tileSize + eventRect.x; // eventCol/eventRow*gp.tileSize means its worldX/worldY coordinates
        eventRect.y = eventRow * gp.tileSize + eventRect.y;
        // CHECKING IF PLAYER'S SOLIDAREA IS COLLIDING WITH EVENTRECT'S SOLIDAREA
        if(gp.player.solidArea.intersects(eventRect)) {
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
            }
        }
        // RESET THEIR SOLIDAREA'S X AND Y
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;


        return hit;
    }



    public void monsterAI() {

        if(!gp.monChaseOn) {
            gp.gameState = gp.cutsceneState;
            gp.sceneM.sceneNum = gp.sceneM.evilBill;
        }

    }


}
