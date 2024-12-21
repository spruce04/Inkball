package inkball;

import processing.core.PApplet;
import processing.event.KeyEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
* This class will administer tests for our App class
*/
public class AppTest {
    static App app = new App();

    @BeforeAll
    /**
     * Set up the app for all of our tests
     */
    public static void setupApp() {
        //set up the app with a path to a specifc config file for testing purposes
        try {
            app.setConfigPath(URLDecoder.decode(app.getClass().getResource("configTest.json").getPath(), StandardCharsets.UTF_8.name()));
            PApplet.runSketch(new String[] { "App" }, app);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    /**
     * Test for the app's set up of the game board
     */
    public void setupTest() { // a positive test for the setup() method
        //call the setup() function
        app.setup();
        
        //test that the setup is working by using an array of expected string values
        //these values should correspond to the .getImage() values of our sprites in the actual gameboard array, which is what we will test  
        String[][] expectedString = 
        {{"wall0", "wall0", "wall0", "wall0", "wall0", "wall0", "wall0", "wall2", "wall2", "wall2", "wall2", "wall0", "wall0", "wall0", "wall0", "wall0", "wall0", "wall0"},
        {"wall0", "tile", "tile", "tile", "bluebrick", "graybrick", "greenbrick", "yellowbrick", "orangebrick", "tile", "tile", "tile", "tile", "tile", "tile", "hole0", "hole0", "wall0"},
        {"wall0", "tile", "tile", "tile", "bluebrick", "graybrick", "greenbrick", "yellowbrick", "orangebrick", "tile", "tile", "tile", "tile", "tile", "tile", "hole0", "hole0", "wall0"},
        {"wall0", "tile", "tile", "tile", "bluebrick", "graybrick", "greenbrick", "yellowbrick", "orangebrick", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "wall0", "wall0", "tile", "tile", "tile", "wall0", "hole1", "hole1", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "wall0", "tile", "wall0", "wall0", "tile", "hole1", "hole1", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "entrypoint", "wall0", "wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "wall0", "tile", "wall0", "wall0", "tile", "hole2", "hole2", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "wall0", "wall0", "tile", "tile", "tile", "wall0", "hole2", "hole2", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "hole0", "hole0", "wall0"},
        {"wall0", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "tile", "hole0", "hole0", "wall0"},
        {"wall0", "wall0", "wall0", "wall0", "wall0", "wall0", "wall0", "wall1", "wall1", "wall1", "wall1", "wall0", "wall0", "wall0", "wall0", "wall0", "wall0", "wall0"}};
        
        //for each brick we have, hit it once
        for (Collideable c : app.getAllCollideables()) {
            if (c instanceof Brick) {
                ((Brick)c).brickHit();
            }
        }
      
        //test that everything is working as it should
        for (int i = 0; i < 18; i++) {
            for (int y = 0; y < 18; y++) {
                assertEquals(expectedString[i][y], app.getBoard()[i][y].getImage(),  "error at index y: " + i + " x: " + y);
            }
        }
    }

    @Test
    /**
     * Test that the pause button is working
     */
    public void pauseTest() {
        //try to pause the app and see if this works - code for this is outlined by Ankit on the ed forum
        app.setup();
        KeyEvent pause = new KeyEvent(app, 0, KeyEvent.RELEASE, 0, ' ', ' ');
        app.setPaused(false);
        app.keyReleased(pause);

        //test that the pause button has worked as intended
        for (Ball b : app.getAllBalls()) {
            assertTrue(b.getPause(), "ball isn't properly pausing");
        }
        assertTrue(app.getPaused(), "app isn't properly pausing");
        assertFalse(app.getStopwatch().getUpdateTime(), "stopwatch isn't properly pausing");
        assertFalse(app.getBallLoader().getStopwatch().getUpdateTime(), "ball loading stopwatch isn't properly pausing");

        //test that the app also unpauses
        app.keyReleased(pause);
        for (Ball b : app.getAllBalls()) {
            assertFalse(b.getPause(), "ball isn't properly unpausing");
        }
        assertFalse(app.getPaused(), "app isn't properly unpausing");
        assertTrue(app.getStopwatch().getUpdateTime(), "stopwatch isn't properly pausing");
        assertTrue(app.getBallLoader().getStopwatch().getUpdateTime(), "ball loading stopwatch isn't properly unpausing");
    }

    @Test
    /**
     * A test to make sure the game goes to the next level as expected
     */
    public void nextLevelTest() {
        app.setup();
        app.getJsonReader().nextLevel();
        //check that we are on the second level
        assertEquals(1, app.getJsonReader().getCurrentLevel(), "Not going to the next level as expected");
    }
}