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


        objectMap[21][56] =  collision;
        objectMap[13][77] =  collision;
        objectMap[46][28] = collision;
        objectMap[46][29] = collision;
        objectMap[34][7] = collision;
        objectMap[40][7] = collision;

        // front office tiles - spaced individually as its each column
        objectMap[41][59] = collision;
        objectMap[41][60] = collision;
        objectMap[41][61] = collision;
        objectMap[41][62] = collision;

        objectMap[42][59] = collision;
        objectMap[42][60] = collision;
        objectMap[42][61] = collision;
        objectMap[42][62] = collision;

        objectMap[43][59] = collision;
        objectMap[43][60] = collision;
        objectMap[43][61] = collision;
        objectMap[43][62] = collision;

        objectMap[44][59] = collision;
        objectMap[44][60] = collision;
        objectMap[44][61] = collision;
        objectMap[44][62] = collision;
    }

}
