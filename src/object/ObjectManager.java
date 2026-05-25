package object;

import entity.Entity;
import main.GamePanel;

// use this as basically saying where ever in the map each tile has potential object. then whatever is set in asset setter, set it here
// then we can call objectmanager while we loop through the pathfinding and set nodes to solid by calling the full objectMap 2d array and it should check if one of
// the indexes inside a specifc col and row has an object placed there. if so set it to be solid tile there.
public class ObjectManager {

    GamePanel gp;
    public Entity[][] objectMap;


    public ObjectManager(GamePanel gp) {
        this.gp = gp;
        objectMap = new Entity[gp.maxWorldCol][gp.maxWorldRow];

        setObjectMap();
    }

    // set all Asetter objects here
    public void setObjectMap() {


        // use this to your advantage so when pathfinder setting nodes based on col and row, you can call objectMap and pass in
        // the current col and row of the pathfinder's current node array index and if it encounters the objcampfire then set
        // that node to solid
        objectMap[21][56] =  new OBJ_Campfire(gp);
        objectMap[13][77] =  new OBJ_Campfire(gp);
        objectMap[48][39] =  new OBJ_Campfire(gp);

    }

}
