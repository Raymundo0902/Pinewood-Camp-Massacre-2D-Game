package object;

import entity.Entity;
import main.GamePanel;

// use this as basically saying where ever in the map each tile has potential object. then whatever is set in asset setter, set it here
// then we can call objectmanager while we loop through the pathfinding and set nodes to solid by calling the full objectMap 2d array and it should check if one of
// the indexes inside a specifc col and row has an object placed there. if so set it to be solid tile there.
public class ObjectManager {

    GamePanel gp;
    public boolean[][] objectMap;
    public boolean collision = true;


    public ObjectManager(GamePanel gp) {
        this.gp = gp;
        objectMap = new boolean[gp.maxWorldCol][gp.maxWorldRow];
        setObjectMap();
    }

    // set all Asetter objects here
    public void setObjectMap() {

        // use this to your advantage so when pathfinder setting nodes based on col and row, you can call objectMap and pass in
        // the current col and row of the pathfinder's current node array index and if it encounters the objcampfire then set
        // that node to solid
//        objectMap[21][56] =  new OBJ_Campfire(gp);
//        objectMap[13][77] =  new OBJ_Campfire(gp);
//        objectMap[46][28] = new OBJ_PORTAPOTTY(gp);
//        objectMap[46][29] = new OBJ_PORTAPOTTY(gp);
//        objectMap[34][7] = new OBJ_Gate(gp, 5);
//        objectMap[40][7] = new OBJ_Gate(gp, 5);
//        objectMap[34][8] = new OBJ_Gate(gp, 5);
//        objectMap[34][9] = new OBJ_Gate(gp, 5);
//        objectMap[40][8] = new OBJ_Gate(gp, 5);
//        objectMap[40][9] = new OBJ_Gate(gp, 5);

        objectMap[21][56] =  collision;
        objectMap[13][77] =  collision;
        objectMap[46][28] = collision;
        objectMap[46][29] = collision;
        objectMap[34][7] = collision;
        objectMap[40][7] = collision;
//        objectMap[34][8] = collision;
//        objectMap[34][9] = collision;
//        objectMap[40][8] = collision;
//        objectMap[40][9] = collision;
    }

}
