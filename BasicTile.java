package fr.mrmephisto.game1.level.tiles;

import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

/**
 * 
 * @author MrMephisto 04/12/2016 18:40
 *
 */
public class BasicTile extends Tile {

	protected int tileId;
	protected int tileColour;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param id
	 * @param x
	 *            /!\ x for the tile and not x for the pixel
	 * @param y
	 * @param tileColour
	 *            like the method Colours.get() (color of the actual tile)
	 * @param levelColour
	 *            is the index of the color for the level Tile (specially
	 *            created for the class Level, please check)
	 */
	public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, false, false, levelColour);
		this.tileId = x + y * 32;
		this.tileColour = tileColour;
	}
	
	public void tick(){
		
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileId, tileColour, 0x00, 1);
	}

}
