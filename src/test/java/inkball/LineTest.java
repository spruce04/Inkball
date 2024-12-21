package inkball;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
* This class will administer tests for our Line class
*/
public class LineTest {

    //stackoverflow used for rounding
    DecimalFormat df = new DecimalFormat("#.##");

    @Test
    /**
     * Test that the collision between a line and a ball is working
     */
    public void testCollision() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        
        app.setup();

        Line testLine = new Line(100, 280, app);

        //test that the line hitboxes are working
        assertEquals(Arrays.toString(testLine.getHitboxPoints().get(0)), Arrays.toString(new int[] {100,280}), "Line initialisation isn't working");
  
        //test that the method to add hitboxes is working
        testLine.addHitboxToLine(220, 281);
        assertEquals(Arrays.toString(testLine.getHitboxPoints().get(1)), Arrays.toString(new int[] {220,281}), "Adding hitboxes to the line isn't working");
        Ball ball = new Ball(170, 168, 0);
        ball.setXVector(2);
        ball.setYVector(2);
        //test ball and line collision
        ball.move();
        ball.move();
        ball.move();
        ball.move();
        ball.move();
        ball.move();
        testLine.checkAllPossibleCollisions(ball);
        //assert statements - stack overflow used for rounding
        assertEquals(Float.parseFloat(df.format(ball.getXVector())), 2.03f, "ball's x vector not bouncing properly");
        assertEquals(Float.parseFloat(df.format(ball.getYVector())), -1.97f, "ball's y vector not bouncing properly");
    }

    @Test
    /**
     * A simple test to ensure that a point (non-hitbox) can be added to the line as expected
     */
    public void addPoint() {
        App app = new App();
        Line testLine = new Line(100, 100, app);
        testLine.addPointToLine(101, 101);
        assertEquals(Arrays.toString(new int[] {96,97}), Arrays.toString(testLine.getAllPoints().get(1)), "points are not correctly added to allPoints");
        assertEquals(testLine.getAllPoints().size(), 121, "a wrong number of points are added to allPoints");
    }
}