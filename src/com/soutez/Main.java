package com.soutez;

import com.soutez.app.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

public class Main {
    public static HashMap<AppType, App> apps = new HashMap<>();
    public static void main(String[] args) throws Exception {
        // register applications to hashmap
        apps.put(AppType.TOWN_PATHFIND, new TownPathfindApp());
        apps.put(AppType.PACMAN, new PacmanApp());
        apps.put(AppType.SENTENCE_GEN, new SentenceGenApp());
        apps.put(AppType.SCRABBLE, new ScrabbleApp());
        apps.put(AppType.TEMPERATURE_CLOCK, new TemperatureClockApp());

        // parse stuff
        Map town = new Map(readFile(new File("mesto.txt")));
        Map pacman = new Map(readFile(new File("packman.txt")));
        String[] dictionary = readFile(new File("slovnik.txt")).split("\n");

        // run the application
        App app = apps.get(AppType.TEMPERATURE_CLOCK); // change this to change the application
        BufferedImage visualization = app.run(town, pacman, dictionary);

        // if app returns null, doesnt need visualization
        if (visualization == null) return;

        // open the window
        JFrame frame = new JFrame("Application");
        frame.getContentPane().setPreferredSize(new Dimension(visualization.getWidth() * 16, visualization.getHeight() * 16));
        frame.pack();
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new JPanel() {
            public void paint(Graphics g) {
                g.drawImage(visualization, 0, 0, visualization.getWidth() * 16, visualization.getHeight() * 16, this);
            }
        });
        frame.setVisible(true);
    }
    public static String readFile(File file) throws IOException  {
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        in.close();
        return out.toString();
    }
}
