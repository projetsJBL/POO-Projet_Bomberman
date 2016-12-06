package fr.mrmephisto.game1.bombs;

import java.util.ArrayList;

import fr.mrmephisto.game1.entities.Entity;
import fr.mrmephisto.game1.gfx.Colour;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

/**
 * 
 * @author MrMephisto 06/12/2016 12:00
 *
 */

public class BasicBomb extends Entity {

	// time after the bomb explodes
	private final int delay = 1000;
	// id will be the time the bomb has been dropped
	private long id;
	

	public BasicBomb(Level level, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
		this.id = System.currentTimeMillis();
	}

	public long getId() {
		return id;
	}

	public void tick() {
	}

	public void render(Screen screen) {
		// Bomb tile is the (0,10)th tile on the sheet
		if(!this.hasExploded()){
			int bombColour = Colour.get(-1, 000, 555, 550);
			screen.render(x, y, 0 + 10 * 32, bombColour, 0x00, 1);
		}
	}

	/********************************************************
	 * MECHANICS
	 ********************************************************/

	public boolean hasExploded() {
		long id = this.getId();
		if ((System.currentTimeMillis() - id) >= delay) {
			return true;
		}

		return false;
	}

	public void removeExplodedBombs(ArrayList<BasicBomb> bombs) {
		if (!bombs.isEmpty()) {
			for (BasicBomb b : bombs){
				if (b.hasExploded()) {
					bombs.remove(b);
				}
			}
		}
	}

}
