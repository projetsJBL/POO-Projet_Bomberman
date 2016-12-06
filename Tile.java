package fr.mrmephisto.game1.level.tiles;

import fr.mrmephisto.game1.gfx.Colour;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

/**
 * Abstract class Super class for the different type of tiles
 * 
 * @author MrMephisto 04/12/2016 18:40
 */

public abstract class Tile {

	// indicate the maximum tile that we can have with the actual board
	public static final Tile[] tiles = new Tile[256];
	// HERE we set the colors of the tile AND the color corresponding to this
	// tile in the .png file for the level. VOID have here the id '0', in x=0
	// and y=0 (on the sprite sheet, please check), with black for its color and
	// correspond with the total black
	// color 000000 (so 0xFF000000 in hexa mode. Check #id color in photoshop)
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colour.get(000,
			-1, -1, -1), 0xFF000000);
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colour.get(-1,
			333, -1, -1), 0xFF555555);
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colour.get(-1, 131,
			141, -1), 0xFF00FF00);
	// the water tile is an animated tile so it's a little different: on the
	// second parameter we have to precise the positions of the inanimate tiles
	// in the sprite sheet with a 2D array BUT we also have to precise if the
	// 'movement' of the animation on the line (for water tile it's the tiles
	// 0:5, 1:5, 2:5 then 1:5 again. The animation delay is set up on 1 second
	// (1000 ms):
	public static final Tile WATER = new AnimatedTile(3, new int[][] {
			{ 0, 5 }, { 1, 5 }, { 2, 5 }, { 1, 5 } }, Colour.get(-1, 003, 224,
			-1), 0xFF0000FF, 1000);

	protected byte id;
	// for collision detection:
	protected boolean solid;
	// for light emission
	protected boolean emitter;
	// the color of the tile
	private int levelColour;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param id
	 * @param isSolid
	 * @param isEmitter
	 * @param levelColour
	 *            is about the color in the level editor (like grey for stone,
	 *            green for grass, ...)
	 */

	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColour) {
		this.id = (byte) id;
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColour = levelColour;
		// Just in case if we have created tiles where tiles are already
		// existing in tiles:
		if (tiles[id] != null)
			throw new RuntimeException("Duplicate tile on id: " + id);
		// else we proceed normally:
		tiles[id] = this;
	}

	public int getLevelColour() {
		return levelColour;
	}

	/*
	 * ACCESSORS
	 */
	public byte getId() {
		return this.id;
	}

	public boolean isSolid() {
		return this.solid;
	}

	public boolean isEmitter() {
		return this.emitter;
	}

	public abstract void tick();

	public abstract void render(Screen screen, Level level, int x, int y);

}
