package fr.mrmephisto.game1.entities;

import fr.mrmephisto.game1.level.Level;
import fr.mrmephisto.game1.level.tiles.Tile;

/**
 * Abstract class Super class for...
 * 
 * @author MrMephisto 01/12/2016 11:25
 */

public abstract class Mob extends Entity {

	protected String name;
	protected int speed;
	// How far the entity can go:
	protected int numSteps = 0;
	protected boolean isMoving;
	// Which direction the entity is moving (0 to 3 for each direction)
	protected int movingDir = 1;
	protected int scale = 1;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param level
	 * @param name
	 * @param x
	 * @param y
	 * @param speed
	 */
	public Mob(Level level, String name, int x, int y, int speed) {
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	/*
	 * ACCESSORS
	 */
	public String getName() {
		return name;
	}

	/*
	 * METHODS
	 */

	/*
	 * to move the mob Fixing the numDir: 0 is up, 1 is down, 2 is left, 3 is
	 * right
	 * 
	 * @param dx is the move quantity on the x axis (positive = right, negative=
	 * left)
	 * 
	 * @param dy is the move quantity on the y axis (positive = down, negative=
	 * up)
	 */
	public void move(int dx, int dy) {
		// if the mob have to move in a diagonal way, we will ensure that he
		// will move on an axis first then on the other
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			// the number of steps he can make is decreased by one
			numSteps--;
			return;
		}

		numSteps++;

		// if a collision is not detected the mob can move
		if (!hasCollided(dx, dy)) {
			// moving up
			if (dy < 0)
				movingDir = 0;
			// moving down
			if (dy > 0)
				movingDir = 1;
			// moving left
			if (dx < 0)
				movingDir = 2;
			// moving right
			if (dx > 0)
				movingDir = 3;

			// moving the mob depending on its speed
			x += dx * speed;
			y += dy * speed;
		}

	}

	/*
	 * the collision detection depend on the specific mob. Please check the mob
	 * class.
	 */
	public abstract boolean hasCollided(int dx, int dy);

	/*
	 * Getting the Last Tile we have walking on and we will compare it with the
	 * next tile
	 */
	protected boolean isSolidTile(int dx, int dy, int x, int y) {
		// to prevent some bugs:
		if (level == null)
			return false;
		// the last tile the player was walking on:
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		// the tile he will walking on:
		Tile newTile = level.getTile((this.x + x + dx) >> 3,
				(this.y + y + dy) >> 3);
		// let's make the comparison:
		if (!lastTile.equals(newTile) && newTile.isSolid()) {
			return true;
		}
		return false;
	}
}
