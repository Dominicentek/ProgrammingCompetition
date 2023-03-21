package com.soutez.app;

import com.soutez.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class TemperatureClockApp implements App {
    public BufferedImage run(Map town, Map pacman, String[] dictionary) {
        // init window
        JFrame frame = new JFrame("Temperature Clock");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(16 * 13, 16 * 13));
        frame.pack();
        frame.setLocation(100, 100);
        frame.add(new JPanel() {
            public void paint(Graphics g) {
                // Get time information
                Calendar calendar = Calendar.getInstance();
                int millis = calendar.get(Calendar.MILLISECOND);
                int seconds = calendar.get(Calendar.SECOND);
                int minutes = calendar.get(Calendar.MINUTE);
                int hours = calendar.get(Calendar.HOUR_OF_DAY) % 12;

                // Calculate the height values (+ interpolation)
                int hoursHeight = (int)((hours + minutes / 60.0) * 16);
                int minutesHeight = (int)((minutes + seconds / 60.0) / 5.0 * 16);
                int secondsHeight = (int)((seconds + millis / 1000.0) / 5.0 * 16);

                // Animate going down
                if (seconds == 0) secondsHeight = (int)(16 * 12 * (1 - millis / 1000.0));
                if (minutes == 0 && seconds == 0) minutesHeight = (int)(16 * 12 * (1 - millis / 1000.0));
                if (hours == 0 && minutes == 0 && seconds == 0) hoursHeight = (int)(16 * 12 * (1 - millis / 1000.0));

                // Draw thermometer
                g.setColor(Color.BLACK);
                g.setFont(g.getFont().deriveFont(12f));
                for (int i = 1; i <= 12; i++) {
                    g.drawString((13 - i) + "", 4, (i + 1) * 16 - 3);
                    g.drawString((13 - i) * 5 + "", 72, (i + 1) * 16 - 3);
                    g.drawString((13 - i) * 5 + "", 140, (i + 1) * 16 - 3);
                    g.drawLine(0, i * 16, 360, i * 16);
                }

                // Draw labels
                g.drawString("hr", 4 + 32, 12);
                g.drawString("min", 72 + 26, 12);
                g.drawString("sec", 140 + 26, 12);

                // Draw quicksilver
                g.setColor(Color.RED);
                g.fillRect(4 + 32, 16 * 13 - hoursHeight, 16, hoursHeight);
                g.fillRect(72 + 32, 16 * 13 - minutesHeight, 16, minutesHeight);
                g.fillRect(140 + 32, 16 * 13 - secondsHeight, 16, secondsHeight);
            }
        });
        frame.setResizable(false);
        frame.setVisible(true);

        // main program loop
        try {
            while (true) {
                Thread.sleep(1);
                frame.repaint();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
