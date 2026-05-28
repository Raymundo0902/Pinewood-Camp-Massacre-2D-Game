package ai;

import main.GamePanel;
import object.OBJ_Campfire;

import java.util.ArrayList;

public class AStarPathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openNodes = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public AStarPathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {

        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            // fills up the game's tiles with nodes
            node[col][row] = new Node(col, row);
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

        }
    }

    public void resetNodes() {
//        System.out.println("RESET TOGGLED");
        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // reset open, checked, and more states
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

             col++;
             if(col == gp.maxWorldCol) {
                 col = 0;
                 row++;
             }
        }

        // Reset other settings
        openNodes.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    // most likely passes in the start col and row of monster and goalcol and row which is the player
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {

        resetNodes();
        // Set start and goal nodes
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openNodes.add(currentNode);


        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // Set solid node

            // Check tile - at my position what tile exists in the world map? where it's Nodes col and row. (NOTE: tiles that have campfires on top of them will be marked with collision on.)
            int tileNum = gp.tileM.mapTileNum[col][row];
            if(gp.tileM.tile[tileNum].collision == true) {
                node[col][row].solid = true;
            }
            // solids do get detected but entity sometimes doesnt know to adjust itself well
//           better way than just checking columns? -- i believe this is is working 100% run a few more test cases where entity is approaching from each possible position where it needs to go through campfire
//            if(gp.objManager.objectMap[col][row] != null) {
//                if (gp.objManager.objectMap[col][row].collision) {
//                    node[col][row].solid = true;
//                }
//            }
            if (gp.objManager.objectMap[col][row]) {
                node[col][row].solid = true;
            }

            // Set cost
            getCost(node[col][row]);

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {

        // G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = (xDistance + yDistance);
        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = (xDistance + yDistance);
        // F cost
        node.fCost = (node.gCost + node.hCost);
    }

    public boolean search() {
        // step prevents algorithm from running forever.
        while(goalReached == false && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            // Check the current node
            currentNode.checked = true;
            openNodes.remove(currentNode);

            // Set above/below nodes to open
            if(row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            if(row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            // Set left/right nodes to open
            if(col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            if(col + 1 < gp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }

            // Find the most promising node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openNodes.size(); i++) {

                // Check if this node's F cost is cheaper
                if(openNodes.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openNodes.get(i).fCost;
                }
                // If equal f costs, compare g costs
                else if (openNodes.get(i).fCost == bestNodefCost) {
                    if(openNodes.get(i).gCost < openNodes.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            // If there is no node in the openNodes arraylist, end the loop
            if(openNodes.size() == 0) {

                break;
            }
            // After loop,
            currentNode = openNodes.get(bestNodeIndex);
            if(currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }
            step++;
        }
        return goalReached;
    }

    // Called when examining neighbors of currentNode
    public void openNode(Node node) {
        // Statement prevents duplicates, walking through walls, no revisiting processed nodes.
        if(node.open == false && node.solid == false && node.checked == false) {
            // Have node be ready for evaluation
            node.open = true;
            // when goal is found we backtrack: goal -> parent -> parent -> parent -> start. Only nodes that were opened gets a parent.
            node.parent = currentNode;
            openNodes.add(node);
        }
    }

    public void trackPath() {

        Node current = goalNode;

        while(current != startNode) {
            // Insert at zero always so we can reverse the parent chain structure so instead of Goal -> parent -> parent -> start we do start -> parent -> parent -> goal
            pathList.add(0, current);
            current = current.parent;
        }
    }



}
