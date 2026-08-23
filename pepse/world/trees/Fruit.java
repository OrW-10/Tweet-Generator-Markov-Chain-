package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A GameObject that represent a fruit.
 * When the avatar collides with it the fruit disappears for the game equivalent of a day.
 */
public class Fruit extends GameObject {
    /* ************** Constants ************** */
    private static final Vector2 FRUIT_DIMENSIONS = new Vector2(30,30);
    private static final Color[] FRUIT_COLORS = {Color.RED,Color.BLUE};
    private static final float TIME_UNTIL_NEW_FRUIT = 30;

    /* ************** Private Fields ************** */
    private int colorIndex = 0;

    /* ************** Constructor ************** */
    /**
     * Creates an instance of Fruit.
     * @param topLeftCorner
     */
    public Fruit(Vector2 topLeftCorner) {
        super(topLeftCorner, FRUIT_DIMENSIONS, new OvalRenderable(FRUIT_COLORS[0]));
        this.setTag("fruit");
    }

    /* ************** changeToNextColor() ************** */
    /**
     * Changes fruit to the next color and updates the Renderable.
     */
    public void changeToNextColor(){
        System.out.println(colorIndex);
        if(++this.colorIndex >= FRUIT_COLORS.length){
            this.colorIndex = 0;
        }
        if(this.renderer().getRenderable() != null)
            this.setUpRenderable();
    }

    /* ************** Override Methods ************** */
    /**
     * Decides which GameObjects Fruits can collide with. (only avatar)
     * @param other The other GameObject being collided with.
     * @return true if the objects should collide (they collide only if both objects return true).
     *         false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("avatar");
    }

    /**
     * Dictates the Fruit's behavior upon collision (with avatar).
     * @param other The GameObject with which a collision occurred. (probably avatar)
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.renderer().setRenderable(null);
        new ScheduledTask(
                this,
                TIME_UNTIL_NEW_FRUIT,
                false,
                this::setUpRenderable
        );
    }

    /**
     * Overriden to make sure fruit doesn't glitch while in collision.
     * @param other The collision partner.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
    }

    /**
     * Overriden to make sure fruit doesn't glitch while exiting collision.
     * @param other The former collision partner.
     */
    @Override
    public void onCollisionExit(GameObject other) {
    }

    /* ************** Helper Functions ************** */
    /* Helper function that creates a new Fruit renderable. */
    private void setUpRenderable() {
        this.setTag("fruit");
        this.renderer().setRenderable(new OvalRenderable(FRUIT_COLORS[this.colorIndex]));
    }
}
