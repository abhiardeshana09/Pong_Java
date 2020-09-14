/**
 * Java Pong Game
 * Author: Abhi Ardeshana
 * Description: Implements a single-player Pong game with a menu bar.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Pong2 extends JPanel implements Runnable, KeyListener, ActionListener, ItemListener {

    // Variables for the ball's coordinates, speed and diameter
    private int ball_x, ball_y, ball_dx, ball_dy, ball_diameter;
    // Variables for the paddle's coordinates, speed, width and height
    private int paddle_x, paddle_y, paddle_dy, paddle_width, paddle_height;
    // Variables to keep track of whether the up key or the down key is being pressed
    private boolean up_pressed, down_pressed;
    // Variable to keep track of whether the player selected the "Show Score" checkbox in the menu bar
    private boolean score_selected = true;
    // Variable to keep track of the player's score
    private int score = 0;
    // Creates an input dialog asking the player for their name, and then stores the input in a string
    private String name = JOptionPane.showInputDialog(null, "Welcome to Pong! Enter your name to start.", "Welcome!",
            JOptionPane.QUESTION_MESSAGE);

    // Constructor
    private Pong2(JFrame frame) {
        // Initialize the JFrame and menu bar
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        // Add the component listener to the JFrame
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // Repositions the ball and the paddle when the screen size is changed
                ball_x = getWidth() / 2;
                ball_y = getHeight() / 2;
                paddle_y = getHeight() / 2;
            }
        });
        // Add the key listener to the JFrame
        frame.addKeyListener(this);
        // Set the screen size
        frame.setSize(1600, 900);
        // Make all the components visible
        frame.setVisible(true);
        // Set the initial values for all the variables
        ball_x = getWidth() / 2;
        ball_y = getHeight() / 2;
        ball_dx = 7;
        ball_dy = 7;
        ball_diameter = 25;
        paddle_x = 100;
        paddle_y = getHeight() / 2;
        paddle_dy = 6;
        paddle_width = 10;
        paddle_height = 100;
    }

    // The "createMenuBar" method creates and returns the JFrame's menu bar
    private JMenuBar createMenuBar() {

        // Create a JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Create the "Speed" menu
        JMenu menu = new JMenu("Speed");
        menu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menu);

        // Create the "Increase" menu item for the speed menu
        JMenuItem menuItem = new JMenuItem("Increase", KeyEvent.VK_I);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        // Create the "Decrease" menu item for the speed menu
        menuItem = new JMenuItem("Decrease", KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Create the "Score" menu
        menu = new JMenu("Score");
        menu.setMnemonic(KeyEvent.VK_N);
        menuBar.add(menu);

        // Create the "Show Score" check box menu item for the score menu
        JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("Show Score", true);
        checkBoxMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        checkBoxMenuItem.addItemListener(this);
        menu.add(checkBoxMenuItem);

        // Return the JMenuBar
        return menuBar;
    }

    // The "run" method maintains the game loop
    public void run() {
        // Creates an Insets object for use in the collision detection algorithms
        Insets i = getInsets();
        while (true) {
            // Moves the ball based on its speed variables
            ball_x += ball_dx;
            ball_y += ball_dy;
            // Calls the gameOver() method if the ball goes off the left side of the screen
            if (ball_x < i.left) {
                gameOver();
            }
            // Bounces the ball if a collision occurs between the ball and the right side of the screen
            if (ball_x + ball_diameter > getWidth() - i.right) {
                ball_dx = -ball_dx;
            }
            // Bounces the ball if a collision occurs between the ball and the top or the bottom of the screen
            if (ball_y < i.top || ball_y + ball_diameter > getHeight() - i.bottom) {
                ball_dy = -ball_dy;
            }
            // Moves the paddle up if the "up" key is being pressed and if there is space available
            if (up_pressed && paddle_y > i.top) {
                paddle_y -= paddle_dy;
            }
            // Moves the paddle down if the "down" key is being pressed and if there is space available
            if (down_pressed && paddle_y + paddle_height < getHeight() - i.bottom) {
                paddle_y += paddle_dy;
            }
            // Bounces the ball and increments the score if a collision occurs between the ball and the paddle
            if (ball_y + ball_diameter > paddle_y && ball_y < paddle_y + paddle_height &&
                    ball_x > paddle_x && ball_x < paddle_x + paddle_width) {
                score++;
                ball_dx = -ball_dx;
            }
            // Repaints the JPanel
            repaint();
            // Sleeps the thread for 10 milliseconds
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {}
        }
    }

    // The "paint" method uses the double buffering technique to draw the ball, paddle and score onto the screen
    public void paint(Graphics g) {
        // Creates a back buffer and obtains the back buffer's graphics object
        Image backBuffer = createImage(getWidth(), getHeight());
        Graphics backBufferGraphics = backBuffer.getGraphics();
        // Sets the color to black anc clears the back buffer's background
        backBufferGraphics.setColor(Color.BLACK);
        backBufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        // Sets the color to white and draws the ball and the paddle onto the back buffer
        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.fillOval(ball_x, ball_y, ball_diameter, ball_diameter);
        backBufferGraphics.fillRect(paddle_x, paddle_y, paddle_width, paddle_height);
        // Sets the font and draws the score onto the back buffer if the "Show Score" checkbox is selected
        if (score_selected) {
            backBufferGraphics.setFont(new Font("Arial", Font.BOLD, 40));
            backBufferGraphics.drawString("Score: " + score, getWidth() - 190, 65);
        }
        // Displays the back buffer using the JPanel's graphics object
        g.drawImage(backBuffer, 0, 0, this);
    }

    // The "keyPressed" method is called whenever the player pressed a key
    public void keyPressed(KeyEvent e) {
        // Sets the up_pressed variable to "true" if the player pressed the "up" key
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up_pressed = true;
        }
        // Sets the down_pressed variable to "true" if the player pressed the "down" key
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down_pressed = true;
        }
    }

    // The "keyReleased" method is called whenever the player releases a key
    public void keyReleased(KeyEvent e) {
        // Sets both variables to "false" to indicate that the player is no longer pressing a key
        up_pressed = false;
        down_pressed = false;
    }

    public void keyTyped(KeyEvent e) {}

    // The "actionPerformed" method is called whenever the player selects the "Increase" or "Decrease" menu options
    public void actionPerformed(ActionEvent e) {
        // Obtains the source of the action event
        JMenuItem source = (JMenuItem)(e.getSource());
        // Increases the x and y speed of the ball if the user selected the "Increase" menu option
        if (source.getText().equals("Increase")) {
            if (ball_dx > 0) ball_dx++;
            else ball_dx--;
            if (ball_dy > 0) ball_dy++;
            else ball_dy--;
        }
        // Decreases the x and y speed of the ball if the user selected the "Decrease" menu option
        if (source.getText().equals("Decrease")) {
            if (ball_dx > 1) ball_dx--;
            else if (ball_dx < -1) ball_dx++;
            if (ball_dy > 1) ball_dy--;
            else if (ball_dy < -1) ball_dy++;
        }
    }

    // The "itemStateChanged" method is called whenever the player selects the "Show Score" menu option
    public void itemStateChanged(ItemEvent e) {
        // Sets the score_selected variable to "false" if the player deselected the checkbox
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            score_selected = false;
        }
        // Sets the score_selected variable to "true" if the player selected the checkbox
        else {
            score_selected = true;
        }
    }

    // The "gameOver" method is called when the player loses
    private void gameOver() {
        try {
            // Creates a FileWriter object
            FileWriter w = new FileWriter("Score List.txt", true);
            // Saves the player's name along with their score to the "Score List" text file
            w.write("\n" + name + ": " + score);
            // Closes the FileWriter
            w.close();
        }
        catch (IOException e) {
            System.out.println("Could not save the score to the file.");
        }
        // Creates a message dialog showing the player their final score
        JOptionPane.showMessageDialog(null, "Ouch! Your final score was " + score + ".", "Game Over!",
                JOptionPane.INFORMATION_MESSAGE);
        // Ends the program
        System.exit(0);
    }

    // The "main" method is called when the program starts
    public static void main(String[] args) {
        // Creates a new JFrame
        JFrame frame = new JFrame("Pong 2");
        // Creates a new instance of the Pong game
        Pong2 app = new Pong2(frame);
        // Starts the game's loop
        new Thread(app).start();
    }
}