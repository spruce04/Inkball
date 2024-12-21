package inkball;

/**
* A simple class to count and display the player's score
*/
public class ScoreCounter {
    private float score;
    private float restartScore;
    private int x;
    private int y;

    /**
    * Constructor to initialise a new score counter
    * @param x the x coordinate
    * @param y the y coordinate
    */
    ScoreCounter(int x, int y) {
        this.score = 0;
        this.restartScore = 0;
        this.x = x;
        this.y = y;
    }

    /**
    * A method to return our current score
    * @return the current value of this.score
    */
    public float getScore() {
        return this.score;
    }

    /**
    * A method to change the value of our current score
    * @param changeBy the amount by which we want to change the score
    */
    public void changeScore(float changeBy) {
        this.score += changeBy;
    }

    /**
    * A method to set the value of our current score
    * @param newScore the amount to which we want to set the score
    */
    public void setScore(float newScore) {
        this.score = newScore;
    }

    /**
     * Get the restart score
     * @return the current value of the restart score
     */
    public float getRestartScore() {
        return this.restartScore;
    }

    /**
     * Set the restart score
     * @param newScore the new value of the restart score
     */
    public void setRestartScore(float newScore) {
        this.restartScore = newScore;
    }

    /**
    * A method to return our x coordinate
    * @return the current value of this.x
    */
    public int getX() {
        return this.x;
    }

    /**
    * A method to return our y coordinate
    * @return the current value of this.y
    */
    public int getY() {
        return this.y;
    }
}