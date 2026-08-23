package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A basic GameObject in the form of a rectangular block that can be used to help build
 * environments within the game.
 * The block is used to create things like the Terrain or different plants.
 * Blocks have physics applied to them by default.
 */
public class Block extends GameObject {
    /* ************** Constants ************** */
    /**
     * The size of the GameBlock (with or height).
     */
    public static final int SIZE = 30;

    /* ************** Constructor ************** */
    /**
     * Creates a new instance of Block.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
            super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
            physics().preventIntersectionsFromDirection(Vector2.ZERO);
            physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        }
}
