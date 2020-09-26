# Pong_Java

This program implements a singleplayer Pong game in Java using the Swing GUI toolkit.

![javapong](https://i.imgur.com/HGjcnOH.jpg)

Upon startup, the program asks the user for their name. When the game ends, the user's name, along with their score, are recorded in a text file. The program uses a try catch block to handle a possible IOException when the score is being saved.

The game also includes a menu bar, which allows the user to increase or decrease the speed of the ball, as well as to show or hide their score.

The program uses double buffering to display objects to the screen. That is, the objects to be displayed in the next frame are first drawn onto a back buffer. Then, the back buffer is drawn onto the screen as an entire image. This helps improve the smoothness of the animation.

The game uses a KeyListener to detect when the arrow keys are pressed, and to move the paddle accordingly. If the user fails to move the paddle enough and fails to return the ball, the game ends.
