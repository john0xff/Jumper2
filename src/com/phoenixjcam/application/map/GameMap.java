package com.phoenixjcam.application.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

/**
 * 
 * map is design in txt file as below example<br>
 * 0 0 0 0 0 <br>
 * 0 1 1 1 0 <br>
 * 0 0 0 0 0 <br>
 * 0 = walls and floors<br>
 * 1 = space to game <br>
 * 
 * @author Bart Bien
 */
public class GameMap {
    private int[][] map;
    private int mapWidth;
    private int mapHeight;

    private Tiles tilesSpec;
    private Point camera;

    // sprites
    private BufferedImage wall;
    private BufferedImage grass;
    private BufferedImage flower;
    private BufferedImage bush;
    private BufferedImage tree1;
    private BufferedImage tree2;

    public GameMap(int tilesSize) {
	tilesSpec = new Tiles();
	tilesSpec.position = new Point();
	tilesSpec.size = tilesSize;

	camera = new Point();

	loadMap();
	loadTiles();

    }

    public void loadMap() {
	try {
	    // wrong way to load map - after make JAR file can't load resource
	    // res
	    // BufferedReader bfReader = new BufferedReader(new
	    // FileReader("res/jumperMap.txt"));

	    // same as below
	    // GameMap.class.getResourceAsStream("jumperMap.txt");

	    // dynamically get map as stream
	    InputStream in = getClass()
		    .getResourceAsStream("res/jumperMap.txt");

	    BufferedReader bfReader = new BufferedReader(new InputStreamReader(
		    in));

	    // first and second line of map from txt file
	    mapWidth = Integer.parseInt(bfReader.readLine());
	    mapHeight = Integer.parseInt(bfReader.readLine());
	    map = new int[mapHeight][mapWidth];

	    String format = " ";

	    for (int row = 0; row < mapHeight; row++) {
		// read entire line from txt file
		String line = bfReader.readLine();

		// split line and put "," instead " " between each string
		String[] formatedLine = line.split(format);

		for (int column = 0; column < mapWidth; column++) {
		    map[row][column] = Integer.parseInt(formatedLine[column]);
		}
	    }
	    bfReader.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void loadTiles() {
	try {
	    wall = ImageIO.read(getClass().getResourceAsStream("res/wall.png"));
	    grass = ImageIO.read(getClass()
		    .getResourceAsStream("res/grass.png"));
	    tree1 = ImageIO.read(getClass().getResourceAsStream(
		    "res/tree-part1.png"));
	    tree2 = ImageIO.read(getClass().getResourceAsStream(
		    "res/tree-part2.png"));
	    flower = ImageIO.read(getClass().getResourceAsStream(
		    "res/flower.png"));
	    bush = ImageIO.read(getClass().getResourceAsStream("res/bush.png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public Tiles getTiles() {
	return tilesSpec;
    }

    public void setTiles(Tiles tiles) {
	this.tilesSpec = tiles;
    }

    public int getColumnTile(int x) {
	return x / tilesSpec.size;
    }

    public int getRowTile(int y) {
	return y / tilesSpec.size;
    }

    /**
     * map is design in txt file as below example<br>
     * 0 0 0 0 0 <br>
     * 0 1 1 1 0 <br>
     * 0 0 0 0 0 <br>
     * 0 = walls and floors<br>
     * 1 = space to game <br>
     */
    public int getTile(int row, int column) {
	return map[row][column];
    }

    public int getTileSize() {
	return tilesSpec.size;
    }

    public Point getCamera() {
	return camera;
    }

    public void setCamera(Point camera) {
	this.camera = camera;
    }

    public void draw(Graphics2D g) {

	for (int row = 0; row < mapHeight; row++) {
	    for (int column = 0; column < mapWidth; column++) {
		int currentTile = map[row][column];

		tilesSpec.position.x = column * tilesSpec.size;
		tilesSpec.position.y = row * tilesSpec.size;

		switch (currentTile) {
		case 0:
		    g.drawImage(wall, tilesSpec.position.x,
			    tilesSpec.position.y, null);
		    break;

		case 1:
		    g.setColor(Color.BLACK);
		    g.fillRect(tilesSpec.position.x, tilesSpec.position.y,
			    tilesSpec.size, tilesSpec.size);
		    break;

		case 2:
		    g.drawImage(grass, tilesSpec.position.x,
			    tilesSpec.position.y, null);
		    break;

		case 3:
		    g.drawImage(tree1, tilesSpec.position.x,
			    tilesSpec.position.y, null);
		    break;

		case 4:
		    g.drawImage(tree2, tilesSpec.position.x,
			    tilesSpec.position.y, null);
		    break;

		case 5:
		    g.drawImage(flower, tilesSpec.position.x,
			    tilesSpec.position.y, null);
		    break;

		case 6:
		    g.drawImage(bush, tilesSpec.position.x,
			    tilesSpec.position.y, null);
		    break;

		default:
		    break;
		}
	    }
	}
    }

    /** specify units of map */
    private class Tiles {
	/** 2D position of left corner x, y */
	private Point position;
	/** size of one tile - square (width = size, heigth = size) */
	private int size;
    }
}
