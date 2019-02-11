package com.pooh.moonrocks.worlds;

import com.pooh.moonrocks.tiles.Tile;
import com.pooh.moonrocks.utils.Utils;

import java.awt.*;

public class World {

    // In terms of Tile (i.e. if the size of the map/world is 5 x 5, it'll be 5 tiles across and 5 tiles down).
    private int width, height;
    private int spawnX, spawnY;
    // We also need a way to store all of the tiles in every single position. Multi-dimensional array. We'll store it
    // using int (for each type of tile's id), not actual Tile object. Multi-dimensional as in we'll have 2 indexes for
    // the array. The rows will be called x, and the columns/height will be called y.
    private int[][] tiles;

    // constructor... 2 ways to create worlds... (1) randomly generated worlds... or (2) load in worlds from a file.
    // The second option will be the same world everytime, this is what we'll learn now. We'll need the location on our
    // computer of the file that we want to load, passed in as an argument to the constructor.
    public World(String path) {
        loadWorld(path);
    } // **** end World(String) constructor ****

    // loadWorld(String) method is responsible for getting the file that we want to load as our world and storing it in
    // the multi-dimensional array called tiles, so we'll know which tiles are in which position.
    private void loadWorld(String path) {
        // Using Utils class's loadFileAsString(String) method to store (in String called file) the location (on our
        // computer) of the world1.txt file.
        String file = Utils.loadFileAsString(path);

        // In our world1.txt file, we had split everything up either by a space or a newline.
        // What we want to do is convert all the Strings within our world1.txt into actual numbers (integers).
        // We have to split up the String that we loaded from world1.txt... we have to split up every individual number.

        // We'll take a String[] array (an array of every single number inside of world1.txt) called tokens
        // and call split on our String representation of the loaded world1.txt file (the String variable called file).

        // @@@@ To split it on any amount of white space (so a space character or a newline character) "\\s+" @@@@
        String[] tokens = file.split("\\s+");

        // What we just did was we took our file and we splitted each number into its own little String and stored each
        // of them into an index in the tokens array. That way we can access all of them separately.

        // Now we have to set the width and height of our world. (This is in terms of number of Tiles, NOT pixels.)
        width = Utils.parseInt( tokens[0] );    // The first number in our world1.txt file (aka first number in tokens).
        height = Utils.parseInt( tokens[1] );
        // Next will be WHERE the player will SPAWN at the start of the game.
        spawnX = Utils.parseInt( tokens[2] );
        spawnY = Utils.parseInt( tokens[3] );   // We just read in the first four numbers of our file (world1.txt).


        // Now every single number after this is actual world data (id numbers of Tiles).
        // We have to read all that data into the World class's int[][] tiles array.

        // Initialize the multi-dimensional int array called tiles by the world's width and height (number of Tiles).
        tiles = new int[width][height];
        // Use nested for-loops (just like in the render() method) to store the int id into the tiles array.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Here's where it gets a bit tricky. First, it has to be an integer, so use the Utils class's parseInt().

                // Parsing something from the tokens array into an int, but the tokens is a 1-D array while tiles is 2-D.
                // We have to convert the x and y for-loop position into whatever proper position it is into the proper
                // position of our tokens array.
                    // @@@@ In parentheses... (x + y * width) and that will appropriately convert the x and y of the
                    // for-loop into the 1-dimensional array index. BUT ALSO HAVE TO ADD 4 because we are setting the
                    // first 4 elements in the world1.txt file (array indexes [0], [1], [2], [3]) into variables width,
                    // height, spawnX, spawnY... they're not actual world data!!!! @@@@
                tiles[x][y] = Utils.parseInt( tokens[ (x + y * width) + 4] );
            }
        }



        /*    ****************************************************************************
        // @@@ Will code this method in the next (video18) tutorial @@@

        // The following is temporary just so we have something to work with (loading the width, height, and tiles array).
                        // @@@ FOR TESTING PURPOSES @@@
        width = 5;
        height = 5;
        tiles = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = 0;    // temporarily setting all of the tile as GrassTile (whose id is 0)
            }
        }

        ***********************************************************************************    */
    }

    // To update the position of all the tiles and things like that.
    public void tick() {

    }

    // To draw all the tiles to the screen, that way we can see the world.
    // Similar to what we did to temporarily load the world... using nested for-loops to loop through every tile.
    public void render(Graphics g) {
        // CodeNMore is starting with y first... it's suppose to prevent a few problems down the road.

        // How can we render a Tile when all we have is a bunch of int? We need another method that'll return a Tile.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // We're calling the Tile's render() method, after we get a tile from the getTile() method.

                /* getTile(x, y).render(g, x, y); */

                    // @@@@@@@@ Something went wrong @@@@@@@
                    // Since we render in terms of pixels, and our tiles are 64x64 pixels in size, all of the tiles were
                    // just drawn ontop of one another because the tiles[][] uses tile coordinates (5x5) - not pixels.

                    // So we have to convert World's render(Graphics) method's usage of
                    // Tile class's render(Graphics, int, int) method
                    // (specifically the x and y we pass for the int, int)...
                    // we have to convert these coordinates from tiles into pixels.

                // All we have to do is multiple the x coordinate by the Tile.TILE_WIDTH, and the same for y.
                getTile(x, y).render(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
            }
        }
    }

    // To be used in the render(Graphics) method. Find the id of the Tile (stored in Tile class as a static variable)
    // using the indexes x and y of the multi-dimensional tiles array, and return that Tile.
    public Tile getTile(int x, int y) {
        // Storing a Tile from the tiles array (which holds every single tile in our game) inside the Tile class (at
        // so-and-so index). We're going to index it with whatever World's tiles array is at x and at y.
        Tile t = Tile.tiles[ tiles[x][y] ];     // (1) Tile.tiles[] is the array from Tile class that stores all the
                                                // different types of tiles that exist in the game.
                                                // (2) tiles[x][y] is the array from this (World) class that stores the
                                                // id of the tile that's suppose to show up at position x and y.

        // Next we have to check whether or not it's equal to null, if it's null we'll get an error.
        // We'll return a DEFAULT tile (we're using dirt as the default) if t is equal to null.
        if (t == null) {
            return Tile.dirtTile;
        }

        return t;
    }

} // **** end World class ****