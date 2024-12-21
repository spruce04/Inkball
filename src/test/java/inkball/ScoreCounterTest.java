package inkball;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
* This class will administer tests for our ScoreCounterTest class
*/
public class ScoreCounterTest {

    @Test
    public void simpleTest() { //a test which will test the functionality of our Score Counter
        ScoreCounter scoreCounter = new ScoreCounter(1, 1);

        //first, check that scoreCounter has the correct x and y values
        assertEquals(scoreCounter.getX(), 1, "ScoreCounter's x value isn't working as it should");
        assertEquals(scoreCounter.getY(), 1, "ScoreCounter's y value isn't working as it should");

        //now check that scoreCounter's score adjusts as expected
        scoreCounter.changeScore(100);
        assertEquals(scoreCounter.getScore(), 100, "Error when trying to add 100 to the base score");
        scoreCounter.changeScore(-88);
        assertEquals(scoreCounter.getScore(), 12, "Error when trying to change the score by a negative value");
    }
}