package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][]; // 2d array
    boolean drawPath = true;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[100]; // we gonna create 10 kinds of tiles, water tile, grass tile, etc. If needed more, increase more indicies (size)
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/gasStation.txt");
    }

    public void getTileImage() {

            // PLACEHOLDERS, JUST SO WE CAN AVOID USING 1 DIGIT NUMS ON MAP SO IT'S NOT UNBALANCED
            setup(0, "darkgrass1", false);
            setup(1, "darkgrass1", false);
            setup(2, "darkgrass1", false);
            setup(3, "darkgrass1", false);
            setup(4, "darkgrass1", false);
            setup(5, "darkgrass1", false);
            setup(6, "darkgrass1", false);
            setup(7, "darkgrass1", false);
            setup(8, "darkgrass1", false);
            setup(9, "darkgrass1", false);



            setup(10, "darkgrass1", false);
            setup(11, "horizontalWall", true);
            setup(12, "water", true);
            setup(13, "retroWall1", true);
            setup(14, "darkTree", true);
            setup(15, "gravelRoad1", false);
            setup(17, "16xTile", false);
            setup(20, "bottomMainWall", true);
            setup(21, "verticalTwoWalls", true);
            setup(23, "bLeftCornerWall", true);
            setup(24, "bRightCornerWall", true);
            setup(25, "tLeftCornerWall", true);
            setup(26, "tRightCornerWall", true);

            // PLAYER CABIN TILES
            setup(22, "k4cabin1", true);
            setup(27, "k4cabin8", true);
            setup(28, "k4cabin10", true);
            setup(29, "k4cabin2", true);
            setup(30, "k4cabin3", true);
            setup(31, "k4cabin9", true);
            setup(18, "k4cabin7", true);
            setup(19, "k4cabin10", true);
            setup(33, "k4cabin4", true);
            setup(34, "k4cabin5", true);
            setup(46, "k4cabin6", true);
            setup(89, "k4cabin11", true);
            setup(90, "k4cabin13", true);
            setup(16, "k4cabin12", true);

            setup(32, "gasStationWall", true);
            setup(35, "darkgrass2", false);
            setup(36, "leftGravelRoad", false);
            setup(37, "rightGravelRoad", false);
            setup(38, "topGravelRoad", false);
            setup(39, "bottomGravelRoad", false);
            setup(40, "tRightGravelRoad", false);
            setup(41, "bLeftCornerGravelRoad", false);
            setup(42, "bRightCornerGravelRoad", false);
            setup(43, "tLeftCornerGravelRoad", false);
            setup(44, "tRightCornerGravelRoad", false);
            setup(45, "bottomMainWall", true);
            setup(47, "topWater", true);
            setup(48, "bottomWater", true);
            setup(49, "bLeftWaterCorner", true);
            setup(50, "bRightWaterCorner", true);
            setup(51, "leftWater", true);
            setup(52, "rightWater", true);
            setup(53, "tLeftWaterCorner", true);
            setup(54, "tRightWaterCorner", true);
            setup(55, "bRightGravelRoad", false);
            setup(56, "LBridge", false);
            setup(57, "RBridge", false);
            setup(58, "bridge", false);
            setup(59, "bLeftGravelRoad", false);
            setup(60, "tLeftGravelRoad", false);
            setup(61, "sand1", false);
            setup(62, "sandShoreRight", true);
            setup(63, "tLeftWaterCorner2", true);
            setup(64, "tRightWaterCorner2", true);
            setup(65, "bLeftWaterCorner2", true);
            setup(66, "bRightWaterCorner2", true);
            setup(67, "tRightGrassToSandWaterCorner", true);
            setup(68, "tRightGrassToSandCorner", false);
            setup(69, "bRightGrassToSandCorner", false);
            setup(70, "sandToGrassRight", false);
            setup(71, "bRightGrassToSandCorner2", false);
            setup(72, "tRightGrassToSandCorner2", false);
            setup(73, "bRightGrassToSandWaterCorner", true);
            setup(74, "water2", true);
            setup(75, "bridge2", false);
            setup(76, "woodfloor", false);
            setup(77, "retroWall2", true);
            setup(78, "retroWall3", true);
            setup(79, "retroWall4", true);
            setup(80, "tLeftCarpet", false);
            setup(81, "tRightCarpet", false);
            setup(82, "carpet1", false);
            setup(83, "carpet2", false);
            setup(84, "bLeftCarpet", false);
            setup(85, "bRightCarpet", false);
            setup(86, "vent", false);
            setup(87, "missingperson1", true);
            setup(88, "missingperson2", true);
            setup(91, "blacktile", true);
            setup(92, "woodwall", true);
    }

    public void setup(int index, String imageName, boolean collision) {

        System.out.println("SETTING UP NEW TILES"); // does run when leaving to main world

        UtilityTool uTool = new UtilityTool();

        try{

            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize); // calls the UTool and get scaled image
            tile[index].collision = collision; // set collision


        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {

        try{
            InputStream is = getClass().getResourceAsStream(filePath); // loads the txt file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // prepared to read and will read one line of text when .readLine() is called

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) { // becomes false only because of row. Row keeps getting updated and never set back to 0.

                String line = br.readLine(); // .readLine() reads one entire line of text and puts it in the String line EX: "1 0 0 0 1 0 1"

                while(col < gp.maxWorldCol) { // "while you haven't reached the end of this row"

                    String numbers[] = line.split(" "); // .split(" ") = EX: string line = "1 0 0 1" so then here it becomes array of strings like from numbers[0] = "1" ... numbers[3] = "1"... IT KEEPS GETTING UPDATED IN INNER LOOP BUT ITS LIKE NOTHING HAPPENED SINCE WE'RE STILL AT THE FIRST LINE OF TEXT.

                    int num = Integer.parseInt(numbers[col]); // use col as an index for numbers[] array. we are changing this string to integer here so we can use them as a number. this happens once each iteration of the inner loop

                    mapTileNum[col][row] = num; // stores num one by one by each iteration of the inner loop
                    col++; // move to next tile in this row
                }
                if(col == gp.maxWorldCol) { // once col == 16, set it back to 0 aka to the beginning of tile map, then move on to next row aka drop down 1 and repeat.
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch(Exception e) {

        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) { // maxWorldCol = 50 and maxWorldRow = 50

            int tileNum = mapTileNum[worldCol][worldRow];

            // WORLD POSITION
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            // SCREEN POSITION
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&    // ONLY DRAW TILES THAT ARE CLOSE ENOUGH TO THE PLAYER ( inside the camera) TO APPEAR ON SCREEN. NOT REQUIRED BUT RECOMMENDED FOR BETTER GAME PERFORMANCE
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && // CAMERA DOESNT HAVE A x or y WORLD POSITION WHICH IS WHY
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);

            }
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
        if(drawPath == true) {
            g2.setColor(new Color(12, 200, 200, 70));

            for(int i = 0; i < gp.pFinder.pathList.size(); i++) {
                // WORLD POSITION
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                // SCREEN POSITION
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }

    }
}
