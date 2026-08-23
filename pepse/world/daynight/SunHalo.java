package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class in charge of creating a halo GameObject that surround the sun
 * and follows the sun's path.
 */
public class SunHalo {
    /* ************** Create() ************** */
    /**
     * Creates an instance of a GameObject that acts like a sun's halo.
     * @param sun - the GameObject whom the halo will surround and follow.
     * @return an instance of GameObject
     */
    public static GameObject create(GameObject sun){
        // creating the object
        OvalRenderable haloRenderable = new OvalRenderable(new Color(255, 255, 0, 20));
        GameObject halo = new GameObject(Vector2.ZERO,sun.getDimensions().mult(1.7f),haloRenderable);
        halo.setTag("halo");

        // setting up the halo's behavior
        halo.setCenter(sun.getCenter());
        sun.addComponent(deltaTime -> halo.setCenter(sun.getCenter()));
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return halo;
    }
}
