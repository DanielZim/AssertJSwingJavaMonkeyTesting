package de.swingtest.gui;

import javax.swing.*;

public class SimpleSwingGui extends JFrame {
    public static void main(String args[]) {
        SimpleSwingGui app = new SimpleSwingGui();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public SimpleSwingGui() {
        this.setSize(300, 300);
        JButton button = new JButton("Press");
        this.getContentPane().add(button); // Adds Button to content pane of frame
    }
}