package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The class that represents a tree in the game whose location is determined by floral.
 * Is composed of trunk, leaves, and fruits.
 */
public class Tree {
    /* ************** Constants ************** */
    private static final int MAX_NUM_OF_BLOCKS_HEIGHT = 3;
    private static final int MIN_HEIGHT = 5*Block.SIZE;
    private static final Vector2 LEAVES_DIMENSIONS = new Vector2(8*Block.SIZE,8*Block.SIZE);
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);
    private static final Color LEAVES_BASE_COLOR = new Color(50, 200, 30);
    private static final float PROBABILITY_OF_LEAF_CREATION = 0.5f;
    private static final float LEAF_OPAQUENESS = 0.6f;
    private static final float PROBABILITY_OF_FRUIT_CREATION = 0.05f;

    /* ************** Private Fields ************** */
    private final List<GameObject> treeBlocks;
    private final Random random;
    private int treeHeight = MIN_HEIGHT;

    /* ************** Getter Functions ************** */
    /**
     * Returns the GameObjects that make up the Tree.
     * @return a list of GameObjects in Tree.
     */
    public List<GameObject> getTreeObjects() {
        return this.treeBlocks;
    }

    /* ************** Constructor ************** */
    /**
     * Creates an instance of Tree.
     *
     * @param baseCoordinates - the corrdinates from which the tree should sprout (bottom right).
     * @param random - the random generator provided by floral.
     */
    public Tree(Vector2 baseCoordinates, Random random){
        this.treeBlocks = new ArrayList<>();
        this.random = random;
        createTrunk(baseCoordinates);
        createLeaves(baseCoordinates);
    }

    /* ************** Helper Functions ************** */
    /* Helper for creating the tree's trunk upon initialization. */
    private void createTrunk(Vector2 baseCoordinates){
        // general trunk setup
        this.treeHeight += this.random.nextInt(MAX_NUM_OF_BLOCKS_HEIGHT)*Block.SIZE;
        RectangleRenderable trunkBlockRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_BASE_COLOR));

        // create individual trunk blocks
        for(int i = Block.SIZE; i <= treeHeight; i+=Block.SIZE){
            Block trunkBlock = new Block(baseCoordinates.add(new Vector2(0,-i)),trunkBlockRenderable);
            trunkBlock.setTag("trunk");
            this.treeBlocks.add(trunkBlock);
        }
    }

    /* A helper function used to create the leaves and fruits on a Tree upon intialization */
    private void createLeaves(Vector2 baseCoordinates){
        // general leaves setup
        int leavesStartingY =
                (int)(baseCoordinates.y() - (this.treeHeight - LEAVES_DIMENSIONS.y()/2));
        int leavesStartingX =
                (int)(baseCoordinates.x() - LEAVES_DIMENSIONS.x()/2 + Block.SIZE/2);

        // the creation of every leaf and fruits
        for (int row = leavesStartingY - Block.SIZE;
             row >= leavesStartingY - LEAVES_DIMENSIONS.y();
             row-=Block.SIZE) {
            for (int col = leavesStartingX; col < leavesStartingX + LEAVES_DIMENSIONS.x(); col+=Block.SIZE) {
                // creation of an individual Leaf
                if (this.random.nextFloat() <= PROBABILITY_OF_LEAF_CREATION) {
                    RectangleRenderable leafBlockRenderable = new RectangleRenderable(
                            ColorSupplier.approximateColor(LEAVES_BASE_COLOR));
                    Leaf leafBlock = new Leaf(new Vector2(col,row),leafBlockRenderable);
                    leafBlock.renderer().setOpaqueness(LEAF_OPAQUENESS);
                    this.treeBlocks.add(leafBlock);
                }

                // creation of an individual Fruit
                if(this.random.nextFloat() <= PROBABILITY_OF_FRUIT_CREATION){
                    Fruit fruit = new Fruit(new Vector2(col,row));
                    this.treeBlocks.add(fruit);
                }
            }
        }
    }
}