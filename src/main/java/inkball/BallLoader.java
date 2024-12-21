package inkball;
import java.util.ArrayList;

/**
 * This is a class that manages spawning balls from the .config JSON file for a certain level
 */
public class BallLoader {
    public ArrayList<Ball> toSpawn = new ArrayList<>();
    int spawnInterval;
    Stopwatch stopwatch;

    /**
     * Initialise a new BallLoader and set an arraylist of the balls to be loaded
     * @param balls an array of the colour identity of the balls to be loaded
     * @param spawnInterval the spawn interval of the balls for this particular level
     * @param app the app, which we need to pass in to be able to add to our allBalls array
     */
    BallLoader(String[] balls, int spawnInterval, App app) {
        for (String b : balls) {
            if (b.equals("grey")) {
                this.toSpawn.add(new Ball(-1, -1, 0));
            }
            else if (b.equals("orange")) {
                this.toSpawn.add(new Ball(-1, -1, 1));
            }
            else if (b.equals("blue")) {
                this.toSpawn.add(new Ball(-1, -1, 2));
            }
            else if (b.equals("green")) {
                this.toSpawn.add(new Ball(-1, -1, 3));
            }
            else if (b.equals("yellow")) {
                this.toSpawn.add(new Ball(-1, -1, 4));
            }
        }

        //add the balls to our major list of balls
        for (Ball b : this.toSpawn) {
            app.getAllBalls().add(b);
        }
        
        this.spawnInterval = spawnInterval;
        //setup a new stopwatch for the purpose of spawning in our balls
        this.stopwatch = new Stopwatch(spawnInterval, 170, 45);
        stopwatch.startPreciseTimer();
    }

    /** 
     * Displays the 5 upcoming balls that will be spawned
     * @param app the app which we pass in to be able to draw balls
     */
    public void displayBalls(App app) {
        int size = toSpawn.size();
        if (size > 5) {
            size = 5;
        }
        for (int i = 0; i < size; i++) {
            app.image(app.getImage(toSpawn.get(i).getImage()), 15+i*28, 25);
        }
    }

    /**
     * Adds a ball to the spawn que (for when a ball has fallen into a hole)
     * @param ball the ball to be added to the spawn que
     */
    public void addToQue(Ball ball) {
        toSpawn.add(ball);
    }

    /**
     * Getter for the BallLoader's stopwatch
     * @return the ball loader's stopwatch
     */
    public Stopwatch getStopwatch() {
        return this.stopwatch;
    }

    /**
     * Draw the ball loader
     * @param app the app which we pass in to draw the ball loader on
     */
    public void draw(App app) {
        //we have to reset the stroke weight to draw the rectangle and stopwatch
        app.strokeWeight(1);
        app.rect(10, 15, 150, 40);
        //we have to format this timer to 2 decimal places
        displayBalls(app);
        //spawn the ball if the time is 0
        if (stopwatch.getTime() <= 0) {
            stopwatch.setTime(spawnInterval);
            if (toSpawn.size() > 0) {
                app.text(String.valueOf(String.format("%.1f",stopwatch.getTime())), stopwatch.getX(), stopwatch.getY());
                Ball incoming = toSpawn.remove(0);
                incoming.spawn(app);
            }
        }
        //set the stopwatch time depending on if there are balls left in the que
        if (toSpawn.size() > 0) {
            app.text(String.valueOf(String.format("%.1f",stopwatch.getTime())), stopwatch.getX(), stopwatch.getY());
        }
        else {
            app.text("0", stopwatch.getX(), stopwatch.getY());
        }
    }

}