package inkball;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.text.DecimalFormat;
import java.util.Arrays;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * This class will carry out tests on our ball
 */
public class BallTest {

    //stackoverflow used for rounding
    DecimalFormat df = new DecimalFormat("#.#");

    @Test
    /**
     * Test that the movement of the ball is working
     */
    public void moveTest() {
        Ball ball = new Ball(100, 100, 0);
        ball.setXVector(2);
        ball.setYVector(-2);
        ball.move();
        assertEquals(ball.getY(), 98, "The ball's y movement isn't working as expected");
        assertEquals(ball.getX(), 102, "The ball's x movement isn't working as expected");
    }

    @Test
    /**
     * Test that the spawning of balls is working
     */
    public void ballSpawnTest() {
        App app = new App();
        //set the app's config to be the test config
        try {
            app.setConfigPath(URLDecoder.decode(app.getClass().getResource("configTest.json").getPath(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        Ball testBall = new Ball(100, 100, 0);
        testBall.spawn(app);
        assertEquals(app.getAllSpawners().get(1).getX()*32, testBall.getX(), "ball's x value isn't matching the spawner as expected");
        assertEquals(app.getAllSpawners().get(1).getY()*32, testBall.getY(), "ball's y value isn't matching the spawner as expected");
    }

    @Test
    /**
     * Test that the collision of the ball with walls is working
     */
    public void wallCollideTest() {
        Ball ball = new Ball(170, 168, 0);
        ball.setXVector(2);
        ball.setYVector(2);
        //set up a wall for the ball to collide with
        //coordinates will be x: 192, y:192
        Wall wall = new Wall(6, 6, 4);
        //test the collision has worked as expected
        ball.move();
        ball.move();
        wall.checkAllPossibleCollisions(ball);
        ball.move();
        //assert statements
        assertEquals(ball.getXVector(), 2, "The ball's x vector isn't correct after wall collision");
        assertEquals(ball.getYVector(), -2, "The ball's y vector isn't correct after wall collision");
        assertEquals(ball.getX(), 176.0, "The ball's x value isn't correct after wall collision");
        //Stackoverflow used to round the getY()
        assertEquals(Float.parseFloat(df.format(ball.getY())), 169.9f, "The ball's y value isn't correct after wall collision");
        assertEquals(ball.getImage(), "ball4","The ball's image isn't correct after wall collision");
        assertEquals(ball.getColour(), "yellow", "The ball's colour isn't correct after wall collision");
    }
}