package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Cashier extends Entity{


// null pointer happens to this npc only not the other one.
    public NPC_Cashier(GamePanel gp) {
        super(gp);
        direction = "left";
        speed = 2;
        name = "cashier";
        getNPCImage();
        setDialogue();
        interactable = false; // cannot interact directly, must interact by colliding with front area of checkout desk.

        solidArea = new Rectangle(); // values below are what parts of the character will be solid
        solidArea.x = -gp.tileSize;
        solidArea.y = -32;
        solidAreaDefaultX = solidArea.x; // reason we create solidAreaDefaultX,Y is so we can recall the default values of solidArea.x and y because we will change solidArea.x and y later.
        solidAreaDefaultY = solidArea.y;
        solidArea.width = gp.tileSize;
        solidArea.height = (gp.tileSize * 2) + 30;

        type = TYPE_NPC;
    }

    public void getNPCImage() {
        left1 = setup("/npc/cashierLeft1", gp.tileSize, gp.tileSize + 5);
    }

    public void setDialogue() {

        // Dialogue
        dialogues[0] = "That'll be $11.99.";
        dialogueSize++;
        dialogues[1] = "What are you some kinda park ranger?";
        dialogueSize++;
        dialogues[2] = "I suggest you be careful now, there's been reportings\n of a killer on the loose not too far out from there..";
        dialogueSize++;
        dialogues[3] = "Im being serious! Anywho, have a good drive there!";
        dialogueSize++;
    }

    @Override
    public void update() {

    }

    public void setAction() {

    }

    public void speak() {

        // DO THIS CHARACTER SPECIFIC STUFF
        super.speak();

    }
}
