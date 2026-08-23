package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * A class used to simulate the terrain on which our character will traverse on.
 * The terrain will be different every time the Game restarts.
 */
public class Terrain {
    /* ************** Public Constants ************** */
    /**
     * The ratio at which the ground's starting height should
     * be in relevance to the window size provided by the GameManager.
     */
    public static final float GROUND_HEIGHT_AT_0_WINDOW_RATIO = (2/3f);

    /* ************** Private Constants ************** */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final double NOISE_FACTOR = Block.SIZE * 7;
    private final NoiseGenerator noiseGenerator;
    private final int groundHeightAtX0;   // the initial height of the ground (leftmost pixel)
    private final Vector2 windowDimensions;

    /* ************** Constructor ************** */
    /**
     * Creates the Object in charge of forming the terrain (but is not the terrain itself).
     *
     * @param windowDimensions - the dimensions of the window screen provided by the GameManager.
     * @param seed - the seed used to simulate the structure of the terrain.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = (int)(windowDimensions.y() * GROUND_HEIGHT_AT_0_WINDOW_RATIO);
        this.noiseGenerator = new NoiseGenerator(seed,groundHeightAtX0);
    }

    /* ************** groundHeightAt() ************** */
    /**
     * Generates what the height of the ground at a given x coordinate.
     * @param x - the x coordinate whose ground height needs to be calculated.
     * @return the height of the ground in the given x coordinate (float)
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /* ************** CreateInRange() ************** */
    /**
     * Creates a list of blocks that will represent the ground in the game from
     * the minX coordinate provided to the maxX coordinate.
     *
     * @param minX - the min x-coordinate provided to start creating the ground from.
     * @param maxX - the max x-coordinate provided to end creating the ground at.
     * @return a list of Blocks which represent the ground in the game.
     */
    public List<Block> createInRange(int minX, int maxX) {
        // check input
        if (maxX < minX){
            int holder = maxX;
            maxX = minX;
            minX = holder;
        }

        // setup loops and list
        List<Block> blocks = new ArrayList<>();
        int distance = Math.abs(maxX-minX);
        int newMaxX = maxX + distance % Block.SIZE;

        // creates the ground blocks
        for(int x = minX; x <= newMaxX; x+=Block.SIZE){

            // the num of times ground height can be divided by SIZE rounded down (top screen closer) * SIZE
            int y = (int)(Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
            for(; y < windowDimensions.y(); y+=Block.SIZE){

                // creating a singular block and adding to list
                RectangleRenderable blockRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x,y),blockRenderable);
                block.setTag("ground");
                blocks.add(block);
            }
        }
        return blocks;
    }
}
