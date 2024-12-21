package inkball;

/**
* The class for a spawner on our game board
*/
public class Spawner extends Tile {

    public Spawner(int x, int y) {
        super(x, y);
        setImage("entrypoint");
    }

    /**
    * Draw the Spawner
    */
    @Override
    public void draw(App app) {
        super.draw(app);
    }
}