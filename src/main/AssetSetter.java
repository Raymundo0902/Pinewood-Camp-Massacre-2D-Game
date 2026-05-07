package main;

import entity.*;
import monster.MON_EVILBILL;
import object.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        // Make a method in here where it resets the values of the array when we transition to pinewood camp so it can fill in the other objs without maxing out space.
        // Using i++ because it's easier to track and not skip over values.

        if(gp.currentMap == gp.GAS_STATION) {
            // Instantiate only gas station entities here
            int i = 0;

            gp.obj[i] = new OBJ_SnackShelf(gp);
            gp.obj[i].worldX = 38 * gp.tileSize;
            gp.obj[i].worldY = (58 * gp.tileSize);
            i++;

            gp.obj[i] = new OBJ_FruitBox(gp);
            gp.obj[i].worldX = 44 * gp.tileSize;
            gp.obj[i].worldY = (62 * gp.tileSize);
            i++;

            gp.obj[i] = new OBJ_SnackShelf(gp);
            gp.obj[i].worldX = 38 * gp.tileSize;
            gp.obj[i].worldY = (61 * gp.tileSize) - 20;
            i++;

            gp.obj[i] = new OBJ_Fridge(gp);
            gp.obj[i].worldX = 39 * gp.tileSize + 5;
            gp.obj[i].worldY = (56 * gp.tileSize)  - 20;
            i++;

            gp.obj[i] = new OBJ_Fridge(gp);
            gp.obj[i].worldX = 41 * gp.tileSize - 29;
            gp.obj[i].worldY = (56 * gp.tileSize)  - 20;
            i++;

            gp.obj[i] = new OBJ_Fridge(gp);
            gp.obj[i].worldX = 42 * gp.tileSize - 16;
            gp.obj[i].worldY = (56 * gp.tileSize)  - 20;
            i++;

            gp.obj[i] = new OBJ_BackofShelf(gp);
            gp.obj[i].worldX = 38 * gp.tileSize - 16;
            gp.obj[i].worldY = (56 * gp.tileSize)  - 20;
            i++;

            gp.obj[i] = new OBJ_FruitBox2(gp);
            gp.obj[i].worldX = 47 * gp.tileSize;
            gp.obj[i].worldY = (62 * gp.tileSize) + 2;
            i++;

            gp.obj[i] = new OBJ_CheckoutCounter(gp);
            gp.obj[i].worldX = 47 * gp.tileSize + 5;
            gp.obj[i].worldY = (57 * gp.tileSize) - 20;
            i++;

            gp.obj[i] = new OBJ_GlassDoor(gp);
            gp.obj[i].worldX = 44 * gp.tileSize;
            gp.obj[i].worldY = (55 * gp.tileSize);
            i++;

        }
        else if(gp.currentMap == gp.PINEWOOD_CAMP) {

            gp.obj[2] = new OBJ_K4Door(gp);
            gp.obj[2].worldX = 14 * gp.tileSize;
            gp.obj[2].worldY = 15 * gp.tileSize;

            gp.obj[3] = new OBJ_TallGrass(gp);
            gp.obj[3].worldX = 14 * gp.tileSize;
            gp.obj[3].worldY = 74 * gp.tileSize;

            gp.obj[4] = new OBJ_TallGrass(gp);
            gp.obj[4].worldX = 15 * gp.tileSize;
            gp.obj[4].worldY = 74 * gp.tileSize;

            gp.obj[5] = new OBJ_Door2(gp); // faces horizontally
            gp.obj[5].worldX = 36 * gp.tileSize;
            gp.obj[5].worldY = 63 * gp.tileSize;
            gp.obj[5].collision = true;

            gp.obj[6] = new OBJ_Door(gp);
            gp.obj[6].worldX = 20 * gp.tileSize;
            gp.obj[6].worldY = 38 * gp.tileSize;

            gp.obj[7] = new OBJ_TallGrass(gp);
            gp.obj[7].worldX = 13 * gp.tileSize;
            gp.obj[7].worldY = 74 * gp.tileSize;

            gp.obj[8] = new OBJ_TallGrass(gp);
            gp.obj[8].worldX = 13 * gp.tileSize;
            gp.obj[8].worldY = 75 * gp.tileSize;

            gp.obj[9] = new OBJ_TallGrass(gp);
            gp.obj[9].worldX = 14 * gp.tileSize;
            gp.obj[9].worldY = 75 * gp.tileSize;

            gp.obj[10] = new OBJ_TallGrass(gp);
            gp.obj[10].worldX = 15 * gp.tileSize;
            gp.obj[10].worldY = 75 * gp.tileSize;

            gp.obj[11] = new OBJ_TallGrass(gp);
            gp.obj[11].worldX = 12 * gp.tileSize;
            gp.obj[11].worldY = 75 * gp.tileSize;

            gp.obj[12] = new OBJ_Rock(gp);
            gp.obj[12].worldX = 21 * gp.tileSize;
            gp.obj[12].worldY = 52 * gp.tileSize;

            gp.obj[13] = new OBJ_Rock(gp);
            gp.obj[13].worldX = 23 * gp.tileSize;
            gp.obj[13].worldY = 54 * gp.tileSize;

            gp.obj[14] = new OBJ_Rock(gp);
            gp.obj[14].worldX = 24 * gp.tileSize;
            gp.obj[14].worldY = 54 * gp.tileSize;

            gp.obj[15] = new OBJ_Rock(gp);
            gp.obj[15].worldX = 24 * gp.tileSize;
            gp.obj[15].worldY = 55 * gp.tileSize;

            gp.obj[16] = new OBJ_Campfire(gp);
            gp.obj[16].worldX = 21 * gp.tileSize;
            gp.obj[16].worldY = 56 * gp.tileSize;

            gp.obj[17] = new OBJ_Campfire(gp);
            gp.obj[17].worldX = 13 * gp.tileSize;
            gp.obj[17].worldY = 77 * gp.tileSize;

            gp.obj[18] = new OBJ_Campfire(gp);
            gp.obj[18].worldX = 48 * gp.tileSize;
            gp.obj[18].worldY = 39 * gp.tileSize;

            // goes in tool shed near player's cabin
//            gp.obj[19] = new OBJ_Rake(gp);
//            gp.obj[19].worldX = 20 * gp.tileSize;
//            gp.obj[19].worldY = 12 * gp.tileSize;

            gp.obj[21] = new OBJ_Desk(gp);
            gp.obj[21].worldX = 40 * gp.tileSize;
            gp.obj[21].worldY = (59 * gp.tileSize) - 10;

            gp.obj[22] = new OBJ_frontDeskCounter(gp);
            gp.obj[22].worldX = 45 * gp.tileSize;
            gp.obj[22].worldY = (61 * gp.tileSize);

            gp.obj[23] = new OBJ_CabinDesk(gp);
            gp.obj[23].worldX = 17 * gp.tileSize;
            gp.obj[23].worldY = 14 * gp.tileSize;

        }
    }

    public void setNPC() {

        if(gp.currentMap == gp.GAS_STATION) {
            // Instantiate only gas station entities here
            int i = 0;
            gp.npc[i] = new NPC_Cashier(gp);
            gp.npc[i].worldX = 48 * gp.tileSize + 5;
            gp.npc[i].worldY = (59 * gp.tileSize) - 12;
            i++;

            // a tester, use a different npc's and replace it with this one because melissa will be a npc from pinewood camp not gas station.
            gp.npc[i] = new NPC_Ayden(gp);
            gp.npc[i].worldX = 44 * gp.tileSize;
            gp.npc[i].worldY = 61 * gp.tileSize;
            i++;

            gp.npc[i] = new NPC_Melissa(gp);
            gp.npc[i].worldX = 47 * gp.tileSize;
            gp.npc[i].worldY = (56 * gp.tileSize) + 30;
            i++;
        }

        if(gp.currentMap == gp.PINEWOOD_CAMP) {
            // insert all the below entities in here
            gp.npc[0] = new NPC_Ayden(gp);
            gp.npc[0].worldX = gp.tileSize*30;
            gp.npc[0].worldY = gp.tileSize*70;

            gp.npc[1] = new NPC_OfficerJames(gp);
            gp.npc[1].worldX = (gp.tileSize*46) + 24;
            gp.npc[1].worldY = gp.tileSize*62;
        }
    }

    public void spawnMon() {

        // disabled for game testing
        if(gp.currentMap == gp.PINEWOOD_CAMP) {
            gp.monster[0] = new MON_EVILBILL(gp);
            gp.monster[0].worldX = gp.tileSize*18;
            gp.monster[0].worldY = gp.tileSize*11;
        }
    }

    public void reseatAssets() {
        // Call when transitioning maps to reseat obj's that should only show when in a specific map
        gp.obj[20] = new OBJ_Bed(gp);
        gp.obj[20].worldX = 20 * gp.tileSize;
        gp.obj[20].worldY = 12 * gp.tileSize;

        gp.obj[23] = new OBJ_CabinDesk(gp);
        gp.obj[23].worldX = 17 * gp.tileSize;
        gp.obj[23].worldY = 14 * gp.tileSize;
    }

    public void removeAssets() {
        // Call when transitioning maps to remove obj's that shouldn't show in specific maps.
        gp.obj[20] = null;
        gp.obj[23] = null;
    }

    public void clearArray() {
        gp.npc = new Entity[gp.maxNpc];
        gp.obj = new Entity[gp.maxObj];
        gp.monster = new Entity[gp.maxMonster];
    }
}
