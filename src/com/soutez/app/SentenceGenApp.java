package com.soutez.app;

import com.soutez.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class SentenceGenApp implements App {
    public BufferedImage run(Map town, Map pacman, String[] dictionary) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // init window
        JFrame frame = new JFrame("Sentence Generator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.getContentPane().setPreferredSize(new Dimension(5 + 80 + 5 + 80 + 5 + 80 + 5 + 256 + 5, 5 + 16 + 5 + 256 + 5 + 32 + 5 + 32 + 5 + 24 + 5));
        frame.pack();
        frame.setLayout(null);
        frame.setResizable(false);

        // init components
        JButton generateButton = new JButton("Generate");
        JButton saveButton = new JButton("Save");
        JTextField sentenceField = new JTextField();
        JTextArea nounInput = new JTextArea();
        JTextArea adjectiveInput = new JTextArea();
        JTextArea verbInput = new JTextArea();
        JLabel nounLabel = new JLabel("Nouns");
        JLabel adjectiveLabel = new JLabel("Adjectives");
        JLabel verbLabel = new JLabel("Verbs");
        JLabel savedSentencesLabel = new JLabel("Saved Sentences");
        JTextArea savedSentences = new JTextArea();

        // set component locations and sizes
        generateButton.setBounds(5, 5 + 16 + 5 + 256 + 5, 80 + 5 + 80 + 5 + 80, 32);
        saveButton.setBounds(5, 5 + 16 + 5 + 256 + 5 + 32 + 5, 80 + 5 + 80 + 5 + 80, 32);
        sentenceField.setBounds(5, 5 + 16 + 5 + 256 + 5 + 32 + 5 + 32 + 5, 80 + 5 + 80 + 5 + 80, 24);
        nounLabel.setBounds(5, 5, 80, 16);
        adjectiveLabel.setBounds(5 + 80 + 5, 5, 80, 16);
        verbLabel.setBounds(5 + 80 + 5 + 80 + 5, 5, 80, 16);
        savedSentencesLabel.setBounds(5 + 80 + 5 + 80 + 5 + 80 + 5, 5, 256, 16);
        nounInput.setBounds(5, 5 + 16 + 5, 80, 256);
        adjectiveInput.setBounds(5 + 80 + 5, 5 + 16 + 5, 80, 256);
        verbInput.setBounds(5 + 80 + 5 + 80 + 5, 5 + 16 + 5, 80, 256);
        savedSentences.setBounds(5 + 80 + 5 + 80 + 5 + 80 + 5, 5 + 16 + 5, 256, 256 + 5 + 32 + 5 + 32 + 5 + 24);

        // set component settings
        sentenceField.setEditable(false);
        savedSentences.setEditable(false);

        // add press events to buttons
        generateButton.addActionListener(e -> {
            // put words to arrays
            String[] nouns = nounInput.getText().replaceAll("\r", "").split("\n");
            String[] adjectives = adjectiveInput.getText().replaceAll("\r", "").split("\n");
            String[] verbs = verbInput.getText().replaceAll("\r", "").split("\n");

            // check if each array has at least 5 elements
            if (nouns.length < 5 || adjectives.length < 5 || verbs.length < 5) {
                JOptionPane.showMessageDialog(frame, "There has to be at least 5 nouns, adjectives or verbs", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // generate sentence
            Random random = new Random();
            String sentence = nouns[random.nextInt(nouns.length)] + " " + adjectives[random.nextInt(adjectives.length)] + " " + verbs[random.nextInt(verbs.length)];
            sentenceField.setText(sentence);
        });
        saveButton.addActionListener(e -> {
            // check if a sentence has been generated
            if (sentenceField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "There is no sentence generated yet", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // add to list of sentences
            savedSentences.setText(savedSentences.getText() + sentenceField.getText() + "\n");
        });

        // Add components to frame
        frame.add(generateButton);
        frame.add(saveButton);
        frame.add(sentenceField);
        frame.add(nounInput);
        frame.add(adjectiveInput);
        frame.add(verbInput);
        frame.add(nounLabel);
        frame.add(adjectiveLabel);
        frame.add(verbLabel);
        frame.add(savedSentencesLabel);
        frame.add(savedSentences);

        // open the window
        frame.setVisible(true);
        return null;
    }
}
