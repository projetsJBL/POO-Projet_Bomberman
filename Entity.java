package fr.mrmephisto.game1.entities;

import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

/**
 * Abstract class for entities
 * 
 * @author MrMephisto 30/11/2016
 */

public abstract class Entity {

	// x and y for the offset of the entity on the screen
	public int x, y;
	protected Level level;

	/**
	 * CONSTRUCTOR
	 * @param level
	 */
	public Entity(Level level) {
		init(level);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public final void init(Level level) {
		this.level = level;
	}
	
	/**
	 * ABSTRACT METHODS
	 */
	public abstract void tick();
	
	

	public abstract void render(Screen screen);
	
}
