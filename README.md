# Inkball

## Inkball game made in java. To run the game:
* Clone the repo
* When in the repo, use the commands `gradle build` and then `gradle run`
* This project was made with gradle 7.4.2 and java 8 and will need gradle and java to work

## How to play:
* The aim of the game is to get balls into either grey holes or holes that correspond to their colour
* Grey balls can go into holes of any colour
* You can use left click to draw lines which balls will bounce off, and right click to remove these lines
* You can pause the game at any time by pressing the space bar
* You can restart the current level at any time by pressing the 'r' key

## Extra functionality:
* You can run the tests for this program by using `gradle test` or `gradle test jacocoTestReport`
* You can build the javadoc for this game by using `javadoc -d ./docs *.java`