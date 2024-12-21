package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

import com.google.common.collect.Table.Cell;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 576; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();

    private Tile[][] board = new Tile[18][18];//this array will contain our game board

    private static HashMap<String, PImage> images = new HashMap<>(); //this hashmap will contain our images
    //initialise a big array that contains the names of all of our images
    private String[] imageNames = new String[] {"ball0", "ball1", "ball2", "ball3", "ball4", "entrypoint", "hole0",
    "hole1", "hole2", "hole3", "hole4", "tile", "wall0", "wall1", "wall2", "wall3", "wall4", "bluebrick", "graybrick", 
    "greenbrick", "orangebrick", "yellowbrick"};
    

    private String pathString = System.getProperty("user.dir");

    private String txtPath; // a path to our txtfile that will change throughout the program

    //ArrayList that will contain all of the current balls in our game
    private ArrayList<Ball> allBalls = new ArrayList<>();

    //An ArrayList that will contain everything in our game that a ball could potentially collide with
    private ArrayList<Collideable> allCollideables = new ArrayList<>();


    //An ArrayList that will contain all of the current lines in our game
    private ArrayList<Line> allLines = new ArrayList<>();
    //An ArrayList that will hold the coordinates of a line
    private ArrayList<int[]> linePoints = new ArrayList<>();
    int lineIndex;

    //initialise a stopwatch (this will be out timer)
    private Stopwatch stopwatch;
    //initialise a scorecounter to count our score
    private ScoreCounter scoreCounter = new ScoreCounter(475, 30);

    //A boolean that represents if the left mouse button is clicked or not
    public boolean leftMouseClicked = false;
    public int lineCount = 0;

    //A line which we use to avoid indexing issues in the mouse dragged function
    Line currentLine;

    //A ball loader
    private BallLoader ballLoader;

    //An array list containing every spawner
    private ArrayList<Spawner> allSpawners = new ArrayList<>();

    //An array list containing every "main" hole
    ArrayList<Hole> mainHoles = new ArrayList<>();

    //A Json Reader to use throughout the program's execution
    private JsonReader jsonUtil;

    private boolean paused = false;//a boolean to keep track of if the game is paused

    int levelNumber = 1;//an int to keep track of the level number we are up to
    int levelCount;//the number of levels in the config.json file
    boolean gameOver = false;//a boolean to keep track of if the game is over

    /**
     * constructor for the app
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Set the config path of the app (for testing purposes)
     * @param newPath the new path of the config file
     */
    public void setConfigPath(String newPath) {
        this.configPath = newPath;
    }

    /**
     * Getter for any specific image
     * @param name the name of the image we want to get
     */
    public PImage getImage(String name) {
        PImage image = images.get(name);
        return image;
    }

    /**
     * Getter for the json reader
     * @return the app's json reader
     */
    public JsonReader getJsonReader() {
        return this.jsonUtil;
    }

    /**
     * @return the app's stopwatch
     */
    public Stopwatch getStopwatch() {
        return this.stopwatch;
    }

    /**
     * @return the boolean value of this.paused
     */
    public boolean getPaused() {
        return this.paused;
    }

    /**
     * @return the app's scorecounter
     */
    public ScoreCounter getScoreCounter() {
        return this.scoreCounter;
    }

    /**
     * Set the value of if the app is paused
     * @param value if the app is paused
     */
    public void setPaused(boolean value) {
        this.paused = value;
    }

    /**
     * Getter for the array list of balls
     * @return the array list that contains all our balls
     */
    public ArrayList<Ball> getAllBalls() {
        return this.allBalls;
    }

    /**
     * Getter for the array list of collideable objects
     * @return the array list that contains all our collideable objects
     */
    public ArrayList<Collideable> getAllCollideables() {
        return this.allCollideables;
    }

    /**
     * Returns the gameboard
     * @return the current gameboard
     */
    public Tile[][] getBoard() {
        return this.board;
    }

    /**
     * Returns the array list containing every spawner
     * @return the array list allSpawners
     */
    public ArrayList<Spawner> getAllSpawners() {
        return this.allSpawners;
    }

    /**
     * Returns the array list containing every main hole
     * @return the array list mainHoles
     */
    public ArrayList<Hole> getAllMainHoles() {
        return this.mainHoles;
    }

    /**
     * Returns the ball loader
     * @return the ball loader
     */
    public BallLoader getBallLoader() {
        return this.ballLoader;
    }

    /**
     * Returns the array list containing every line
     * @return the array list containing every line
     */
    public ArrayList<Line> getAllLines() {
        return this.allLines;
    }

    /**
     * Add a line to the array list of lines for testing purposes
     * @param line the line to add
     */
    public void addLine(Line line) {
        this.allLines.add(line);
    }

    /**
     * Setter for any specific image
     * @param name the name of the image
     * @param image the actual image
     */
    public void setImage(String name, PImage image) {
        images.put(name, image);
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        //take the data from our JSON file and save it into a variable called json
		JSONObject json = loadJSONObject(configPath);
        jsonUtil = new JsonReader(json);
        int a;

        for (a = 0; a < levelNumber; a++) {//go to the correct level number
            jsonUtil.nextLevel();
        }
        txtPath = jsonUtil.currentLevelPath();
        String[] ballsToLoad = jsonUtil.currentBallsToSpawn();
        int spawnInterval = jsonUtil.currentSpawnInterval();   
        ballLoader = new BallLoader(ballsToLoad, spawnInterval, this);

        String[][] layout = TxtReader.readTxtFile(txtPath, this);
        //iterate through the board and make every part of it the element that it corresponds to
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (layout[i][j] == null) {
                    this.board[i][j] = new Tile(j, i);
                }
                else if (layout[i][j].equals("wall 0")) {
                    Wall wall = new Wall(j, i, 0);
                    this.board[i][j] = wall;
                    allCollideables.add(wall);
                }
                else if (layout[i][j].equals("wall 1")) {
                    Wall wall = new Wall(j, i, 1);
                    this.board[i][j] = wall;
                    allCollideables.add(wall);
                }
                else if (layout[i][j].equals("wall 2")) {
                    Wall wall = new Wall(j, i, 2);
                    this.board[i][j] = wall;
                    allCollideables.add(wall);
                }
                else if (layout[i][j].equals("wall 3")) {
                    Wall wall = new Wall(j, i, 3);
                    this.board[i][j] = wall;
                    allCollideables.add(wall);
                }
                else if (layout[i][j].equals("wall 4")) {
                    Wall wall = new Wall(j, i, 4);
                    this.board[i][j] = wall;
                    allCollideables.add(wall);
                }
                else if (layout[i][j].equals("spawner")) {
                    Spawner spawner = new Spawner(j, i);
                    this.board[i][j] = spawner;
                    allSpawners.add(spawner);
                }
                else if (layout[i][j].equals("H0")) {
                    this.board[i][j] = new Hole(j, i, 0, false);
                }
                else if (layout[i][j].equals("H1")) {
                    this.board[i][j] = new Hole(j, i, 1, false);
                }
                else if (layout[i][j].equals("H2")) {
                    this.board[i][j] = new Hole(j, i, 2, false);
                }
                else if (layout[i][j].equals("H3")) {
                    this.board[i][j] = new Hole(j, i, 3, false);
                }
                else if (layout[i][j].equals("H4")) {
                    this.board[i][j] = new Hole(j, i, 4, false);
                }
                else if (layout[i][j].equals("H0M")) {
                    Hole mHole = new Hole(j, i, 0, true);
                    mainHoles.add(mHole);
                    this.board[i][j] = mHole;
                }
                else if (layout[i][j].equals("H1M")) {
                    Hole mHole = new Hole(j, i, 1, true);
                    mainHoles.add(mHole);
                    this.board[i][j] = mHole;
                }
                else if (layout[i][j].equals("H2M")) {
                    Hole mHole = new Hole(j, i, 2, true);
                    mainHoles.add(mHole);
                    this.board[i][j] = mHole;
                }
                else if (layout[i][j].equals("H3M")) {
                    Hole mHole = new Hole(j, i, 3, true);
                    mainHoles.add(mHole);
                    this.board[i][j] = mHole;
                }
                else if (layout[i][j].equals("H4M")) {
                    Hole mHole = new Hole(j, i, 4, true);
                    mainHoles.add(mHole);
                    this.board[i][j] = mHole;
                }
                else if (layout[i][j].equals("brick 0")) {
                    Brick brick = new Brick(j, i, 0);
                    this.board[i][j] = brick;
                    allCollideables.add(brick);
                }
                else if (layout[i][j].equals("brick 1")) {
                    Brick brick = new Brick(j, i, 1);
                    this.board[i][j] = brick;
                    allCollideables.add(brick);
                }
                else if (layout[i][j].equals("brick 2")) {
                    Brick brick = new Brick(j, i, 2);
                    this.board[i][j] = brick;
                    allCollideables.add(brick);
                }
                else if (layout[i][j].equals("brick 3")) {
                    Brick brick = new Brick(j, i, 3);
                    this.board[i][j] = brick;
                    allCollideables.add(brick);
                }
                else if (layout[i][j].equals("brick 4")) {
                    Brick brick = new Brick(j, i, 4);
                    this.board[i][j] = brick;
                    allCollideables.add(brick);
                }
            }
        }

		//the image is loaded from relative path: "src/main/resources/inkball/..."
		//we will store all the images in our hashmap - the loadImage() part is taken from the scaffold
        for (String name:imageNames) {
            try {
                PImage result = loadImage(URLDecoder.decode(this.getClass().getResource(name+".png").getPath(), StandardCharsets.UTF_8.name()));
                setImage(name, result);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            
        }
        //initialise our stopwatch
        stopwatch = new Stopwatch(jsonUtil.currentLevelTime(), 475, 60);
        //set the restart score of the scoreCounter
        scoreCounter.setRestartScore(scoreCounter.getScore());
    }

    /**
     * Receive key pressed signal from the keyboard.
     * @param event the relevant event
     */
	@Override
    public void keyPressed(KeyEvent event){
        
    }

    /**
     * Receive key released signal from the keyboard.
     * @param e the relevant event
     */
	@Override
    public void keyReleased(KeyEvent e){
        //restart on r key
        if (e.getKey() == 'R' || e.getKey() == 'r') {
            if (gameOver) {//if the game is over, restart the whole game
                allBalls.clear();
                allLines.clear();
                mainHoles.clear();
                allSpawners.clear();
                allCollideables.clear();
                scoreCounter.setRestartScore(0);
                scoreCounter.setScore(0);
                levelNumber = 1;
                gameOver = false;
                paused = false;
                this.setup();
            }
            else {//if not, just restart the level
                allBalls.clear();
                allLines.clear();
                allCollideables.clear();
                scoreCounter.setScore(scoreCounter.getRestartScore());
                paused = false;
                this.setup();
            }
            
        }

        //pause when space bar is pressed
        if (e.getKey() == ' ') {
            for (Ball b : allBalls) {
                b.setPause();
            }
            if (paused == false) {//if the game isn't paused
                stopwatch.stopStopwatch();
                ballLoader.getStopwatch().stopStopwatch();
                paused = true;
            }
            else {
                paused = false;
                stopwatch.startStopwatch();
                ballLoader.getStopwatch().startStopwatch();
            }
            
        }
    }

    @Override
    /**
     * Receive key released pressed from the mouse.
     * @param e the relevant event
     */
    public void mousePressed(MouseEvent e) {
        // create a new player-drawn line object
        //if the mouse is in the topbar, then we don't want to draw the line
        if (e.getY() <=  64 || e.isControlDown()) {
            return;
        }
        if (e.getButton() == LEFT) {
            leftMouseClicked = true;
            currentLine = new Line(e.getX(), e.getY(), this);
            allLines.add(currentLine);
            allCollideables.add(currentLine);
            lineIndex = allLines.indexOf(currentLine);
        }
    }
	
	@Override
    /**
     * If the mouse is dragged
     * @param e the relevant event
     */
    public void mouseDragged(MouseEvent e) {
        //if the mouse is in the topbar or control button is pressed, then we don't want to draw the line
        if (e.getY() <=  64 || e.isControlDown()) {
            return;
        }
        // add line segments to player-drawn line object if left mouse button is held
        if (leftMouseClicked) {
            //first, we have to ensure that the line index is valid
            lineIndex = allLines.lastIndexOf(currentLine);
            if (lineIndex == -1) {//if the line no longer exists, return
                return;
            }
            if (lineCount%10 == 0) {//add the hitbox to our line array
                allLines.get(lineIndex).addHitboxToLine(e.getX(), e.getY());
            }
            allLines.get(lineIndex).addPointToLine(e.getX(), e.getY());
            lineCount++;
        }   
    }

    @Override
    /**
     * Receive mouse released signal from the keyboard.
     * @param e the relevant event
     */
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == RIGHT || (e.getButton() == LEFT && e.isControlDown())) {
            //delete the line
            //We need to use an iterator instead of a for:each loop to be able to delete
            Iterator<Line> iterator = allLines.iterator();
            while (iterator.hasNext()) {
                boolean shouldRemove = false;//we can't remove immediately in case of duplicates
                Line line = iterator.next();
                for (int[] points : line.getAllPoints()) {
                    if (e.getX() == points[0] && e.getY() == points[1]) {
                        shouldRemove = true;
                    }
                }
                if (shouldRemove) {//if the line is marked for removal, remove it (so we don't call remove() multiple times)
                    iterator.remove();
                    allCollideables.remove(line);//remove it from collideables as well
                }
            }
        }
		else if (e.getButton() == LEFT) {
            leftMouseClicked = false;
            lineCount = 0;//reset the linecount
        }
    }

    /**
     * Remove a line if a ball has collided with it
     * @param line the line to be removed
     */
    public void removeLine(Line line) {
        allCollideables.remove(line);
        allLines.remove(line);
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        //if the stopwatch is out of time
        if (stopwatch.getTime() <= 0) {
            this.fill(0);
            this.textSize(CELLAVG/1.2f);
            this.text("===TIME'S UP===", 200, 45);
            return;
        }
        if (gameOver) {//if the game is over, we don't want to draw anything new
            scoreCounter.changeScore(stopwatch.getTime());
            this.fill(0);
            this.textSize(CELLAVG/1.2f);
            this.text("===ENDED===", 225, 45);
            return;
        }
        background(200,200,200);
        if (stopwatch.started() == false) {//start the stopwatch at the start of the game
            stopwatch.startTimer();
        }
        //draw everything on the board
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
              this.board[i][j].draw(this);//pass the current instance of our app as an argument
        }}

        //draw the ball spawner
        this.textSize(CELLAVG/2);
        ballLoader.draw(this);

        //draw all of our balls
        //we need to use an iterator in case a ball is removed during an iteration
        //and we need to make a new ArrayList copy to iterate over
        ArrayList<Ball> allBallsCopy = new ArrayList<>(allBalls);
        Iterator<Ball> ballIt = allBallsCopy.iterator();
        while (ballIt.hasNext()) {
            Ball b = ballIt.next();
            b.draw(this);
            if (b.getDelete()) {
                ballIt.remove();
            }
        }

        //draw the stopwatch and score counter
        this.fill(0);
        this.text("Time: " + String.format("%.0f", stopwatch.getTime()), stopwatch.getX(), stopwatch.getY());
        this.text("Score: " + String.format("%.0f", scoreCounter.getScore()), scoreCounter.getX(), scoreCounter.getY());


        //draw the lines
        for (Line line : allLines) {
            line.draw(this);
        }

        //if the game is paused, display the paused message
        if (paused) {
            this.fill(0);
            this.textSize(CELLAVG/1.2f);
            this.text("***PAUSED***", 250, 45);
        }

        //if there are no more balls, go to the next level
        if (allBalls.size() == 0) {
            //add the leftover time from the clock to the score counter
            scoreCounter.changeScore(stopwatch.getTime());
            levelNumber++;
            for (int i = 0; i < this.board.length; i++) {
                for (int j = 0; j < this.board[i].length; j++) {
                    this.board[i][j] = null;
                }
            }
            levelCount = jsonUtil.levelsArray.size();
            if (levelNumber > levelCount) {//if this happens, the player has beaten all levels
                gameOver = true;
                return;
            }
            else {
                jsonUtil.nextLevel();
                allLines.clear();
                allCollideables.clear();
                mainHoles.clear();
                allSpawners.clear();
                this.setup();
            }
        }
    }
    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}
