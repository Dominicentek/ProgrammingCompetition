package com.soutez.app;

import com.soutez.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TownPathfindApp implements App {
    public ArrayList<Path> paths = new ArrayList<>();
    public ArrayList<Point> taken = new ArrayList<>();
    public ArrayList<Path> takenPath = new ArrayList<>();
    public BufferedImage run(Map town, Map pacman, String[] dictionary) {
        // define positions
        int startPosX = 0;
        int startPosY = 0;
        int endPosX = 0;
        int endPosY = 0;

        // define and parse map
        boolean[][] map = new boolean[town.width][town.height];
        for (int x = 0; x < town.width; x++) {
            for (int y = 0; y < town.height; y++) {
                char value = town.get(x, y);
                map[x][y] = value == 'X';
                if (value == '@') {
                    startPosX = x;
                    startPosY = y;
                }
                if (value == '%') {
                    endPosX = x;
                    endPosY = y;
                }
            }
        }

        // pathfind
        Path path = new Path();
        path.path.add(new Point(startPosX, startPosY)); // add beginning point to path
        paths.add(path); // add path to path list
        Path shortestPath;
        while ((shortestPath = contains(endPosX, endPosY)) == null) { // until it finds end point
            advance(map);
        }

        // define the visualization image
        BufferedImage image = new BufferedImage(town.width, town.height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // Add pixel from map
                if (map[x][y]) image.setRGB(x, y, 0xFF000000);
                else image.setRGB(x, y, 0xFFFFFFFF);

                // If the pixel is contained in the shortest path
                if (shortestPath.path.contains(new Point(x, y))) image.setRGB(x, y, 0xFF0000FF);

                if (x == startPosX && y == startPosY) image.setRGB(x, y, 0xFFFF0000); // draw start pos
                if (x == endPosX && y == endPosY) image.setRGB(x, y, 0xFF00FF00); // draw end pos
            }
        }

        // return the finished image
        return image;
    }
    public void advance(boolean[][] map) {
        ArrayList<Path> paths = new ArrayList<>(this.paths);
        for (Path path : paths) {
            path.advance(map);
        }
    }
    public Path contains(int x, int y) {
        for (int i = 0; i < taken.size(); i++) {
            Point point = taken.get(i);
            if (point.x == x && point.y == y) return takenPath.get(i);
        }
        return null;
    }
    public class Path {
        public ArrayList<Point> path = new ArrayList<>();
        public void advance(boolean[][] map) {
            // advance path in all directions
            paths.remove(this);
            advanceDirection(map, -1, 0);
            advanceDirection(map, 1, 0);
            advanceDirection(map, 0, -1);
            advanceDirection(map, 0, 1);
        }
        public void advanceDirection(boolean[][] map, int dirX, int dirY) {
            Point current = path.get(path.size() - 1);

            // add the direction to current position
            int x = current.x;
            int y = current.y;
            x += dirX;
            y += dirY;

            // check if out of bounds
            if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) return;

            // check if target is solid or not
            boolean target = map[x][y];
            Point targetPoint = new Point(x, y);
            if (!target) { // not solid, so add to path
                // check if the point is contained in other path, to improve performance
                if (contains(x, y) != null) return;

                // finally, add the point to the path
                Path copy = copy();
                copy.path.add(targetPoint);
                paths.add(copy);

                // assign points to paths
                takenPath.add(this);
                taken.add(targetPoint);
            }
        }
        public Path copy() {
            Path copy = new Path();
            copy.path.addAll(path);
            return copy;
        }
    }
}
