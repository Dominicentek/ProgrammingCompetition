package com.soutez.app;

import com.soutez.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ScrabbleApp implements App {
    public ArrayList<Character> letters = new ArrayList<>();
    public char[] vowels = "aeiouy".toCharArray();
    public char[] consonants = "bcdfghjklmnpqrstvwxz".toCharArray();
    public int points = 0;
    public BufferedImage run(Map town, Map pacman, String[] dictionary) {
        Random random = new Random();

        // init window
        JFrame frame = new JFrame("Scrabble");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(5 + 256 + 5, 5 + 16 + 5 + 16 + 5 + 24 + 5 + 24 + 5));
        frame.pack();
        frame.setLocation(100, 100);
        frame.setResizable(false);
        frame.setLayout(null);

        // init components
        JLabel points = new JLabel("Points: 0");
        JLabel message = new JLabel();
        JTextField lettersField = new JTextField();
        JTextField input = new JTextField();
        JButton button = new JButton("Submit");

        // set bounds of components
        points.setBounds(5, 5, 256, 16);
        message.setBounds(5, 5 + 16 + 5, 256, 16);
        lettersField.setBounds(5, 5 + 16 + 5 + 16 + 5, 256, 24);
        input.setBounds(5, 5 + 16 + 5 + 16 + 5 + 24 + 5, 187, 24);
        button.setBounds(197, 5 + 16 + 5 + 16 + 5 + 24 + 5, 64, 24);

        // change settings of elements
        lettersField.setEditable(false);

        // get 15 letters from the start
        for (int i = 0; i < 15; i++) {
            letters.add(generateRandomLetter(random));
        }

        // display the characters
        for (char letter : letters) {
            lettersField.setText(lettersField.getText() + letter + " ");
        }

        // add event to button
        button.addActionListener(e -> {
            // check if there are any extra characters
            ArrayList<Character> letters = new ArrayList<>(this.letters);
            boolean invalid = false;
            for (char character : input.getText().toCharArray()) {
                if (!letters.contains(character)) {
                    System.out.println("This word is not possible to create with your letters");
                    invalid = true;
                    break;
                }
                letters.remove((Character) character);
            }
            if (invalid) return;

            // search the dictionary for this word
            boolean contains = false;
            for (String word : dictionary) {
                if (word.equals(input.getText())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                System.out.println("This word is not in the dictionary!");
                invalid = true;
            }
            if (invalid) return;

            // all conditions are met, add points to the current score
            this.points += input.getText().length();
            points.setText("Points: " + this.points);

            // give the player more letters
            for (int i = 0; i < random.nextInt(10) + 1; i++) {
                this.letters.add(generateRandomLetter(random));
            }
            for (char letter : letters) {
                lettersField.setText(lettersField.getText() + letter + " ");
            }
        });
        frame.setVisible(true);
        return null;
    }
    public char generateRandomLetter(Random random) {
        if (random.nextBoolean() /* should a vowel be generated or not */) return vowels[random.nextInt(vowels.length)];
        else return consonants[random.nextInt(consonants.length)];
    }
}
