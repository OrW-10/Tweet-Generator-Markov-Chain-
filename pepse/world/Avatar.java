package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * An Avatar class the player can control during a Game.
 * It interacts with objects present in the GameManager.
 */
public class Avatar extends GameObject {
    /* ************** Constants ************** */
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(30,30);
    private static final String INITIAL_ANIMATION = "assets/idle_0.png";
    private static final float GRAVITY = 550;
    private enum ActionState{
        RUNNING(0.1),
        JUMPING(0.3),
        IDLE(0.2);


        private final double animationSpeed;
        ActionState(double x){
            this.animationSpeed = x;
        }
    }
    private static final Boolean FACING_RIGHT = false;
    private static final Boolean FACING_LEFT = true;
    private static final float KEY_PRESSED_VELOCITY = 200;
    private static final float JUMP_LAUNCH_VELOCITY = -550;
    private static final String ENERGY_TEXT = "Energy: ";
    private static final double MAX_ENERGY = 100;
    private static final float IDLE_ENERGY = 1;
    private static final float RUN_ENERGY = 0.5f;
    private static final float JUMP_ENERGY = 10;
    private static final double FRUIT_ENERGY = 10;
    private static final String[] IDLE_ANIMATION_PICTURES =
            {"assets/idle_0.png",
            "assets/idle_1.png",
            "assets/idle_2.png",
            "assets/idle_3.png"};
    private static final String[] RUN_ANIMATION_PICTURES =
            {"assets/run_0.png",
            "assets/run_1.png",
            "assets/run_2.png",
            "assets/run_3.png",
            "assets/run_4.png",
            "assets/run_5.png"};
    private static final String[] JUMP_ANIMATION_PICTURES =
            {"assets/jump_0.png",
            "assets/jump_1.png",
            "assets/jump_2.png",
            "assets/jump_3.png"};

    /* ************** Private Fields ************** */
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private double energy = 100;
    private ActionState actionState = ActionState.IDLE;
    private Boolean facing = FACING_RIGHT;
    private Runnable jumpEffect = null;

    /* ************** Getter Functions ************** */

    /**
     * returns the amount oof energy in the form of text.
     * @return String with energy level.
     */
    public String getEnergyText() {
        return ENERGY_TEXT + energy + "%";
    }

    /* ************** Setter Functions ************** */

    /**
     * Sets which effect should take effect after a successful jump.
     * @param jumpEffect the callback to the desired effect.
     */
    public void setJumpEffect(Runnable jumpEffect) {
        this.jumpEffect = jumpEffect;
    }

    /* ************** Constructor ************** */
    /**
     * Constructs a new Avatar instance.
     *
     * @param pos - position from which the avatar should start appearing from.
     *              (the bottom right coordinate from where he should start)
     * @param inputListener - the object used to determine which keyboard keys the user has pressed.
     * @param imageReader - the object used to read image files.
     */
    public Avatar(Vector2 pos,
                  UserInputListener inputListener,
                  ImageReader imageReader){
        super(
                pos.add(AVATAR_DIMENSIONS.mult(-1)), // so pos is bottom right pixel
                AVATAR_DIMENSIONS,
                imageReader.readImage(INITIAL_ANIMATION, true));
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.setTag("avatar");
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.transform().setAcceleration(Vector2.DOWN.mult(GRAVITY));
    }

    /* ************** Update (Override) ************** */
    /**
     * Updates the state of the avatar object in accordance
     * to the circumstances at play in this frame of the game.
     * The avatar will update his velocity, energy, and animation.
     *
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

        ActionState currentState = HandleMovement();
        updateSpriteAnimation(currentState);
    }

    /* ************** onCollisionEnter() (Override) ************** */
    /**
     * Determines what the avatar object will do upon colliding with a legal object.
     * If the object is an apple, he will eat it.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // fruit collision instance
        if(other.getTag().equals("fruit")){
            other.setTag("eaten");
            this.energy = Math.min(this.energy + FRUIT_ENERGY, MAX_ENERGY);
        }
    }

    /* ************** Helper Functions ************** */
    /* Handles the avatar's movement (velocity and energy) */
    private ActionState HandleMovement() {
        ActionState currentState = ActionState.IDLE;

        // x Axis movement
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= KEY_PRESSED_VELOCITY;
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += KEY_PRESSED_VELOCITY;

        if(xVel != 0 && this.energy >= RUN_ENERGY){
            this.energy -= RUN_ENERGY;
            currentState = ActionState.RUNNING;
        }
        else{
            xVel = 0;
        }
        this.transform().setVelocityX(xVel);

        // y axis movement
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            if(this.energy >= JUMP_ENERGY){
                this.energy -= JUMP_ENERGY;
                this.transform().setVelocityY(JUMP_LAUNCH_VELOCITY);
                if(this.jumpEffect != null){
                    this.jumpEffect.run();
                }
            }
        }

        // if not moving left or right but airborne
        if(currentState == ActionState.IDLE && this.getVelocity().y() != 0){
            currentState = ActionState.JUMPING;
        }

        // velocity is zero
        if(this.getVelocity().equals(Vector2.ZERO) && this.energy <= MAX_ENERGY){
            this.energy = Math.min(this.energy+IDLE_ENERGY,MAX_ENERGY);
        }

        return currentState;
    }

    /* Helper function for updating the avatar's animation */
    private void updateSpriteAnimation(ActionState currentState) {
        // update animation type
        if(this.actionState != currentState){
            this.actionState = currentState;
            switch(actionState){
                case IDLE:
                    renderer().setRenderable(new AnimationRenderable(
                            IDLE_ANIMATION_PICTURES,
                            imageReader,
                            true,
                            ActionState.IDLE.animationSpeed));
                    break;
                case RUNNING:
                    renderer().setRenderable(new AnimationRenderable(
                            RUN_ANIMATION_PICTURES,
                            imageReader,
                            true,
                            ActionState.RUNNING.animationSpeed));
                    break;
                case JUMPING:
                    renderer().setRenderable(new AnimationRenderable(
                            JUMP_ANIMATION_PICTURES,
                            imageReader,
                            true,
                            ActionState.JUMPING.animationSpeed));
                    break;
            }
        }

        // update which way avatar is facing
        if(this.getVelocity().x() > 0){
            facing = FACING_RIGHT;
        }
        if(this.getVelocity().x() < 0){
            facing = FACING_LEFT;
        }
        renderer().setIsFlippedHorizontally(facing);
    }
}
