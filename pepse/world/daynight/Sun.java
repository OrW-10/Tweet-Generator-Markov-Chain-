package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

import static danogl.components.Transition.TransitionType.TRANSITION_LOOP;

/**
 * A class in charge of creating the GameObject that represents the sun within the game.
 * The sun will travel in a circular motion across the sky in by accounting for the height
 * of the ground at the starting point.
 * The sun will give an indication to the time of day in the game.
 */
public class Sun {
    /* ************** Constants ************** */
    private static final Vector2 SUN_SIZE_VECTOR = new Vector2(100,100);
    private static final Vector2 DISTANCE_FROM_ROTATION_CENTER = new Vector2(0,-300);

    /* ************** Create() ************** */
    /**
     * The function that creates an instance of GameObject that represents the sun.
     *
     * @param windowDimensions - the dimensions of the window screen provided by the GameManager.
     * @param cycleLength - the amount of time a full day should last.
     * @return an instance of the GameObject in charge of the sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        // The initial location of the sun and the center of its circular motion
        Vector2 cycleCenter = new Vector2(
                (int)windowDimensions.x()/2,
                windowDimensions.y()* Terrain.GROUND_HEIGHT_AT_0_WINDOW_RATIO);
        Vector2 initialSunCenter = new Vector2(cycleCenter.add(DISTANCE_FROM_ROTATION_CENTER));

        // creating the object
        OvalRenderable sunRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(initialSunCenter,SUN_SIZE_VECTOR,sunRenderable);
        sun.setTag("sun");

        // setting up sun's behavior
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        new Transition<Float>(
                sun,                                  // the game object being changed
                (angle) -> sun.setCenter(       // the method to call
                        initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                0f,                         // initial transition value
                360f,                                 // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength,                          // transition fully over half a day
                TRANSITION_LOOP,                      // the looping type (enum)
                null);
        return sun;
    }
}
