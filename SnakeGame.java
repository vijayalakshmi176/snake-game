import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener, ActionListener {
    private LinkedList<Point> snake;
    private Point food;
    private int direction;
    private boolean isRunning;
    private int score;

    public SnakeGame() {
        // Initialize the game variables
        snake = new LinkedList<>();
        snake.add(new Point(5, 5)); // Initial position
        food = new Point(10, 10); // Initial food position
        direction = KeyEvent.VK_RIGHT; // Initial direction
        isRunning = true;
        score = 0;

        // Set up the game panel
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Start the game loop
        Timer timer = new Timer(200, this); // Reduced speed
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkCollision();
            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
            direction = KeyEvent.VK_LEFT;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
            direction = KeyEvent.VK_RIGHT;
        } else if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
            direction = KeyEvent.VK_UP;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
            direction = KeyEvent.VK_DOWN;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    private void move() {
        // Move the snake based on the current direction
        Point head = new Point(snake.getFirst());
        switch (direction) {
            case KeyEvent.VK_LEFT:
                head.x--;
                break;
            case KeyEvent.VK_RIGHT:
                head.x++;
                break;
            case KeyEvent.VK_UP:
                head.y--;
                break;
            case KeyEvent.VK_DOWN:
                head.y++;
                break;
        }

        // Add the new head to the snake
        snake.addFirst(head);

        // Check if the snake has eaten the food
        if (head.equals(food)) {
            score++;
            generateFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollision() {
        // Check for wall collisions
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= getWidth() / 20 || head.y < 0 || head.y >= getHeight() / 20) {
            isRunning = false;
        }

        // Check for self-collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isRunning = false;
                break;
            }
        }
    }

    private void generateFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(getWidth() / 20);
            y = rand.nextInt(getHeight() / 20);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < snake.size(); i++) {
            Point segment = snake.get(i);
            g.setColor(new Color(0, 200, 0)); // Light green
            g.fillRoundRect(segment.x * 20, segment.y * 20, 20, 20, 10, 10);
        }

        g.setColor(Color.WHITE);
        g.fillOval(food.x * 20, food.y * 20, 20, 20);

        // Display the score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        if (!isRunning) {
            g.setColor(Color.WHITE);
            g.drawString("Game Over!", getWidth() / 2 - 40, getHeight() / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
