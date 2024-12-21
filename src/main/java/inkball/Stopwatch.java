package inkball;
import java.util.Timer;
import java.util.TimerTask;

//Task class to schedule our timer - in the same file as they are closely related
class Task extends TimerTask {
    Stopwatch stopwatch;

    /**
     * Constructor where we pass in our stopwatch
     * @param stopwatch the stopwatch which we need to modify
     */
    public Task(Stopwatch stopwatch) {
         this.stopwatch = stopwatch;
    }

    /**
     * Method to run once every second - decrease the stopwatch value by one
     */
    public void run() {
        if (stopwatch.getUpdateTime() == false) {
            return;
        }
        if (this.stopwatch.getTime() > 0) {
            this.stopwatch.setTime(this.stopwatch.getTime()-1);
        }
        else {
            return;
        }
        
    }
}

/**
* A task to be used if we need a more precise time increment
*/
class PreciseTask extends TimerTask {
    Stopwatch stopwatch;

    /**
     * Constructor where we pass in our stopwatch
     * @param stopwatch the stopwatch which we need to modify
     */
    public PreciseTask(Stopwatch stopwatch) {
         this.stopwatch = stopwatch;
    }

    /**
     * Method to run ten times every second - decrease the stopwatch value by 0.1
     */
    public void run() {
        if (stopwatch.getUpdateTime() == false) {
            return;
        }
        if (this.stopwatch.getTime() > 0) {
            this.stopwatch.setTime(this.stopwatch.getTime()-0.1f);
        }
        else {
            return;
        }
        
    }
}

/**
* A class that will execute our timer for each level
*/
public class Stopwatch {
    private int x;
    private int y;
    private float time;
    private Timer timer = new Timer();//create a new timer
    private Task task = new Task(this);//create a new timer task, pass in our current instance of stopwatch as the param
    private PreciseTask preciseTask = new PreciseTask(this);
    private boolean started = false;
    private boolean updateTime = true;
    private float snapshot;

    /**
     * Constructor for the stopwatch
     * @param time the time we initialise the stopwatch at
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Stopwatch(int time, int x, int y) {
        this.time = time + 1;
        this.x = x;
        this.y = y;
        this.started = started;
        this.snapshot = snapshot;
        this.updateTime = updateTime;
    }

    /**
     * Getter for our current time
     * @return the current time of the stopwatch
     */
    public float getTime() {
        if (updateTime) {
            return this.time;
        }
        else {
            return this.snapshot;
        }
        
    }

    /**
     * See if the timer has already started
     * @return if the stopwatch has been started
     */
    public boolean started() {
        return this.started;
    }

    /**
     * Setter for our current time
     * @param param the new time value
     */
    public void setTime(float param) {
        this.time = param;
    }

    /**
     * Getter for our current x
     * @return the current x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter for our current y
     * @return the current y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Stop our stopwatch when the game is over
     */
    public void stopStopwatch() {
        //set this as false so we are no longer updating on the game board
        this.snapshot = getTime();
        this.updateTime = false;
    }

    /**
     * Start the stopwatch if its been stopped
     */
    public void startStopwatch() {
        this.updateTime = true;
    }

    /**
     * A getter for our stopwatches' update time property
     * @return the value of this.updateTime (T or F)
     */
    public boolean getUpdateTime() {
        return this.updateTime;
    }

    /**
     * Start our timer with a normal task
     */
    public void startTimer() {
        if (this.started == true) {
            return;
        }
        this.started = true;
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    
    /**
     * Start our timer with a more precise task for the ball spawning
     */
    public void startPreciseTimer() {
        if (this.started == true) {
            return;
        }
        this.started = true;
        timer.scheduleAtFixedRate(preciseTask, 0, 100);
    }
}
