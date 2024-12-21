package inkball;

/**
 * A class to implement a brick on our gameboard
 */
public class Brick extends Wall {

    int hitCount;
    int wallType;
    boolean delete;

    /**
     * Constructor for a brick
     * @param x the brick's x coordinate
     * @param y the brick's y coordinate
     * @param wallType the brick's colour/type
     */
    public Brick(int x, int y, int wallType) {
        super(x, y, wallType);
        this.hitCount = 0;
        this.wallType = wallType;
        changeSprite(0, wallType);
        this.delete = false;
    }

    /**
     * If the brick is hit by a ball
     */
    public void brickHit() {
        hitCount++;
        changeSprite(hitCount, wallType);
    }

    @Override
    /**
    * Check if a particular line is set for collision with a given ball
    * If the brick is set for collision, we increment its collision counter
    * @param ball the ball that we are checking if anything has collided with
    * @param line the array of points representing a line that we are checking if the ball has collided with
    * @return true if they will collide of false if they will not
    */
    public boolean isColliding(Ball ball, int[][] line) {
        //System.out.println(Arrays.deepToString(line));
        if ((Collideable.distance(line[0], ball.getBallCollisionCoords()) + Collideable.distance(line[1], ball.getBallCollisionCoords())) < Collideable.distance(line[0], line[1])+12) {
            if (matches(ball, this)) {//if the ball is gray or matches the brick, we "break" the brick
                brickHit();
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Change the brick's sprite according to its colour and the number of times its been hit
     * @param hCount the number of times the brick has been hit
     * @param wType the colour of the brick
     */
    public void changeSprite(int hCount, int wType) {
        if (hCount >= 3) {//if the brick has been hit over 3 times, its time to delete it
            delete = true;
            return;
        }

        if (wType == 0) {
            if (hCount == 0) {
                setImage("wall0");
            }
            else if (hCount < 3) {
                setImage("graybrick");
            }
        }
        else if (wType == 1) {
            if (hCount == 0) {
                setImage("wall1");
            }
            else if (hCount < 3) {
                setImage("orangebrick");
            }
        }
        else if (wType == 2) {
            if (hCount == 0) {
                setImage("wall2");
            } 
            else if (hCount < 3) {
                setImage("bluebrick");
            }  
        }
        else if (wType == 3) {
            if (hCount == 0) {
                setImage("wall3");
            }
            else if (hCount < 3) {
                setImage("greenbrick");
            }
        }
        else if (wType == 4) {
            if (hCount == 0) {
                setImage("wall4");
            }
            else if (hCount < 3) {
                setImage("yellowbrick");
            }
        }
    }

    /**
     * See if the brick and the ball's colour matches
     * @param ball the ball whose colour we want to check
     * @param brick the brick whose colour we want to check
     * @return true if the colour's match, false if not
     */
    public boolean matches(Ball ball, Brick brick) {
        if (ball.getImage().equals("ball0") || brick.getImage().equals("graybrick") || brick.getImage().equals("wall0")) {
            return true;
        }
        else if (ball.getColour().equals(brick.getImage().substring(0, brick.getImage().length() - 5))) {
            return true;
        }
        else if (ball.getImage().substring(4).equals(brick.getImage().substring(4))) {
            return true;
        }
        return false;
    }

    @Override
    /**
     * Change the ball colour to match that of the brick
     */
    public void changeBallColour(Ball ball) {
        if (getImage().equals("orangebrick") || getImage().equals("wall1")) {
            ball.setImage("ball1");
            ball.setColour("orange");
        }
        else if (getImage().equals("bluebrick") || getImage().equals("wall2")) {
            ball.setImage("ball2");
            ball.setColour("blue");
        }
        else if (getImage().equals("greenbrick" ) || getImage().equals("wall3")) {
            ball.setImage("ball3");
            ball.setColour("green");
        }
        else if (getImage().equals("yellowbrick") || getImage().equals("wall4")) {
            ball.setImage("ball4");
            ball.setColour("yellow");
        }
    }

    /**
     * Draw the brick
     */
    @Override
    public void draw(App app) {
        if (delete) {//if the brick is marked for deletion, delete it
            app.getAllCollideables().remove(this);
            //replace the brick with a tile
            app.getBoard()[(int)getY()][(int)getX()] = new Tile((int)getX(), (int)getY());
        }
        else {
            super.draw(app);
        }
    }
}