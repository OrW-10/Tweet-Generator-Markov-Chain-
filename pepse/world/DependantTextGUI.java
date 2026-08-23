package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

/**
 * A textGUI GameObject whose content is changed based on a String Supplier callback.
 * The callback informs the text what the current value of the text should be and the object
 * changes in accordance to this.
 */
public class DependantTextGUI extends GameObject{
    /* ************** Private Fields ************** */
    private String currentSuppliedValue;
    private final Supplier<String> dependency;

    /* ************** Constructor ************** */
    /**
     * Construct a new DependantTextGUI instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param getCurrentValue - a callback to a String Supplier function that updates the
     *                        object what it should display at any given moment.
     */
    public DependantTextGUI(Vector2 topLeftCorner, Vector2 dimensions, Supplier<String> getCurrentValue) {
        super(topLeftCorner, dimensions, new TextRenderable(getCurrentValue.get()));
        this.dependency = getCurrentValue;
        this.currentSuppliedValue = getCurrentValue.get();
    }

    /* ************** Update() (Override) ************** */
    /**
     * Updates the object in accordance to the information that should be on display in the current frame.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        String newValue = dependency.get();
        if(!newValue.equals(currentSuppliedValue)){
            this.currentSuppliedValue = newValue;
            TextRenderable newText = new TextRenderable(currentSuppliedValue);
            this.renderer().setRenderable(newText);
        }
    }
}
