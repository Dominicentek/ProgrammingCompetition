package com.soutez.app;

import com.soutez.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class PacmanApp implements App {
    public static final int GAME_STATE_PLAYING = 0;
    public static final int GAME_STATE_WIN = 1;
    public static final int GAME_STATE_LOSE = 2;
    public static final int MOVEMENT_DIRECTION_NONE = -1;
    public static final int MOVEMENT_DIRECTION_UP = 0;
    public static final int MOVEMENT_DIRECTION_LEFT = 1;
    public static final int MOVEMENT_DIRECTION_DOWN = 2;
    public static final int MOVEMENT_DIRECTION_RIGHT = 3;
    public boolean up = false;
    public boolean left = false;
    public boolean down = false;
    public boolean right = false;
    public ArrayList<Point> objects = new ArrayList<>();
    public int tickTimeout = 0;
    public Point player = new Point(9, 10);
    public Point playerDraw = new Point(player.x * 16, player.y * 16);
    public int playerMovementDir = MOVEMENT_DIRECTION_NONE;
    public Point enemy = new Point(1, 1);
    public Point prevEnemy = new Point(enemy);
    public int enemyMovementDir = MOVEMENT_DIRECTION_NONE;
    public int gameState = GAME_STATE_PLAYING;
    public int playerMovement = 0;
    public BufferedImage run(Map town, Map pacman, String[] dictionary) {
        // define and parse map
        boolean[][] map = new boolean[pacman.width][pacman.height];
        for (int x = 0; x < town.width; x++) {
            for (int y = 0; y < town.height; y++) {
                map[x][y] = pacman.get(x, y) == 'X';
            }
        }

        // generate the pacman window
        JFrame frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(pacman.width * 16, pacman.height * 16));
        frame.pack();
        frame.setLocation(100, 100);
        frame.add(new JPanel() {
            public void paint(Graphics g) {
                // draw black bg
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, frame.getContentPane().getPreferredSize().width, frame.getContentPane().getPreferredSize().height);

                // draw level
                g.setColor(Color.BLUE);
                for (int x = 0; x < pacman.width; x++) {
                    for (int y = 0; y < pacman.height; y++) {
                        if (map[x][y]) g.fillRect(x * 16, y * 16, 16, 16);
                    }
                }

                // draw objects
                g.setColor(Color.WHITE);
                for (Point object : objects) {
                    g.fillOval(object.x * 16 + 4, object.y * 16 + 4, 8, 8);
                }

                // interpolate positions
                double t = tickTimeout / 16.0;
                int enemyX = (int)((prevEnemy.x * (1 - t) + enemy.x * t) * 16);
                int enemyY = (int)((prevEnemy.y * (1 - t) + enemy.y * t) * 16);

                if (new Rectangle(enemyX, enemyY, 8, 8).intersects(playerDraw.x, playerDraw.y, 8, 8)) gameState = GAME_STATE_LOSE;

                // draw player
                g.setColor(Color.YELLOW);
                g.fillOval(playerDraw.x + 4, playerDraw.y + 4, 8, 8);

                // draw enemy
                g.setColor(Color.RED);
                g.fillOval(enemyX + 4, enemyY + 4, 8, 8);

                // Draw game state texts
                g.setFont(g.getFont().deriveFont(48f));
                if (gameState == GAME_STATE_WIN) {
                    g.setColor(Color.GREEN);
                    String string = "You win!";
                    Rectangle2D bounds = g.getFontMetrics().getStringBounds(string, g);
                    g.drawString(string, (int)(frame.getContentPane().getPreferredSize().width / 2 - bounds.getWidth() / 2), frame.getContentPane().getPreferredSize().height / 2);
                }
                if (gameState == GAME_STATE_LOSE) {
                    g.setColor(Color.RED);
                    String string = "You lost!";
                    Rectangle2D bounds = g.getFontMetrics().getStringBounds(string, g);
                    g.drawString(string, (int)(frame.getContentPane().getPreferredSize().width / 2 - bounds.getWidth() / 2), frame.getContentPane().getPreferredSize().height / 2);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) up = true;
                if (e.getKeyCode() == KeyEvent.VK_S) down = true;
                if (e.getKeyCode() == KeyEvent.VK_A) left = true;
                if (e.getKeyCode() == KeyEvent.VK_D) right = true;
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) up = false;
                if (e.getKeyCode() == KeyEvent.VK_S) down = false;
                if (e.getKeyCode() == KeyEvent.VK_A) left = false;
                if (e.getKeyCode() == KeyEvent.VK_D) right = false;
            }
        });
        frame.setResizable(false);
        frame.setVisible(true);

        // spawn 10 objects
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            while (true) { // loop until it finds a valid spot
                int x = random.nextInt(pacman.width);
                int y = random.nextInt(pacman.height);
                Point location = new Point(x, y);
                if (map[x][y]) continue; // generated spot is solid
                if (objects.contains(location)) continue; // generated spot already contains an object
                objects.add(location); // add the object to the object list
                break;
            }
        }

        // main game loop
        try {
            while (gameState == GAME_STATE_PLAYING) {
                long time = System.currentTimeMillis();

                // handle movement
                if (playerMovement >= 0) {
                    playerMovement++;
                    if (playerMovement == 16) playerMovement = -1;
                    if (playerMovementDir == MOVEMENT_DIRECTION_UP) playerDraw.y--;
                    if (playerMovementDir == MOVEMENT_DIRECTION_LEFT) playerDraw.x--;
                    if (playerMovementDir == MOVEMENT_DIRECTION_DOWN) playerDraw.y++;
                    if (playerMovementDir == MOVEMENT_DIRECTION_RIGHT) playerDraw.x++;
                }
                else {
                    if (up && !map[player.x][player.y - 1]) {
                        playerMovement = 0;
                        playerMovementDir = MOVEMENT_DIRECTION_UP;
                        player.y--;
                    }
                    else if (left && !map[player.x - 1][player.y]) {
                        playerMovement = 0;
                        playerMovementDir = MOVEMENT_DIRECTION_LEFT;
                        player.x--;
                    }
                    else if (down && !map[player.x][player.y + 1]) {
                        playerMovement = 0;
                        playerMovementDir = MOVEMENT_DIRECTION_DOWN;
                        player.y++;
                    }
                    else if (right && !map[player.x + 1][player.y]) {
                        playerMovement = 0;
                        playerMovementDir = MOVEMENT_DIRECTION_RIGHT;
                        player.x++;
                    }
                }

                // handle enemy
                tickTimeout++;
                if (tickTimeout == 16) {
                    tickTimeout = 0;

                    // check if player collided with object
                    for (Point object : new ArrayList<>(objects)) {
                        if (object.x == player.x && object.y == player.y) {
                            objects.remove(object);
                            if (objects.isEmpty()) gameState = GAME_STATE_WIN;
                        }
                    }
                    if (gameState == GAME_STATE_WIN) break;

                    // set previous position of enemy
                    prevEnemy = new Point(enemy.x, enemy.y);

                    // handle enemy movement
                    double minDistance = Double.MAX_VALUE;
                    int moveDir = MOVEMENT_DIRECTION_NONE;
                    int backwards = invertMovDir(enemyMovementDir);
                    if (enemyDistanceToPlayer(0, -1) < minDistance && backwards != MOVEMENT_DIRECTION_UP && !map[enemy.x][enemy.y - 1]) {
                        minDistance = enemyDistanceToPlayer(0, -1);
                        moveDir = MOVEMENT_DIRECTION_UP;
                    }
                    if (enemyDistanceToPlayer(1, 0) < minDistance && backwards != MOVEMENT_DIRECTION_LEFT && !map[enemy.x - 1][enemy.y]) {
                        minDistance = enemyDistanceToPlayer(-1,  0);
                        moveDir = MOVEMENT_DIRECTION_LEFT;
                    }
                    if (enemyDistanceToPlayer(0, 1) < minDistance && backwards != MOVEMENT_DIRECTION_DOWN && !map[enemy.x][enemy.y + 1]) {
                        minDistance = enemyDistanceToPlayer(0, 1);
                        moveDir = MOVEMENT_DIRECTION_DOWN;
                    }
                    if (enemyDistanceToPlayer(1, 0) < minDistance && backwards != MOVEMENT_DIRECTION_RIGHT && !map[enemy.x + 1][enemy.y]) {
                        minDistance = enemyDistanceToPlayer(1, 0);
                        moveDir = MOVEMENT_DIRECTION_RIGHT;
                    }
                    enemyMovementDir = moveDir;
                    if (moveDir == MOVEMENT_DIRECTION_UP) enemy.y--;
                    if (moveDir == MOVEMENT_DIRECTION_LEFT) enemy.x--;
                    if (moveDir == MOVEMENT_DIRECTION_DOWN) enemy.y++;
                    if (moveDir == MOVEMENT_DIRECTION_RIGHT) enemy.x++;
                }
                Thread.sleep(Math.max(0, time - System.currentTimeMillis() + 16)); // lock to 60 FPS
                frame.repaint(); // draw next frame
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        frame.repaint(); // update the frame
        return null;
    }
    public double enemyDistanceToPlayer(int dirX, int dirY) {
        return Math.sqrt(Math.pow(enemy.x + dirX - player.x, 2) + Math.pow(enemy.y + dirY - player.y, 2));
    }
    public int invertMovDir(int dir) {
        if (dir == MOVEMENT_DIRECTION_UP) return MOVEMENT_DIRECTION_DOWN;
        if (dir == MOVEMENT_DIRECTION_LEFT) return MOVEMENT_DIRECTION_RIGHT;
        if (dir == MOVEMENT_DIRECTION_DOWN) return MOVEMENT_DIRECTION_UP;
        if (dir == MOVEMENT_DIRECTION_RIGHT) return MOVEMENT_DIRECTION_LEFT;
        return MOVEMENT_DIRECTION_NONE;
    }
}
