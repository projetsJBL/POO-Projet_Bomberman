package fr.mrmephisto.game1.level.tiles;

/**
 * Class for animating the tiles
 * 
 * @author MrMephisto 04/12/2016 18:40
 * 
 *         The basis are simple: an animate tile is composed of several
 *         inanimate tiles which are located on the same line in the sprite
 *         sheet. They are animated with iterations that pass from one inanimate
 *         tile to another forward then backward, depending on an animation
 *         delay.
 */
public class AnimatedTile extends BasicTile {

	private int[][] animationTileCoords;
	// id of the animation
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationSwitchDelay;

	public AnimatedTile(int id, int[][] animationCoords, int tileColour,
			int levelColour, int animationSwitchDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColour,
				levelColour);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;

	}

	public void tick() {
		// if the animation has passed his delay:
		if ((System.currentTimeMillis() - lastIterationTime) >= animationSwitchDelay) {
			// updating the animation time
			lastIterationTime = System.currentTimeMillis();
			// it's time to move to the next inanimate tile. If we are at the
			// end of the proper line, we go back to its beginning with a
			// modulo.
			currentAnimationIndex = (currentAnimationIndex + 1)
					% animationTileCoords.length;
			// now we have to update the tileId for the super class
			this.tileId = (animationTileCoords[currentAnimationIndex][0] + animationTileCoords[currentAnimationIndex][1] * 32);
		}
	}

}
