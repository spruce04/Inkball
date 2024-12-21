package inkball;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
* A class to test the functionality of our holes
*/
public class HoleTest {
    public static App app = new App();

    @BeforeAll
    /**
     * Setup the app for testing
     */
    public static void setup() {
        //set up the app with a path to a specifc config file for testing purposes
        try {
            app.setConfigPath(URLDecoder.decode(app.getClass().getResource("configTest.json").getPath(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    /**
     * Test that a ball falling into a hole works as intended
     */
    public void testFall() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        Hole hole = new Hole(1, 1, 2, true);
        Ball ball = new Ball(200, 200, 2);
        Ball ballTwo = new Ball(200, 200, 3);
        hole.fall(ball, app);
        //assert statements
        assertEquals(app.getScoreCounter().getScore(), 50, "first ball falling into hole isn't working properly");
        assertEquals(14, app.getAllBalls().size(), "ball being removed from the game isn't working properly.  \n" + //
                        "Please note that the testlvl.txt file is needed for this testcase.\n");
        hole.fall(ballTwo, app);
        assertEquals(app.getScoreCounter().getScore(), 25, "second ball falling into hole isn't working properly");
        assertEquals(7, app.getBallLoader().toSpawn.size(), "ball being removed from the que isn't working properly.  \n" + //
                        "Please note that the testlvl.txt file is needed for this testcase.\n");
    }
}