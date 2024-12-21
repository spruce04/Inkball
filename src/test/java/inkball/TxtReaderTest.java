package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
* This class will administer tests for our TxtReader class
*/
public class TxtReaderTest {

    @Test
    public void positiveTest() { //a positive test for the TxtReader
        App app = new App();
        String[][] expected =  // this is what we would expect our array result to be
           {{"wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0"},
            {"wall 0", null, "H2M", "H2", null, null, null, null, null, null, null, null, null, null, null, null, "spawner", "wall 0"},
            {"wall 0", null, "H2", "H2", null, null, null, null, null, null, "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0"},
            {"wall 0", null, null, null, null, "spawner", null, null, null, null, "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", "wall 2", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", null, "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, "H3M", "H3", null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, "H3", "H3", null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, "wall 0", "wall 0", "wall 0", null, null, null, null, null, null, null, null, null, "wall 0"},
            {"wall 0", null, null, null, null, "H4M", "H4", null, null, null, null, null, null, "wall 1", "wall 2", "wall 3", "wall 4", "wall 0"},
            {"wall 0", null, null, null, null, "H4", "H4", null, null, null, null, null, null, null, "spawner", null, null, "wall 0"},
            {"wall 0", "H1M", "H1", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0"},
            {"wall 0", "H1", "H1", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0", "wall 0"}};
        String[][] result;
        try {
            result = TxtReader.readTxtFile(this.getClass().getResource("positiveTestLayout.txt").getPath(), app);
            //check that the 2 board layout arrays match - I used the Junit Doc to check how to compare 2 arrays, as mentioned at the top of the file
            assertArrayEquals(expected, result, "The result array doesn't match the expected array");

            //check that the balls have been appropriately initialised
            ArrayList<Ball> allBallsReturned = app.getAllBalls();
            ArrayList<Ball> allBallsTemplate = new ArrayList<Ball>();
            allBallsTemplate.add(new Ball(7*32, 3*32, 3));
            allBallsTemplate.add(new Ball(1*32, 4*32, 1));
            allBallsTemplate.add(new Ball(4*32, 7*32, 2));
            allBallsTemplate.add(new Ball(7*32, 11*32, 4));
            //check that the 2 ball arrays have the same size before we try to iterate through them
            assertEquals(allBallsReturned.size(), allBallsTemplate.size(), "The size of the ball arraylists is not the same");
            for (int i = 0; i < allBallsReturned.size(); i++) {
                assertTrue(equalBalls(allBallsReturned.get(i), allBallsTemplate.get(i)), "The arraylists of balls are not equal");
            }

        } catch (Exception e) {
            System.out.println("?");
        }
    }

    /**
    *A method that returns a boolean value based on if 2 balls are equal based on their type and coordinates
    */
    public boolean equalBalls(Ball ball1, Ball ball2) {
        if (ball1.getX() != ball2.getX()) {
            return false;
        }
        else if (ball1.getY() != ball2.getY()) {
            return false;
        }
        else if (ball1.getImage() != ball2.getImage()) {
            return false;
        }
        return true;
    }
}