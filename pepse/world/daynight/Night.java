package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;

/**
 * A class that creates a GameObject that will be used to simulate the change in
 * light throughout a day cycle.
 * Specifically one that represents the night.
 */
public class Night {
    /* ************** Constants ************** */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /* ************** Create() ************** */
    /**
     * Creates the night GameObject which is in charge of changing the lighting
     * in accordance to the time of day.
     *
     * @param windowDimensions - the dimensions of the window screen provided by the GameManager.
     * @param cycleLength - the amount of time a full day should last.
     * @return an instance of the GameObject in charge of the night.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        // creating the object
        RectangleRenderable nightRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO,windowDimensions,nightRenderable);
        night.setTag("night");

        // setting up the object's behavior
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        new Transition<Float>(
                night,                                // the game object being changed
                night.renderer()::setOpaqueness,      // the method to call
                0f,                                   // initial transition value
                MIDNIGHT_OPACITY,                     // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  // use a cubic interpolator
                cycleLength/2,                        // transition fully over half a day
                TRANSITION_BACK_AND_FORTH,            // the looping type (enum)
                null);                                // nothing further to execute upon reaching final value
        return night;
    }
}
