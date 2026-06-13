package main;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // lets the window properly close when user clicks the "x" button
        window.setResizable(false); // false = cannot resize the window. stays one size
        window.setTitle("Pinewood Camp Massacre");

        try {
            ImageIcon icon = new ImageIcon(Main.class.getResource("/images/pinewoodassociates.png"));
            Image logoImage = icon.getImage();
            window.setIconImage(logoImage); // Sets the window/taskbar logo
        } catch (Exception e) {
            System.out.println("Logo image could not be loaded: " + e.getMessage());
        }

        GamePanel gamePanel = new GamePanel(); // GamePanel object is created on the heap
        window.add(gamePanel);

        gamePanel.config.loadConfig();
        if(gamePanel.toggleFullScreen == true) {
            window.setUndecorated(true);
        }

        window.pack(); // causes the window to be sized to fit the preferred size and layouts of its subcomponents (GamePanel)

        window.setLocationRelativeTo(null); // doesn't specify location of window. will be centered
        window.setVisible(true); // see the window

        gamePanel.setupGame(); // put this method here so it can start before the game thread starts
        gamePanel.startGameThread();

    }

}