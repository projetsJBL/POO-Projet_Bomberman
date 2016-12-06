package fr.mrmephisto.game1.level.tiles;

/**
 * 
 * @author MrMephisto 02/12/2016 10:00
 *
 */
public class BasicSolidTile extends BasicTile{

	public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, x, y, tileColour, levelColour);
		this.solid = true;
	}

}
