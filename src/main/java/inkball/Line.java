package inkball;

import java.util.ArrayList;

/**
* A class to implement player-drawn lines on our gameboard
*/
public class Line implements Collideable {

    public ArrayList<int[]> hitboxPoints;
    public ArrayList<int[]> allPoints;
    App app;

    /**
    * The constructor for a line class
    * @param x the x value of the line's initial x coordinate
    * @param y the y value of the line's initial y coordinate
    * @param app the app, which we have to pass in to be able to remove lines
    */
    Line(int x, int y, App app) {
        this.hitboxPoints = new ArrayList<>();
        this.allPoints = new ArrayList<>();
        this.app = app;
        addHitboxToLine(x, y);
    }

    /**
    * Add a new hitbox to our line
    * @param x the x value of the hitbox to add to the line
    * @param y the y value of the hitbox to add to the line
    */
    public void addHitboxToLine(int x, int y) {
        this.hitboxPoints.add(new int[] {x, y});
    }

    /**
    * Add a new point to our line
    * @param x the x value of the point to add to the line
    * @param y the y value of the point to add to the line
    */
    public void addPointToLine(int x, int y) {
        //we can't add only this point, we also have to consider that there will be an extra 5px on each side
        //add all points that could possibly be covered by the line
        for (int i = x-5; i<=x+5; i++) {
            for (int j = y-5; j<=y+5; j++) {
                this.allPoints.add(new int[] {i, j});
            }
        } 
    }

    /**
    * Return the list of all of our hitboxes
    * @return the list of all the hitboxes on our line
    */
    public ArrayList<int[]> getHitboxPoints() {
        return this.hitboxPoints;
    }

    /**
    * Return the list of all of our points
    * @return the list of all the points on our line
    */
    public ArrayList<int[]> getAllPoints() {
        return this.allPoints;
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
        if (Collideable.distance(line[0], ball.getBallCollisionCoords()) + Collideable.distance(line[1], ball.getBallCollisionCoords()) < Collideable.distance(line[0], line[1])+12) {
            return true;
        }
        return false;
    }

    @Override
    /**
    * Check if any points of a line are set for collision with a given ball
    * @param ball the ball that we are checking if anything has collided with
    */
    public void checkAllPossibleCollisions(Ball ball) {
        for (int i = 0; i < this.hitboxPoints.size()-1; i++) {
            //note that we will have to account for the topbar
            int[][] line = {{this.hitboxPoints.get(i)[0],this.hitboxPoints.get(i)[1]-app.TOPBAR},{this.hitboxPoints.get(i+1)[0],this.hitboxPoints.get(i+1)[1]-app.TOPBAR}};
            if (isColliding(ball, line)) {
                bounce(ball, line, -1);
                app.removeLine(this);
            }
        }
    }

    @Override
    /**
    * Makes a ball bounce off of a given hitbox/line
    * @param ball the ball we will make bounce
    * @param line the line we are bouncing the ball against
    * @param reference unneccessary for bouncing of lines (only needed for walls)
    */
    public void bounce(Ball ball, int[][] line, int reference) {
        //First, calcualte the normalised normal vectors
        double[][] nnVectors = Collideable.normalisedNormalVectors(line);
  
        //Now, we have to find the normal vector on the side of the line that we want to use
        //Using the method outlined in the assignment PDF
        double[] midpoint = Collideable.midpoint(line);
        //if this is true, then the first normal vector is the shortest (used step 4 in the assignment pdf for calculations)
        double[] shortestNormal;
        if (Collideable.distance(new double[] {midpoint[0]+nnVectors[0][0], midpoint[1]+nnVectors[0][1]}, ball.getBallCollisionCoords()) < Collideable.distance(new double[] {midpoint[0]+nnVectors[1][0], midpoint[1]+nnVectors[1][1]}, ball.getBallCollisionCoords())) {
            shortestNormal = nnVectors[0];
        }
        else { //else, the second is the shortest
            shortestNormal = nnVectors[1];
        }
        double dotProduct = Collideable.dotProduct(new float[] {ball.getXVector(), ball.getYVector()}, shortestNormal);
        double newXVector = (ball.getXVector()-2*((dotProduct)*shortestNormal[0]));
        double newYVector = (ball.getYVector()-2*((dotProduct)*shortestNormal[1]));

        ball.setXVector((float) newXVector);
        ball.setYVector((float) newYVector);
    }

    /**
    * Draw the line (work in progress)
    * @param app we have to pass in the app to be able to draw the lines
    */
    public void draw(App app) {
        for (int i = 0; i < this.hitboxPoints.size()-1; i++) {
            app.fill(0);//ensure that the colour is black
            //general line() method used from processing docs as stated at the top of the file
            app.strokeWeight(10);
            app.line(this.hitboxPoints.get(i)[0], this.hitboxPoints.get(i)[1], this.hitboxPoints.get(i+1)[0], this.hitboxPoints.get(i+1)[1]);
        }
    }


}