package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;

/**
 * A Leaf object used to represent leaves on a Tree.
 * Moves as if a wind blows through the tree.
 */
public class Leaf extends Block {
    /* ************** Constants ************** */
    private static final Float LEAF_TILT = 20f;
    private static final float MOVEMENT_DURATION = 2;
    private static final Vector2 REGULAR_LEAF_SIZE = new Vector2(Block.SIZE,Block.SIZE);
    private static final Vector2 MOTION_LEAF_SIZE = new Vector2(Block.SIZE,Block.SIZE).mult(0.9f);

    /* ************** Constructor ************** */
    /**
     * Creates an instance of Leaf.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        this.setTag("leaf");
        new ScheduledTask(
                this,
                new Random().nextFloat(),
                false,
                this::moveLeaves
        );
    }

    /* ************** shouldCollideWith() (Override) ************** */
    /**
     * Dictates what other GameObjects the Leaf can collide with (set to none).
     * @param other The other GameObject.
     * @return false - the Leaf should never collide.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    /* ************** Helper Functions ************** */
    /* A helper function for setting up the Leaf's movement. */
    private void moveLeaves() {
        // handle the angle of the Leaf
        new Transition<Float>(
                this,         // the game object being changed
                renderer()::setRenderableAngle,       // the method to call
                0f,                                   // initial transition value
                LEAF_TILT,                            // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  // use a cubic interpolator
                MOVEMENT_DURATION,                    // transition duration
                TRANSITION_BACK_AND_FORTH,            // the looping type (enum)
                null);                                // action upon completion

        // handle the size of the Leaf
        new Transition<Vector2>(
                this,         // the game object being changed
                this::setDimensions,                  // the method to call
                REGULAR_LEAF_SIZE,                    // initial transition value
                MOTION_LEAF_SIZE,                     // final transition value
                Transition.CUBIC_INTERPOLATOR_VECTOR, // use a cubic interpolator
                MOVEMENT_DURATION,                    // transition duration
                TRANSITION_BACK_AND_FORTH,            // the looping type (enum)
                null);                                // action upon completion
    }
}
