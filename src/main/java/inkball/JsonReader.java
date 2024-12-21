package inkball;

import processing.data.JSONObject;
import processing.data.JSONArray;
import java.io.*;

/**
* The class for a .json file reader
* This class' purpose is to help us with reading the .json file and running the game from there
*/
public class JsonReader {
    JSONObject json;
    JSONArray levelsArray;
    private int currentLevel = -1;
    JSONObject levelBody;

    /**
    * Constructor for our JSON reader
    * @param json the json that we want the reader to go through
    */
    JsonReader(JSONObject json) {
        this.json = json;
        this.levelsArray = json.getJSONArray("levels");
        this.currentLevel = -1;
    }

    /**
     * Getter for the current level (integer)
     * @return the integer representing what level we are at
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Go to the next level
     */
    public void nextLevel() {
        currentLevel++;
        levelBody = levelsArray.getJSONObject(currentLevel);
    }

    /**
     * Returns the path to the current level txt file
     * @return the path to the current level txt file
     */
    public String currentLevelPath() {
        return levelBody.getString("layout");
    }

    /**
     * Returns the time associated with the current level
     * @return the time that our timer has for the current level
     */
    public int currentLevelTime() {
        return levelBody.getInt("time");
    }

    /**
     * A method to return an array of the balls to spawn in for a file
     * @return An array of String that represents the balls array for a level in our JSON file
     */
    public String[] currentBallsToSpawn() {
        JSONArray b =  levelBody.getJSONArray("balls");
        String[] toReturn = new String[b.size()];
        for (int i = 0; i < b.size(); i++) {
            toReturn[i] = b.getString(i);
        }
        return toReturn;
    }

    /**
    * A method to return the current spawn interval of our levle
    * @return the current spawn interval of our level
    */
    public int currentSpawnInterval() {
        return levelBody.getInt("spawn_interval");
    }

    /**
    * A method that returns a value by which we want to change our score
    * @param success if the ball entered a hole of the right colour or if it didn't
    * @param colour the colour of the ball
    * @return the change in the player's score
    */
    public float scoreChange(boolean success, String colour) {
        int baseScore;
        if (success) {
            baseScore = json.getJSONObject("score_increase_from_hole_capture").getInt(colour);
            return (float) baseScore * levelBody.getFloat("score_increase_from_hole_capture_modifier"); 
        }
        else {//if we are wrong, return the score as a negative value
            baseScore = json.getJSONObject("score_decrease_from_wrong_hole").getInt(colour);
            return (float) -1 * baseScore * levelBody.getFloat("score_decrease_from_wrong_hole_modifier"); 
        }
    }

}
