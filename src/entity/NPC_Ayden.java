package entity;

import main.GamePanel;

import java.awt.*;

public class NPC_Ayden extends Entity{

    public NPC_Ayden(GamePanel gp) {
        super(gp);
        direction = "up";
        speed = 2;
        name = "ayden";
        getNPCImage();
        setDialogue();

        solidArea = new Rectangle(); // values below are what parts of the character will be solid
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x; // reason we create solidAreaDefaultX,Y is so we can recall the default values of solidArea.x and y because we will change solidArea.x and y later.
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        type = TYPE_NPC;
    }

    public void getNPCImage() {
        up1 = setup("/npc/ayden_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/ayden_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/ayden_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/ayden_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/ayden_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/ayden_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/ayden_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/ayden_right2", gp.tileSize, gp.tileSize);

    }

    public void setDialogue() {

        dialogues[0] = "Hmmm...";
        dialogues[1] = "There's a missing person that was last seen within\na 10 mile radius from here..";
        dialogues[2] = "Nah, but I wonder if this is tied to the killer that's on\nthe loose. Hopefully not.";
        dialogues[3] = "It's a murderer who's been actively killing folks around\nthe area not too far out from here.";

    }

    @Override
    public void update() {

        // Allows npc to continue looking at the missing persons poster after finishing convo with player
        if(resetPosition == true) {
            direction = "up";
            resetPosition = false;
        }
    }

    public void speak() {

        // DO CHARACTER SPECIFIC STUFF
        super.speak();

    }
}
