package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that creates a GameObject that will be used to simulate the sky
 * (background) in the game.
 * Set up to follow the game's camera.
 */
public class Sky {
    /* ************** Constants ************** */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /* ************** Create() ************** */
    /**
     * Creates the sky GameObject which is displayed in the background of the game.
     *
     * @param windowDimensions - the dimensions of the window screen provided by the GameManager.
     * @return the GameObject representing the sky.
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag("sky");
        return sky;
    }
}
