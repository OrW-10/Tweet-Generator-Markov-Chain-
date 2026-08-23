package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static danogl.components.Transition.TransitionType.TRANSITION_ONCE;

/**
 * A class used to simulate the placement of trees in the game.
 * Also has function that can change all the plants in the game at once.
 * The trees will be placed differently every time the Game restarts
 * and will look a bit different from game to game.
 */
public class Flora {
    /* ************** Constants ************** */
    private static final float PROBABILITY_OF_PLANTING = 0.1F;
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);
    private static final float ANGLE_CHANGE = 90;
    private static final float LEAF_TRANSITION_TIME = 2;

    /* ************** Private Fields ************** */
    private final Function<Integer, Float> treeHeightAtXFunction;
    private final long seed;
    private List<Tree> trees = null;

    /* ************** Constructor ************** */
    /**
     * Creates the Object in charge of forming the trees in the game.
     *
     * @param treeHeightAtXFunction - the function used by flora to find the base
     *                              height of the tree.
     * @param seed - the seed used to simulate the placement of the trees on the terrain.
     */
    public Flora(Function<Integer,Float> treeHeightAtXFunction, long seed){
        this.treeHeightAtXFunction = treeHeightAtXFunction;
        this.seed = seed;
    }

    /* ************** createInRange() ************** */
    /**
     * Randomly generates trees within the range provided.
     * @param minX - the min x-coordinate provided to start generating the trees from.
     * @param maxX - the max x-coordinate provided to end generating the trees at.
     * @return a list of Trees which represent the trees in the game.
     */
    public List<Tree> createInRange(int minX, int maxX){
        // check input
        if (maxX < minX){
            int holder = maxX;
            maxX = minX;
            minX = holder;
        }

        // set up for loop
        this.trees = new ArrayList<>();
        Random random = new Random(this.seed);
        int distance = Math.abs(maxX-minX);
        int newMaxX = maxX + distance % Block.SIZE;

        // creates the trees
        for(int x = minX; x <= newMaxX; x+=Block.SIZE){
            if(random.nextFloat() < PROBABILITY_OF_PLANTING){
                Tree tree = new Tree(new Vector2(x,treeHeightAtXFunction.apply(x)), random);
                this.trees.add(tree);
            }
        }
        return trees;
    }

    /* ************** changeEnvironment() ************** */
    /**
     * A function that can be used to change the environment in an instant.
     * This one turns leaves 90 degrees, changes fruit colors, and changes trunk colors.
     */
    public void changeEnvironment(){
        for (Tree tree : trees){
            // set up trunk color
            RectangleRenderable trunkBlockRenderable =
                    new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_BASE_COLOR));

            // change in each tree
            for(GameObject obj : tree.getTreeObjects()){
                switch (obj.getTag()){
                    case "fruit":
                    case "eaten":
                        Fruit fruit = (Fruit) obj;
                        fruit.changeToNextColor();
                        break;
                    case "leaf":
                        new Transition<Float>(
                                obj,
                                obj.renderer()::setRenderableAngle,
                                obj.renderer().getRenderableAngle(),
                                obj.renderer().getRenderableAngle() + ANGLE_CHANGE,
                                Transition.LINEAR_INTERPOLATOR_FLOAT,
                                LEAF_TRANSITION_TIME,
                                TRANSITION_ONCE,
                                null);
                        break;
                    case "trunk":
                        obj.renderer().setRenderable(trunkBlockRenderable);
                        break;
                }
            }
        }
    }
}
