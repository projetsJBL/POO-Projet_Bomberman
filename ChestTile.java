package fr.mrmephisto.game1.level.tiles;

public class ChestTile extends BasicTile {

	public ChestTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, x, y, tileColour, levelColour);
		this.chest = true;

	}
}