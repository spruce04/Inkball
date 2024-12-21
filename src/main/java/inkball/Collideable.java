package inkball;

/**
* An interface for every object that a ball could potentially collide with
*/
public interface Collideable {
    /**
    * Check if a particular line is set for collision with a given ball
    * @param ball the ball that we are checking if anything has collided with
    * @param line the array of points representing a line that we are checking if the ball has collided with
    * @return true if they will collide of false if they will not
    */
    public boolean isColliding(Ball ball, int[][] line);

    /**
    * Check if any line of a wall is set for collision with a given ball
    * @param ball the ball that we are checking if anything has collided with
    */
    public void checkAllPossibleCollisions(Ball ball);

    /**
    * Makes a ball bounce off of a given hitbox/line
    * @param ball the ball we will make bounce
    * @param line the line we are bouncing the ball against
    * @param reference an int that serves as a reference as to what line we bounced off of
    */
    public void bounce(Ball ball, int[][] line, int reference);

    /**
    * An method that returns the distance between 2 'points'
    * @param A the first point, represented as an array of ints
    * @param B the second point, represented as an array of ints
    * @return the distance between the points
    */
    public static double distance(int[] A, float[] B) {
        return Math.sqrt(((B[0]-A[0])*(B[0]-A[0])) + ((B[1]-A[1])*(B[1]-A[1])));
    }

    public static double distance(int[] A, int[] B) {
        return Math.sqrt(((B[0]-A[0])*(B[0]-A[0])) + ((B[1]-A[1])*(B[1]-A[1])));
    }

    public static double distance(double[] A, float[] B) {
        return Math.sqrt(((B[0]-A[0])*(B[0]-A[0])) + ((B[1]-A[1])*(B[1]-A[1])));
    }

    /**
    * An overloaded method that returns the distance between 2 'points'
    * (Overloaded to work with doubles as well)
    * @param A the first point, represented as an array of ints
    * @param B the second point, represented as an array of ints
    * @return the distance between the points
    */
    public static double distance(double[] A, int[] B) {
        return Math.sqrt(((B[0]-A[0])*(B[0]-A[0])) + ((B[1]-A[1])*(B[1]-A[1])));
    }

    /**
     * A method that will return the normalised normal vectors to a line
     * @param line the line to which we take the normal vectors
     * @return an array of doubles that represents the normal vectors of the line
     */
    public static double[][] normalisedNormalVectors(int[][] line) {
        //following the formula from the pdf
        double dx = line[1][0] - line[0][0];
        double dy =  line[1][1] - line[0][1];
        double[][] normalVectors = {{-dy,dx},{dy,-dx}};
        //normalise the normal vectors - Khan Academy used for the general formula
        for (double[] v : normalVectors) {
            double magnitude = Math.sqrt(v[0]*v[0] + v[1]*v[1]);
            v[0] = v[0]/magnitude;
            v[1] = v[1]/magnitude;
        }
        //return the normalised normal vectors
        return normalVectors;
    }

    /**
     * A method that will return the midpoint of a line
     * @param line the line of which we will return the midpoint
     * @return an array of doubles that represents the midpoint of the line
     */
    public static double[] midpoint(int[][] line) {
        return new double[] {(line[0][0]+line[1][0])/2, (line[0][1]+line[1][1])/2};
    }

    /**
     * A method that will return the dot product of 2 vectors
     * @param v the first vector
     * @param v2 the second vector
     * @return an the dot product of the 2 vectors
     */
    public static double dotProduct(float[] v, double[] v2) {
        double toReturn = 0;
        for (int i = 0; i < v.length; i++) {
            toReturn += v[i] * v2[i];
        }
        return toReturn;
    }

}