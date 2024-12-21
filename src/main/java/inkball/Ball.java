package inkball;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* The class for a ball on our game board
*/
public class Ball extends GameObject {
    private int ballType;
    private float xVector;
    private float yVector;
    private float tempXVector;
    private float tempYVector;
    private boolean closeToHole;
    private boolean delete;
    private String colour;
    private boolean pause;
    Random random = new Random();
    //an int array of our 2 possible initial vector values
    private int[] possibleVecors = {-2, 2};

    /**
    * Constructor for our ball
    * @param x the x coordinates of the ball
    * @param y the y coordinates of the ball
    * @param ballType an int representing the colour of the ball
    */
    public Ball(int x, int y, int ballType) {
        super(x, y);
        this.ballType = ballType;
        if (ballType == 0) {
            setImage("ball0");
            this.colour = "grey";
        }
        else if (ballType == 1) {
            setImage("ball1");
            this.colour = "orange";
        }
        else if (ballType == 2) {
            setImage("ball2");
            this.colour = "blue";
        }
        else if (ballType == 3) {
            setImage("ball3");
            this.colour = "green";
        }
        else if (ballType == 4) {
            setImage("ball4");
            this.colour = "yellow";
        }
        this.xVector = possibleVecors[random.nextInt(2)];
        this.yVector = possibleVecors[random.nextInt(2)];
        this.closeToHole = false;
        this.delete = false;//only true if we want to mark the ball for deletion
        this.pause = false;
    }

    /**
     * Returns whether the ball is marked for deletion
     * @return true if the ball is marked for deletion, false if its not
     */
    public boolean getDelete() {
        return this.delete;
    }

    /**
     * Toggles the ball being paused
     */
    public void setPause() {
        if (this.pause == true) {
            this.pause = false;
        }
        else {
            this.pause = true;
        }
    }

    /**
     * Return if the ball is paused
     * @return a boolean value that represents if the ball is paused
     */
    public boolean getPause() {
        return this.pause;
    }

    /**
     * Mark the ball for deletion
     */
    public void setDelete() {
        this.delete = true;
    }

    /**
     * A setter for the colour of the ball
     * @param newColour the new colour of the ball
     */
    public void setColour(String newColour) {
        this.colour = newColour;
    }

    /**
     * Returns the colour of the ball, represented as a String
     * @return the colour of the ball, represented as a String
     */
    public String getColour() {
        return this.colour;
    }


    /**
    * A function to move the ball on the board
    * It changes the ball's x and y values by the value of the ball's x and y vectors
    */
    public void move() {
        if (closeToHole) {
            setX(getX()+getTempXVector());
            setY(getY()+getTempYVector());  
        }
        else {
            setX(getX()+getXVector());
            setY(getY()+getYVector());
        }
    }

    /**
    * Draw the ball
    * @param app the app, which we need to pass in to be have something to draw the ball on
    */
    @Override
    public void draw(App app) {
        //if the ball isn't currently on the board, or is paused, then return
        if (getX() == -1 && getY() == -1) {
            return;
        }
        //check if the ball is colliding with any collideable objects
        //we need to make a second ArrayList so that deletions don't effect our iteration here
        List<Collideable> collideables = new ArrayList<>(app.getAllCollideables());
        for (Collideable col : collideables) {
            col.checkAllPossibleCollisions(this);
        }
        //if the ball is going to collide with the edge of the screen
        if (getY() <= 0 || getY() >= 552) {//top or bottom edge
            setYVector(-1 * getYVector());
        }
        else if (getX() <= 0 || getX() >= 548) {//right or left
            setXVector(-1 * getXVector());
        }
        if (this.pause == false) {
            move();
        }
        String currentImage = getImage();

        //if we are close to a hole, then adjust the size of the ball accordingly
        float f = 100f;
        Hole hole = null;
        for (Hole h : app.getAllMainHoles()) {
            if (h.distanceToBall(this) < f) {
                f = h.distanceToBall(this);
                hole = h;
            }
        }
        if (f < 32) {//if we are close, adjust the size of the ball image and apply the hole's vector
            closeToHole = true;
            float[] vector = hole.vectorFromBall(this);
            setTempXVector(getXVector()+vector[0]);
            setTempYVector(getYVector()+vector[1]);
            if (f <= 8) {
                hole.fall(this, app);
            }
            app.image(app.getImage(currentImage), this.getX(), this.getY() + App.TOPBAR, 0.5f*f, 0.5f*f);
        }
        else {//if we're not close to any hole, draw the ball as normal
            closeToHole = false;
            app.image(app.getImage(currentImage), this.getX(), this.getY() + App.TOPBAR);
        }
    }

    /**
    * A getter for the ball's x vector
    * @return the value of the x vector
    */
    public float getXVector() {
        return this.xVector;
    }

    /**
    * A getter for the ball's y vector
    * @return the value of the y vector
    */
    public float getYVector() {
        return this.yVector;
    }

    /**
    * A setter for the ball's x vector
    * @param number the new value of the x vector
    */
    public void setXVector(float number) {
        this.xVector = number;
    }

    /**
    * A setter for the ball's y vector
    * @param number the new value of the y vector
    */
    public void setYVector(float number) {
        this.yVector = number;
    }

    /**
    * A getter for the ball's temporary x vector (to be used if its attracted by a hole)
    * @return the value of the temporary x vector
    */
    public float getTempXVector() {
        return this.tempXVector;
    }

    /**
    * A getter for the ball's temporary y vector (to be used if its attracted by a hole)
    * @return the value of the temporary y vector
    */
    public float getTempYVector() {
        return this.tempYVector;
    }

    /**
    * A setter for the ball's temporary x vector
    * @param number the new value of the temporary x vector
    */
    public void setTempXVector(float number) {
        this.tempXVector = number;
    }

    /**
    * A setter for the ball's temporary y vector
    * @param number the new value of the temporary y vector
    */
    public void setTempYVector(float number) {
        this.tempYVector = number;
    }

    /**
    * A getter for the ball's future coordinates for collision testing purposes
    * @return an integer array of the ball's future x and y coordinates
    */
    public float[] getBallCollisionCoords() {
        return new float[]{getX()+getXVector()+12, getY()+getYVector()+12};//we have to add 12 to account for the centre of the ball
    }

    /**
    * A getter for the ball's current coordinates
    * @return an integer array of the ball's current x and y coordinates
    */
    public float[] getCurrentBallCoords() {
        return new float[]{getX()+12, getY()+12};//we have to add 12 to account for the centre of the ball
    }

    /**
    * Spawn the ball by setting its x and y coordinates to that of a random spawner
    * @param app the app, which we pass in to select a spawner
    */
    public void spawn(App app) {
        //get a random spawner
        int spawnerNum = app.getAllSpawners().size();
        Spawner chosen = app.getAllSpawners().get(random.nextInt(spawnerNum));
        //match the ball's coords with the spawner's coords
        this.setX(chosen.getX()*32);
        this.setY(chosen.getY()*32);
    }
}