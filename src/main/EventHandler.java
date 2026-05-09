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
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {
        if(hit(20,56, "any") == true) { gp.player.takeDamage(); }
        else if (hit(18, 18, "any") == true && gp.subMap == gp.SUB_MAIN_WORLD &&
                 gp.currentTask == TaskState.INVESTIGATE) { monsterAI();}
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

        if(gp.monChaseOn == false) {
            gp.gameState = gp.cutsceneState;
            gp.sceneM.sceneNum = gp.sceneM.evilBill;
        }

    }


}
