package main;

import entity.*;
import monster.MON_EVILBILL;
import object.*;
import tasks.TaskState;

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

            if(gp.currentTask != TaskState.INVESTIGATE) {

                gp.obj[2] = new OBJ_K4Door(gp);
                gp.obj[2].worldX = 15 * gp.tileSize;
                gp.obj[2].worldY = 15 * gp.tileSize;

                gp.obj[5] = new OBJ_Door2(gp); // faces horizontally
                gp.obj[5].worldX = 36 * gp.tileSize;
                gp.obj[5].worldY = 63 * gp.tileSize;
                gp.obj[5].collision = true;

                gp.obj[16] = new OBJ_Campfire(gp);
                gp.obj[16].worldX = 21 * gp.tileSize;
                gp.obj[16].worldY = 56 * gp.tileSize;

                gp.obj[17] = new OBJ_Campfire(gp);
                gp.obj[17].worldX = 13 * gp.tileSize;
                gp.obj[17].worldY = 77 * gp.tileSize;

                gp.obj[21] = new OBJ_Desk(gp);
                gp.obj[21].worldX = 40 * gp.tileSize;
                gp.obj[21].worldY = (59 * gp.tileSize) - 10;

                gp.obj[22] = new OBJ_frontDeskCounter(gp);
                gp.obj[22].worldX = 45 * gp.tileSize;
                gp.obj[22].worldY = (61 * gp.tileSize);



                // EXIT GATE
                gp.obj[25] = new OBJ_Gate(gp, 3);
                gp.obj[25].worldX = 34 * gp.tileSize;
                gp.obj[25].worldY = 7 * gp.tileSize;

                gp.obj[26] = new OBJ_Gate(gp, 1);
                gp.obj[26].worldX = 35 * gp.tileSize;
                gp.obj[26].worldY = 7 * gp.tileSize;

                gp.obj[27] = new OBJ_Gate(gp, 1);
                gp.obj[27].worldX = 36 * gp.tileSize;
                gp.obj[27].worldY = 7 * gp.tileSize;

                gp.obj[28] = new OBJ_Gate(gp, 2);
                gp.obj[28].worldX = 37 * gp.tileSize;
                gp.obj[28].worldY = 7 * gp.tileSize;

                gp.obj[29] = new OBJ_Gate(gp, 1);
                gp.obj[29].worldX = 38 * gp.tileSize;
                gp.obj[29].worldY = 7 * gp.tileSize;

                gp.obj[30] = new OBJ_Gate(gp, 1);
                gp.obj[30].worldX = 39 * gp.tileSize;
                gp.obj[30].worldY = 7 * gp.tileSize;

                gp.obj[31] = new OBJ_Gate(gp, 4);
                gp.obj[31].worldX = 40 * gp.tileSize;
                gp.obj[31].worldY = 7 * gp.tileSize;

                gp.obj[32] = new OBJ_PORTAPOTTY(gp);
                gp.obj[32].worldX = 46 * gp.tileSize;
                gp.obj[32].worldY = 28 * gp.tileSize;


                gp.obj[33] = new OBJ_Carpet(gp);
                gp.obj[33].worldX = 22 * gp.tileSize;
                gp.obj[33].worldY = 13 * gp.tileSize;

                gp.obj[23] = new OBJ_CabinDesk(gp);
                gp.obj[23].worldX = 22 * gp.tileSize;
                gp.obj[23].worldY = 17 * gp.tileSize;

                gp.obj[34] = new OBJ_BookShelf(gp);
                gp.obj[34].worldX = 18 * gp.tileSize;
                gp.obj[34].worldY = 13 * gp.tileSize;

                gp.obj[35] = new OBJ_Barrel(gp);
                gp.obj[35].worldX = 16 * gp.tileSize;
                gp.obj[35].worldY = 14 * gp.tileSize;

            }
            else if(gp.currentTask == TaskState.INVESTIGATE) {
                gp.obj[24] = new OBJ_Key(gp);
                gp.obj[24].worldX = 12 * gp.tileSize;
                gp.obj[24].worldY = 74 * gp.tileSize;
            }
        }
    }

    public void removeOutsideAssets(){
        gp.obj[2] = null;
    }

    // Call when entering player cabin — hides outdoor-only objects
    public void hideCabinExteriorAssets() {
        gp.obj[2] = null;   // OBJ_K4Door
        // null out all gate slots
        for (int i = 25; i <= 31; i++) {
            gp.obj[i] = null;
        }
    }

    // Call when exiting player cabin — restores outdoor-only objects
    public void restoreCabinExteriorAssets() {
        gp.obj[2] = new OBJ_K4Door(gp);
        gp.obj[2].worldX = 15 * gp.tileSize;
        gp.obj[2].worldY = 15 * gp.tileSize;

        if(!gp.player.unlockedGate) {

            gp.obj[25] = new OBJ_Gate(gp, 3);
            gp.obj[25].worldX = 34 * gp.tileSize;
            gp.obj[25].worldY = 7 * gp.tileSize;

            gp.obj[26] = new OBJ_Gate(gp, 1);
            gp.obj[26].worldX = 35 * gp.tileSize;
            gp.obj[26].worldY = 7 * gp.tileSize;

            gp.obj[27] = new OBJ_Gate(gp, 1);
            gp.obj[27].worldX = 36 * gp.tileSize;
            gp.obj[27].worldY = 7 * gp.tileSize;

            gp.obj[28] = new OBJ_Gate(gp, 2);
            gp.obj[28].worldX = 37 * gp.tileSize;
            gp.obj[28].worldY = 7 * gp.tileSize;

            gp.obj[29] = new OBJ_Gate(gp, 1);
            gp.obj[29].worldX = 38 * gp.tileSize;
            gp.obj[29].worldY = 7 * gp.tileSize;

            gp.obj[30] = new OBJ_Gate(gp, 1);
            gp.obj[30].worldX = 39 * gp.tileSize;
            gp.obj[30].worldY = 7 * gp.tileSize;

            gp.obj[31] = new OBJ_Gate(gp, 4);
            gp.obj[31].worldX = 40 * gp.tileSize;
            gp.obj[31].worldY = 7 * gp.tileSize;
        } else {
            setUnlockedGate();
        }
    }

    public void setUnlockedGate() {

        // CLEAR UNECESSARY GATE OBJECTS
        clearGate();

        gp.obj[26] = new OBJ_Gate(gp, 5);
        gp.obj[26].worldX = 34 * gp.tileSize;
        gp.obj[26].worldY = 8 * gp.tileSize;

        gp.obj[27] = new OBJ_Gate(gp, 5);
        gp.obj[27].worldX = 34 * gp.tileSize;
        gp.obj[27].worldY = 9 * gp.tileSize;

        gp.obj[28] = new OBJ_Gate(gp, 5);
        gp.obj[28].worldX = 40 * gp.tileSize;
        gp.obj[28].worldY = 8 * gp.tileSize;

        gp.obj[29] = new OBJ_Gate(gp, 5);
        gp.obj[29].worldX = 40 * gp.tileSize;
        gp.obj[29].worldY = 9 * gp.tileSize;
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

    // Call when transitioning maps to reseat obj's that should only show when in a specific map
    public void reseatAssets() {
        gp.obj[20] = new OBJ_Bed(gp);
        gp.obj[20].worldX = 25 * gp.tileSize;
        gp.obj[20].worldY = 13 * gp.tileSize;

        gp.obj[23] = new OBJ_CabinDesk(gp);
        gp.obj[23].worldX = 22 * gp.tileSize;
        gp.obj[23].worldY = 17 * gp.tileSize;

        gp.obj[33] = new OBJ_Carpet(gp);
        gp.obj[33].worldX = 22 * gp.tileSize;
        gp.obj[33].worldY = 13 * gp.tileSize;

        gp.obj[34] = new OBJ_BookShelf(gp);
        gp.obj[34].worldX = 18 * gp.tileSize;
        gp.obj[34].worldY = 13 * gp.tileSize;

        gp.obj[35] = new OBJ_Barrel(gp);
        gp.obj[35].worldX = 16 * gp.tileSize;
        gp.obj[35].worldY = 14 * gp.tileSize;

    }

    // Call when transitioning maps to remove obj's that shouldn't show in specific maps.
    public void removeAssets() {
        // Mostly, if not all, from player cabin
        gp.obj[20] = null;
        gp.obj[23] = null;
        gp.obj[33] = null;
        gp.obj[34] = null;
        gp.obj[35] = null;



        // front desk cabin
        gp.obj[21] = null;
        gp.obj[22] = null;
        gp.obj[5] = null;


    }

    public void clearArray() {
        gp.npc = new Entity[gp.maxNpc];
        gp.obj = new Entity[gp.maxObj];
        gp.monster = new Entity[gp.maxMonster];
    }

    private void clearGate() {
        for(int i = 26; i < 31; i++) {
            gp.obj[i] = null;
        }
    }
}
