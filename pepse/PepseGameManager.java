package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.List;
import java.util.Random;

import static danogl.collisions.Layer.*;

/**
 * The GameManager in charge of running the game and keeping track of the
 * GameObjects and their effects/interactions.
 */
public class PepseGameManager extends GameManager {
    /* ************** Constants ************** */
    private static final Vector2 AVATAR_STARTING_POS = new Vector2(200,-10);
    private static final float DAY_LENGTH = 30;
    private static final Vector2 TEXT_SIZES = new Vector2(100, 100);

    /* ************** Private Fields ************** */
    private long seed;
    private Terrain terrain;
    private Flora flora;

    /* ************** initializeGame() ************** */
    /**
     * The function that initializes all the GameObjects at the start of the game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // setting up seed for the game
        this.seed = setUpSeed();

        // setup environment
        setUpEnvironment(windowController.getWindowDimensions(), this.seed);

        // setup flora
        setUpPlants(windowController.getWindowDimensions());

        // setup avatar and energy GUI
        setUpAvatar(inputListener, imageReader);
    }

    /* ************** Helper Functions ************** */
    /* Sets up the game's seed so that randomly generated values can be retraced (relevant if expanded). */
    private long setUpSeed() {
        Random random = new Random();
        long seed = random.nextInt();
        while(seed == 0){
            seed = random.nextInt();
        }
        return seed;
    }

    /* Sets up the environment of the game except for the plants. */
    private void setUpEnvironment(Vector2 windowDimensions, long seed) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, BACKGROUND);
        this.terrain = new Terrain(windowDimensions, (int)seed);
        List<Block> blocks = this.terrain.createInRange(0, (int)windowDimensions.x());
        for(Block block : blocks){
            gameObjects().addGameObject(block, STATIC_OBJECTS);
        }
        GameObject night = Night.create(windowDimensions,DAY_LENGTH);
        GameObject sun = Sun.create(windowDimensions,DAY_LENGTH);
        gameObjects().addGameObject(sun,BACKGROUND);
        GameObject halo = SunHalo.create(sun);
        gameObjects().addGameObject(halo,BACKGROUND);
        gameObjects().addGameObject(night,FOREGROUND);
    }

    /* Sets up the plants in the game. */
    private void setUpPlants(Vector2 windowDimensions) {
        this.flora = new Flora(this.terrain::groundHeightAt, seed);
        List<Tree> trees = this.flora.createInRange(0, (int)windowDimensions.x());
        for(Tree tree : trees){
            for(GameObject block : tree.getTreeObjects()){
                gameObjects().addGameObject(block,STATIC_OBJECTS);
            }
        }
    }

    /* Sets up the game's avatar and energy GUI. */
    private void setUpAvatar(UserInputListener inputListener, ImageReader imageReader){
        Avatar avatar = new Avatar(AVATAR_STARTING_POS, inputListener, imageReader);
        avatar.setJumpEffect(this.flora::changeEnvironment);
        gameObjects().addGameObject(avatar,DEFAULT);
        DependantTextGUI energyText = new DependantTextGUI(
                Vector2.ZERO,
                TEXT_SIZES,
                avatar::getEnergyText);
        gameObjects().addGameObject(energyText,UI);
    }

    /* ************** main() ************** */
    /**
     * The main function that starts and runs the program.
     * @param args - the arguments inserted by the user.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
