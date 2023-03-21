package com.soutez;

public class Map {
    public int width;
    public int height;
    public char[] data;
    public Map(String raw) {
        String string = raw.replaceAll("\n", "");
        int size = string.length();
        height = raw.split("\n").length;
        width = size / height;
        data = string.toCharArray();
    }
    public void set(char value, int x, int y) {
        data[y * width + x] = value;
    }
    public char get(int x, int y) {
        return data[y * width + x];
    }
}
