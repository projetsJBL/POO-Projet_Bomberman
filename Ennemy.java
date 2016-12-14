package fr.Bomber.game.entities;

import fr.Bomber.comportement.Comportement;
import fr.Bomber.game.Game;
import fr.Bomber.game.bombs.BasicBomb;
import fr.Bomber.game.gfx.Colour;
import fr.Bomber.game.gfx.Screen;
import fr.Bomber.game.level.Level;

public class Ennemy extends Mob {

	// colors of the ennemy
	private int colour;
	private Comportement C;
	private int scale = 1;

	// mechanics
	public boolean isSwimming = false;
	private int tickCount = 0;

	private boolean combat;

	// bombs

	public Ennemy(Level level, int x, int y, int speed, int colour1, int colour2, int colour3, int colour4,
			Comportement C) {
		super(level, "Ennemy", x, y, speed);
		this.colour = Colour.get(colour1, colour2, colour3, colour4);
		this.C = C;

	}

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
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01, 1);
		}

		// BOMBS

		// We have a 2x2 tiles player so we have to
		// render the 4 tiles of the font. Be careful with animations: the
		// adjustments with 'flipTop' or 'flipBottom' on the xOffset prevent a
		// bug where the player is cut in half when he is walking. first: up
		// left

		// Top of the body
		screen.render(xOffset + (tileModifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale);
		// up right
		screen.render(xOffset + tileModifier - (tileModifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour,
				flipTop, scale);

		// bottom of the body. If the player is swimming we render it
		// differently.
		if (!isSwimming) {
			// down left
			screen.render(xOffset + tileModifier - (tileModifier * flipBottom), yOffset + tileModifier,
					(xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale);
			// down right
			screen.render(xOffset + (tileModifier * flipBottom), yOffset + tileModifier, xTile + (yTile + 1) * 32,
					colour, flipBottom, scale);
		}
	}

	public void tick() {

		int dx = 0;
		int dy = 0;

		int xP = Game.getPlayer().x;
		int yP = Game.getPlayer().y;

		int dist_x = xP - this.x;
		int dist_y = yP - this.y;

		long Time = System.currentTimeMillis() / 500;
		if (Math.abs(dist_x) < 40 && Math.abs(dist_y) < 40) {
			combat = true;

			if (Time % 4 == 0) {
				// System.out.println("Ils sont a côté! x: " + dist_x + " / y: "
				// + dist_y);
				// the ennemy can't drop a bomb in water
				if (!this.isSwimming) {
					BasicBomb b = new BasicBomb(level, this.x, this.y);
					this.level.addBasicBomb(b);

				}
			}
			int dir = direction();

			switch (dir) {
			case 0:
				dy--;
				break;
			case 1:
				dx--;
				break;
			case 2:
				dy++;
				break;
			case 3:
				dx++;
				break;
			case 4:
				dx = 0;
				dy = 0;
				break;
			}
		} else {
			combat = false;
			int dir = direction();

			switch (dir) {
			case 0:
				dy--;
				break;
			case 1:
				dx--;
				break;
			case 2:
				dy++;
				break;
			case 3:
				dx++;
				break;
			case 4:
				dx = 0;
				dy = 0;
				break;
			}
		}
		// walking:
		if (dx != 0 || dy != 0)

		{
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

	public int direction() {
		int dir = 0;

		int min = 0;
		int max = 1;
		long Time = System.currentTimeMillis() / 500;

		if (0 <= Time % 13 && Time % 13 < 2) {
			if (!this.C.hasChanged()) {
				this.C.setType(min + (int) (Math.random() * ((max - min) + 1)));
			}
		}

		if (this.hasCollided(this.x + 1, this.y + 1) || this.hasCollided(this.x - 1, this.y + 1)
				|| this.hasCollided(this.x + 1, this.y - 1) || this.hasCollided(this.x - 1, this.y - 1)) {
			if (!this.C.hasChanged()) {
				this.C.setType(0);
			}
		}
		if (!combat) {

			switch (this.C.getType()) {

			case 0:
				if (0 <= Time % 15 && Time % 15 < 3) {
					dir = 1;// dx--
				} else if (3 <= Time % 15 && Time % 15 < 6) {
					dir = 0;// dy--
				} else if (6 <= Time % 15 && Time % 15 < 8) {
					dir = 4;// Arret
				} else if (8 <= Time % 15 && Time % 15 < 10) {
					dir = 3;// dx++
				} else if (10 <= Time % 15 && Time % 15 < 13) {
					dir = 2; // dy++
				} else {
					dir = 4;
				}
				break;

			case 1:
				if (0 <= Time % 15 && Time % 15 < 3) {
					dir = 2;// dy++
				} else if (3 <= Time % 15 && Time % 15 < 6) {
					dir = 3;// dx++
				} else if (6 <= Time % 15 && Time % 15 < 8) {
					dir = 4;// Arret
				} else if (8 <= Time % 15 && Time % 15 < 10) {
					dir = 0;// dy--
				} else if (10 <= Time % 15 && Time % 15 < 13) {
					dir = 1;// dx--
				} else {
					dir = 4;
				}
				break;
			}
		} else {
			switch (this.C.getType()) {
			case 0:
				if (0 <= Time % 8 && Time % 8 < 2) {
					dir = 1;// dx--
				} else if (2 <= Time % 8 && Time % 8 < 4) {
					dir = 0;// dy--
				} else if (4 <= Time % 8 && Time % 8 < 6) {
					dir = 3;// dx++
				} else if (6 <= Time % 8 && Time % 8 < 7) {
					dir = 2;// dy++
				}
				break;

			case 1:
				if (0 <= Time % 8 && Time % 8 < 2) {
					dir = 3;// dx++
				} else if (2 <= Time % 8 && Time % 8 < 4) {
					dir = 2;// dy++
				} else if (4 <= Time % 8 && Time % 8 < 6) {
					dir = 1;// dx--
				} else if (6 <= Time % 8 && Time % 8 < 7) {
					dir = 0;// dy--
				}
				break;
			}
		}

		/*
		 * case 0: if (0 <= Time / 500 % 6 && Time / 500 % 6 < 3) { dir = 1;//
		 * dx++ } else if (3 <= Time / 500 % 6 && Time / 500 % 6 < 5) { dir =
		 * 3;// dx-- } break; case 1: if (0 <= Time / 500 % 6 && Time / 500 % 6
		 * < 3) { dir = 2;// dy++ } else if (3 <= Time / 500 % 6 && Time / 500 %
		 * 6 < 5) { dir = 0;// dy-- } break;
		 */

		Time = 0;
		return dir;

	}

	public Comportement getComportement() {
		return C;
	}

	public void setComportement(Comportement comportement) {
		this.C = comportement;
	}
}
