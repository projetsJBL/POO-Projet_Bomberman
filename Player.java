package fr.mrmephisto.game1.entities;

import fr.mrmephisto.game1.InputHandler;
import fr.mrmephisto.game1.bombs.BasicBomb;
import fr.mrmephisto.game1.gfx.Colour;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

/**
 * 
 * @author MrMephisto 06/12/2016 22:50
 *
 */

public class Player extends Mob {

	private InputHandler input;
	// colors of the player
	private int colour = Colour.get(-1, 000, 305, 210);
	private int scale = 1;
	// mechanics
	public boolean isSwimming = false;
	private int tickCount = 0;

	// bombs

	public Player(Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input = input;
	}

	/********************************************************
	 * MECHANICS
	 ********************************************************/

	/*
	 * Warning! the collision box is a rectangle where the angles on the player
	 * are located: 1. On the top left pixel of his pants; 2. On the top right
	 * pixel of his pants; 3. On the bottom left pixel on his left foot; 4. On
	 * the bottom right pixel of his right foot. It prevents the the head is not
	 * blocked by a rock or a wall but his feet, which is more logical.
	 */
	public boolean hasCollided(int dx, int dy) {
		// first we have to delimit the collision box. It's a 4x8 rectangle
		// (check the spriteSheet)
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
		// Generating the collision box for each side of the rectangle
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(dx, dy, x, yMin))
				return true;
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(dx, dy, x, yMax))
				return true;
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(dx, dy, xMin, y))
				return true;
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(dx, dy, xMax, y))
				return true;
		}

		return false;
	}

	/********************************************************
	 * TICK
	 ********************************************************/

	public void tick() {
		// move quantity
		int dx = 0;
		int dy = 0;
		// moving the camera depending on which key is pressed
		if (input.up.isPressed()) {
			dy--;
		}
		if (input.left.isPressed()) {
			dx--;
		}
		if (input.down.isPressed()) {
			dy++;
		}
		if (input.right.isPressed()) {
			dx++;
		}
		// a bomb is posed
		if (input.space.isPressed()) {
			// the player can't drop a bomb in water
			if (!this.isSwimming) {
				BasicBomb b = new BasicBomb(level, this.x, this.y);
				this.level.addBasicBomb(b);
			}

		}

		// walking:
		if (dx != 0 || dy != 0) {
			move(dx, dy);
			isMoving = true;
		} else {
			isMoving = false;
		}

		// swimming (remember: the id of water tiles is 3):
		if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3) {
			this.isSwimming = true;
		}
		if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3) {
			this.isSwimming = false;
		}

		// Bomb.removeExplodedBombs(bombs);
		tickCount++;
	}

	/********************************************************
	 * RENDER
	 ********************************************************/

	/*
	 * rendering the player
	 */
	public void render(Screen screen) {
		int xTile = 0;
		// the player's tile is the 28th tile (by y)
		int yTile = 28;
		// Let's get the variables for animations
		// walking speed is the speed for animations
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		// we must consider the size of the mob depending on his scale (check
		// the spritesheet to look the size of the player)
		int tileModifier = 8 * scale;
		int xOffset = x - tileModifier / 2;
		// we have to decrease by 4 to reach the center of the font (because the
		// tiles are 8x8 pixels)
		int yOffset = y - tileModifier / 2 - 4;

		// ANIMATIONS
		// if the player moving down
		if (movingDir == 1) {
			// picking up the font of the player moving down (the 2nd on the
			// spriteSheet)
			xTile += 2;
			// else if the player is neither moving up or down
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}
		// if the player is swimming
		if (isSwimming) {
			int waterColour = 0;
			yOffset += 4;
			// setting colors every quarter of 60 tickCount
			if (tickCount % 60 < 15) {
				yOffset -= 1;
				waterColour = Colour.get(-1, -1, 225, -1);
			} else if (tickCount % 60 >= 15 && tickCount % 60 < 30) {
				waterColour = Colour.get(-1, 225, 115, -1);
			} else if (tickCount % 60 >= 30 && tickCount % 60 < 45) {
				waterColour = Colour.get(-1, 115, -1, 225);
			} else {
				waterColour = Colour.get(-1, 225, 115, -1);
			}
			// now rendering the font (which is originally in half in the sprite
			// sheet so we have to render it 2 times: one for one half, one for
			// mirroring it)
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00,
					1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour,
					0x01, 1);
		}

		// BOMBS

		// We have a 2x2 tiles player so we have to
		// render the 4 tiles of the font. Be careful with animations: the
		// adjustments with 'flipTop' or 'flipBottom' on the xOffset prevent a
		// bug where the player is cut in half when he is walking. first: up
		// left

		// Top of the body
		screen.render(xOffset + (tileModifier * flipTop), yOffset, xTile
				+ yTile * 32, colour, flipTop, scale);
		// up right
		screen.render(xOffset + tileModifier - (tileModifier * flipTop),
				yOffset, (xTile + 1) + yTile * 32, colour, flipTop, scale);

		// bottom of the body. If the player is swimming we render it
		// differently.
		if (!isSwimming) {
			// down left
			screen.render(xOffset + tileModifier - (tileModifier * flipBottom),
					yOffset + tileModifier, (xTile + 1) + (yTile + 1) * 32,
					colour, flipBottom, scale);
			// down right
			screen.render(xOffset + (tileModifier * flipBottom), yOffset
					+ tileModifier, xTile + (yTile + 1) * 32, colour,
					flipBottom, scale);
		}
	}
}
