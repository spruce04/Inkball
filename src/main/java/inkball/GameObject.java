package inkball;

/**
* The abstract base class for any object on our game board
*/
public abstract class GameObject {
    private float x;
    private float y;
    private String image = "";

    /**
    * Constructor for our tile
    * @param x the x coordinate
    * @param y the y coordinate
    */
    GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
    * Determine the current sprite of our tile
    * @param app the app, which we pass in to draw on
    */
    public abstract void draw(App app);

    /**
    * Set the object's image
    * @param param the image key
    */
    public void setImage(String param) {
        this.image = param;
    }

    /**
    * Get the object's image
    * @return the string associated with an object's image
    */
    public String getImage() {
        return this.image;
    }

    /**
    * Get the object's x
    * @return the x value
    */
    public float getX() {
        return this.x;
    }

    /**
    * Get the object's y
    * @return the y value
    */
    public float getY() {
        return this.y;
    }

    /**
    * Set the object's x value
    * @param value the value that we are assigning to x
    */
    public void setX(float value) {
        this.x = value;
    }

    /**
    * Set the object's y value
    * @param value the value that we are assigning to y
    */
    public void setY(float value) {
        this.y = value;
    }
}