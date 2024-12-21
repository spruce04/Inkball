package inkball;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

/**
* The class for a .txt file reader
* This class' purpose is to interpret a .txt file and return what needs to be known to drawn a game layout
*/
public class TxtReader {

    /**
    * This method takes a file path and returns an array to be used in our App to set the game board
    * @param path the path of the file to read
    * @param app the app, which we edit some properties of
    * @return an array of strings representing the game board
    */
    public static String[][] readTxtFile(String path, App app) {
        String[][] boardArray = new String[18][18];
        char c;
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            StringBuilder fileContent = new StringBuilder();
            //put the file's content in a stringbuilder
            while(scanner.hasNext()) {
                fileContent.append(scanner.nextLine());
            }
            scanner.close();
            //put each character from the string builder into an array that corresponds with the game board
            int index = 0;
            for (int i = 0; i < boardArray.length; i++) {
                for (int j = 0; j < boardArray[i].length; j++) {
                    try {//if there is nothing at the index, make it blank
                        c = fileContent.charAt(index);
                    } catch (Exception e) {
                        continue;
                    }
                    
                    try {//use subtle error handling for index 0
                        if (fileContent.charAt(index-1) == 'B' && j != 0) {//if we had a ball before, skip
                            index++;
                            continue;
                        }
                    } catch (Exception e) {

                    }
                    if (boardArray[i][j] != null) {//if this index has already been filled for whatever reason, continue
                        index++;
                        continue;
                    }
                    if (c == 'X') {
                        boardArray[i][j] = "wall 0";
                    }
                    else if (Character.getNumericValue(c) == 1) {
                        boardArray[i][j] = "wall 1";
                    }
                    else if (Character.getNumericValue(c) == 2) {
                        boardArray[i][j] = "wall 2";
                    }
                    else if (Character.getNumericValue(c) == 3) {
                        boardArray[i][j] = "wall 3";
                    }
                    else if (Character.getNumericValue(c) == 4) {
                        boardArray[i][j] = "wall 4";
                    }
                    else if (c == 'S') {
                        boardArray[i][j] = "spawner";
                    }
                    else if (c == 'H') { 
                        char holeType = fileContent.charAt(index+1);
                        addHole(boardArray, j, i, holeType);
                    }
                    else if (c == 'B') {
                        char ballType = fileContent.charAt(index+1);
                        addBall(app, j, i, Character.getNumericValue(ballType));
                    }
                    else if (c == 'G') {
                        boardArray[i][j] = "brick 0";
                    }
                    else if (c == 'O') {
                        boardArray[i][j] = "brick 1";
                    }
                    else if (c == 'U') {
                        boardArray[i][j] = "brick 2";
                    }
                    else if (c == 'E') {
                        boardArray[i][j] = "brick 3";
                    }
                    else if (c == 'Y') {
                        boardArray[i][j] = "brick 4";
                    }
                    index++;//increment our index after each loop executes
                }
                
            }
            
        } catch (FileNotFoundException e){
            System.out.println("File " + path + " couldn't be found");
        }
        return boardArray;
    }

    /**
    * This method takes a adds a 4-square sized hole to the array at our specified position
    * @param paramArray our array of the board
    * @param baseX the x coordinate
    * @param baseY the y coordinate
    * @param holeType the type of the hole
    * 
    */
    public static void addHole(String[][] paramArray, int baseX, int baseY, char holeType) {
        paramArray[baseY][baseX] = "H" + holeType+"M";
        paramArray[baseY+1][baseX] = "H" + holeType;
        paramArray[baseY][baseX+1] = "H" + holeType;
        paramArray[baseY+1][baseX+1] = "H" + holeType;
    }

    /**
    * This method takes adds a ball to be drawn on our gameboard at the start of the game
    * @param app the app, which we edit properties of
    * @param baseX the x value of the ball
    * @param baseY the y value of the ball
    * @param ballType the type of the ball
    */
    public static void addBall(App app, int baseX, int baseY, int ballType) {
        ArrayList<Ball> allBalls = app.getAllBalls();
        Ball newBall = new Ball(baseX*32, baseY*32, ballType);
        allBalls.add(newBall);
    }

}