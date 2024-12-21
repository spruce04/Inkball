package inkball;

/**
* The class for a tile on our game board
* Tiles are just on the board but don't have any impact to gameplay
* Also serves as a base class for all other classes that live on the board
*/
public class Tile extends GameObject {

    /**
    * Constructor for our tile
    */
    Tile(int x, int y) {
        super(x, y);
        setImage("tile");
    }

    /**
    * Draw the tile
    */
    @Override
    public void draw(App app) {
        String currentImage = getImage();
        app.image(app.getImage(currentImage), this.getX()*App.CELLSIZE, this.getY()*App.CELLSIZE + App.TOPBAR);
    }
}
