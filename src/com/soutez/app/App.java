package com.soutez.app;

import com.soutez.Map;

import java.awt.image.BufferedImage;

public interface App {
    BufferedImage run(Map town, Map pacman, String[] dictionary);
}
