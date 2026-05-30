package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class OBJ_BookShelf extends Entity {

    public OBJ_BookShelf(GamePanel gp) {
        super(gp);
        down1 = setup("/objects/bookshelf", gp.tileSize * 2, gp.tileSize * 2);
        collision = true;
        name = "bookshelf";
    }

    @Override
    public void update() {}

    // Had to declare its own draw method from entity because the image is gp.tileSize*2 long so it needs to show image from its top/bottom edge of camera view
    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX; // find out its screenX and Y and if its in the camera frame then draw it
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize> gp.player.worldX - gp.player.screenX && // player.worldX - player.screenX = left edge of camera.      objectRight > cameraLeft
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && // player.worldX + player.screenX = right edge of camera.    objectLeft < cameraRight
                worldY + (gp.tileSize) > gp.player.worldY - gp.player.screenY && // player.worldY - player.screenY = top edge of camera.      objectBottom > cameraTop
                worldY - (gp.tileSize) < gp.player.worldY + gp.player.screenY) { // player.worldY + player.screenY = bottom edge of camera.   objectTop < cameraBottom

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // makes Entity back to normal transparency
            g2.drawImage(down1, screenX, screenY, null);

            // COLLISION VISUALS (DEBUG)
            g2.setColor(Color.red);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

    }
}
