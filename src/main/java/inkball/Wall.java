package inkball;

import java.util.Arrays;

/**
* The class for a wall on our game board
*/
public class Wall extends Tile implements Collideable {
    //the points by which we define the wall and its lines for hitbox purposes
    private int[] topLeftPx = new int[2];
    private int[] topRightPx = new int[2];
    private int[] bottomLeftPx = new int[2];
    private int[] bottomRightPx = new int[2];
    private int[][] topLine = new int[2][2];
    private int[][] bottomLine = new int[2][2];
    private int[][] leftLine = new int[2][2];
    private int[][] rightLine = new int[2][2];

    public Wall(int x, int y, int wallType) {
        super(x, y);
        if (wallType == 0) {
            setImage("wall0");
        }
        if (wallType == 1) {
            setImage("wall1");
        }
        if (wallType == 2) {
            setImage("wall2");
        }
        if (wallType == 3) {
            setImage("wall3");
        }
        if (wallType == 4) {
            setImage("wall4");
        }
        //setup the corner arrays (these act as a set of coordinates for us to put into lines)
        this.topLeftPx[0] = x*32;
        this.topLeftPx[1] = y*32;
        this.topRightPx[0] = x*32+32;
        this.topRightPx[1] = y*32;
        this.bottomLeftPx[0] = x*32;
        this.bottomLeftPx[1] = y*32+32;
        this.bottomRightPx[0] = x*32+32;
        this.bottomRightPx[1] = y*32+32;
        //set up the line arrays
        this.topLine[0] = topLeftPx;
        this.topLine[1] = topRightPx;
        this.bottomLine[0] = bottomLeftPx;
        this.bottomLine[1] = bottomRightPx;
        this.leftLine[0] = topLeftPx;
        this.leftLine[1] = bottomLeftPx;
        this.rightLine[0] = topRightPx;
        this.rightLine[1] = bottomRightPx;
    }

    @Override
    /**
    * Check if a particular line is set for collision with a given ball
    * @param ball the ball that we are checking if anything has collided with
    * @param line the array of points representing a line that we are checking if the ball has collided with
    * @return true if they will collide of false if they will not
    */
    public boolean isColliding(Ball ball, int[][] line) {
        //System.out.println(Arrays.deepToString(line));
        if ((Collideable.distance(line[0], ball.getBallCollisionCoords()) + Collideable.distance(line[1], ball.getBallCollisionCoords())) < Collideable.distance(line[0], line[1])+12) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    /**
    * Check if any line of a wall is set for collision with a given ball
    * @param ball the ball that we are checking if anything has collided with
    */
    public void checkAllPossibleCollisions(Ball ball) {
        if (isColliding(ball, topLine)) {
            bounce(ball, topLine, 0);
        }
        else if (isColliding(ball, rightLine)) {
            bounce(ball, rightLine, 1);
        }
        else if (isColliding(ball, bottomLine)) {
            bounce(ball, bottomLine, 2);
        }
        else if (isColliding(ball, leftLine)) {
            bounce(ball, leftLine, 3);
        }
    }

    @Override
    /**
    * Makes a ball bounce off of a given hitbox/line
    * Also changes the colour of the ball if the wall has a certain colour
    * @param ball the ball we will make bounce
    * @param line the line we are bouncing the ball against
    * @param reference an int reference of what wall the ball bounced off
    */
    public void bounce(Ball ball, int[][] line, int reference) {
        //we can do this simply for the boxes - just reflect the ball's vector depending on what edge it bounced off of
        if (reference == 0) {
            ball.setY(ball.getY()-.1f);//this part of the code is to ensure there are no bugs with balls stuck in walls
            ball.setYVector(-1 * ball.getYVector());
        }
        else if (reference == 1) {
            ball.setX(ball.getX()+.1f);
            ball.setXVector(-1 * ball.getXVector());
        }
        else if (reference == 2) {
            ball.setY(ball.getY()+.1f);
            ball.setYVector(-1 * ball.getYVector());
        }
        else {
            ball.setX(ball.getX()-.1f);
            ball.setXVector(-1 * ball.getXVector());
        }

        //After changing the ball's vector, we also need to change it's colour to match that of the wall
        changeBallColour(ball);
    }

    /**
     * Change the colour of the ball to match that of the wall
     * @param ball the ball to change the colour of
     */
    public void changeBallColour(Ball ball) {
        if (getImage().equals("wall1")) {
            ball.setImage("ball1");
            ball.setColour("orange");
        }
        else if (getImage().equals("wall2")) {
            ball.setImage("ball2");
            ball.setColour("blue");
        }
        else if (getImage().equals("wall3")) {
            ball.setImage("ball3");
            ball.setColour("green");
        }
        else if (getImage().equals("wall4")) {
            ball.setImage("ball4");
            ball.setColour("yellow");
        }
    }

    /**
    * Draw the Wall
    */
    @Override
    public void draw(App app) {
        super.draw(app);
    }
}