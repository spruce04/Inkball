package inkball;

/**
* The class for a hole on our game board
*/
public class Hole extends Tile {

    private boolean mainHole;

    /**
     * Constructor for a hole
     * @param x the hole's x coordinate
     * @param y the hole's y coordinate
     * @param type the type (colour) of the hole
     * @param main if the hole is the one that we will draw or not (we only draw the main hole)
     */
    public Hole(int x, int y, int type, boolean main) {
        super(x, y);
        if (type == 0) {
            setImage("hole0");
        }
        if (type == 1) {
            setImage("hole1");
        }
        if (type == 2) {
            setImage("hole2");
        }
        if (type == 3) {
            setImage("hole3");
        }
        if (type == 4) {
            setImage("hole4");
        }
        this.mainHole = main;
    }

    /**
     * Returns the coordinates of the centre of the hole
     * @return an array of the coordinates of the hole's center
     */
    public double[] getHoleCenter() {
        return new double[] {getX()*App.CELLSIZE+34, getY()*App.CELLSIZE+34};
    }

    /**
     * Check the distance between the hole and a ball
     * @param ball the ball that we want to check the distance with
     * @return the distance between the hole and the ball
     */
    public float distanceToBall(Ball ball) {
        double[] holeCenter = getHoleCenter();
        return (float) Collideable.distance(holeCenter, ball.getCurrentBallCoords());
    }

    /**
     * Get the vector between a ball and this hole
     * @param ball the ball that we want to get the vector from
     * @return the vector from the ball to the hole
     */
    public float[] vectorFromBall(Ball ball) {
        double[] holeCenter = getHoleCenter();
        float[] vector = new float[] {((float)holeCenter[0]-ball.getCurrentBallCoords()[0])*0.05f,(float)(holeCenter[1]-ball.getCurrentBallCoords()[1])*0.05f};
        return vector;
    }

    /**
     * Check if a ball and hole have matching colours
     * @param ball the ball that we are checking against the hole
     * @return true if the colours match, false if they dont
     */
    public boolean sameColour(Ball ball) {
        String ballID = ball.getImage().substring(4);
        String holeID = getImage().substring(4);
        if (ballID.equals(holeID) && !ballID.equals("0")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Used for when a ball falls into a hole
     * @param ball the ball that is falling into the hole
     * @param app the app, which contains our ballLoader
     */
    public void fall(Ball ball, App app) {
        ball.setX(-1);
        ball.setY(-1);
        //if the ball is gray, or the ball and hole colour correspond, then the ball is absorbed and score is added
        if (ball.getImage().equals("ball0") || getImage().equals("hole0") || sameColour(ball)) {
            app.getScoreCounter().changeScore(app.getJsonReader().scoreChange(true, ball.getColour()));
            app.getAllBalls().remove(ball);
        }
        else {
            //else add it back to the que and change the score accordingly
            app.getBallLoader().addToQue(ball);
            app.getScoreCounter().changeScore(app.getJsonReader().scoreChange(false, ball.getColour()));
        }
        
    }

    /**
    * Draw the hole
    */
    @Override
    public void draw(App app) {
        if (this.mainHole) { //we only want to draw one of the four holes
            String currentImage = getImage();
            app.image(app.getImage(currentImage), this.getX()*App.CELLSIZE, this.getY()*App.CELLSIZE + App.TOPBAR);
        }
    }
}