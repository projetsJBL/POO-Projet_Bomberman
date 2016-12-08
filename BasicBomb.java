package fr.mrmephisto.game1.bombs;

import fr.mrmephisto.game1.entities.Entity;
import fr.mrmephisto.game1.gfx.Colour;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

/**
 * 
 * @author MrMephisto 07/12/2016 19:00
 *
 */

public class BasicBomb extends Entity {

	// time after the bomb explodes
	private final int delay = 1000;
	// id will be the time the bomb has been dropped
	private long id;
	private long blastTime;
	private int blastLength;

	public BasicBomb(Level level, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
		this.id = System.currentTimeMillis();
		this.blastTime = System.currentTimeMillis() + this.delay;
		this.blastLength = 50;
	}

	public long getId() {
		return id;
	}

	/********************************************************
	 * RENDERING
	 ********************************************************/
	
	private boolean exploded;
	private boolean exploding;
	
	public boolean isExploding() {
		return exploding;
	}
	
	public boolean hasExploded(){
		return exploded;
	}

	public void tick() {		
		
		//exploding
		long id = this.getId();
		if ((System.currentTimeMillis() - id) >= delay) {
			this.exploding =  true;
		} else{
			this.exploding = false;
		}
		
		//has exploded
		if ((System.currentTimeMillis() - this.blastTime) >= 700) {
			this.exploded = true;
			this.exploding = false;
		}else{
			this.exploded = false;
		}

		
	}

	/********************************************************
	 * RENDERING
	 ********************************************************/

	// explosion animation render (there's the main blast fonts and the end blast fonts)
	int mainVerticalBlast_tileId = 0;
	int endVerticalBlast_tileId = 0;
	int mainHorizontalBlast_tileId = 0;
	int endHorizontalBlast_tileId = 0;
	int[][] mainVerticalBlast_animationTileCoords = new int[][] { { 4, 11 },
			{ 5, 11 }, { 6, 11 }, { 7, 11 }, { 6, 11 }, { 5, 11 }, { 4, 11 } };
	int[][] endVerticalBlast_animationTileCoords = new int[][] { { 0, 11 },
			{ 1, 11 }, { 2, 11 }, { 3, 11 }, { 2, 11 }, { 1, 11 }, { 0, 11 } };
	int[][] mainHorizontalBlast_animationTileCoords = new int[][] { { 12, 11 },
			{ 13, 11 }, { 14, 11 }, { 15, 11 }, { 14, 11 }, { 13, 11 },
			{ 12, 11 } };
	int[][] endHorizontalBlast_animationTileCoords = new int[][] { { 8, 11 },
			{ 9, 11 }, { 10, 11 }, { 11, 11 }, { 10, 11 }, { 9, 11 }, { 8, 11 } };
	int mainVerticalBlast_currentAnimationIndex = 0;
	int endVerticalBlast_currentAnimationIndex = 0;
	int mainHorizontalBlast_currentAnimationIndex = 0;
	int endHorizontalBlast_currentAnimationIndex = 0;
	int animationSwitchDelay = 100;
	long lastIterationTime = this.getId();

	public void render(Screen screen) {
		// the bomb is dropped
		if (!exploding) {
			int bombColour = Colour.get(-1, 000, 555, 550);
			screen.render(x, y, 0 + 10 * 32, bombColour, 0x00, 1);
		//the bomb exploding
		} else if (exploding && !exploded) {
			// if the animation has passed his delay:
			if ((System.currentTimeMillis() - lastIterationTime) >= animationSwitchDelay) {
				// updating the animation time
				lastIterationTime = System.currentTimeMillis();
				// it's time to move to the next inanimate tile. If we are at
				// the
				// end of the proper line, we go back to its beginning with a
				// modulo.
				mainVerticalBlast_currentAnimationIndex = (mainVerticalBlast_currentAnimationIndex + 1)
						% mainVerticalBlast_animationTileCoords.length;
				endVerticalBlast_currentAnimationIndex = (endVerticalBlast_currentAnimationIndex + 1)
						% endVerticalBlast_animationTileCoords.length;
				mainHorizontalBlast_currentAnimationIndex = (mainHorizontalBlast_currentAnimationIndex + 1)
						% mainHorizontalBlast_animationTileCoords.length;
				endHorizontalBlast_currentAnimationIndex = (endHorizontalBlast_currentAnimationIndex + 1)
						% endHorizontalBlast_animationTileCoords.length;
				// now we have to update the tileId for the super class
				mainVerticalBlast_tileId = (mainVerticalBlast_animationTileCoords[mainVerticalBlast_currentAnimationIndex][0] + mainVerticalBlast_animationTileCoords[mainVerticalBlast_currentAnimationIndex][1] * 32);
				endVerticalBlast_tileId = (endVerticalBlast_animationTileCoords[endVerticalBlast_currentAnimationIndex][0] + endVerticalBlast_animationTileCoords[endVerticalBlast_currentAnimationIndex][1] * 32);
				mainHorizontalBlast_tileId = (mainHorizontalBlast_animationTileCoords[mainHorizontalBlast_currentAnimationIndex][0] + mainHorizontalBlast_animationTileCoords[mainHorizontalBlast_currentAnimationIndex][1] * 32);
				endHorizontalBlast_tileId = (endHorizontalBlast_animationTileCoords[endHorizontalBlast_currentAnimationIndex][0] + endHorizontalBlast_animationTileCoords[endHorizontalBlast_currentAnimationIndex][1] * 32);
			}

			int colour = Colour.get(-1, 500, 530, 550);
			for (int i = 0; i < this.blastLength; i++) {
				screen.render(x, y + i, mainVerticalBlast_tileId, colour, 0x00,
						1);
				screen.render(x, y - i, mainVerticalBlast_tileId, colour, 0x00,
						1);
				screen.render(x + i, y, mainHorizontalBlast_tileId, colour,
						0x00, 1);
				screen.render(x - i, y, mainHorizontalBlast_tileId, colour,
						0x00, 1);
			}

			screen.render(x, y + (7 + this.blastLength),
					endVerticalBlast_tileId, colour, 0x02, 1);
			screen.render(x, y - (7 + this.blastLength),
					endVerticalBlast_tileId, colour, 0x00, 1);
			screen.render(x+ (7 + this.blastLength), y ,
					endHorizontalBlast_tileId, colour, 0x00, 1);
			screen.render(x- (7 + this.blastLength), y,
					endHorizontalBlast_tileId, colour, 0x01, 1);
		}

	}

	/********************************************************
	 * MECHANICS
	 ********************************************************/

}
