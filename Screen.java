package fr.mrmephisto.game1.gfx;

/**
 * 
 * @author MrMephisto 01/12/2016 21:30
 *
 */

public class Screen {

	/**
	 * ATTRIBUTES
	 */
	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	// mirroring the fonts (essentially making easy animations)
	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x02;

	// We will use 4 colors for the sprite sheet
	// In fact we will use different RGB shades of these colors INSIDE the game
	// but by using only 4 colors on the sheet
	// Check the Game class for this
	// public int[] colours = new int[MAP_WIDTH * MAP_WIDTH * 4];
	public int[] pixels;

	public int xOffset = 0;
	public int yOffset = 0;

	public int width;
	public int height;

	// the actual sheet is 32 tiles high and 32 tiles width
	public SpriteSheet sheet;

	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;

		// setting the colors.
		// as much as the number of pixels in the SpriteSheet.
		// check the Colours class and the size of the sprite sheet
		pixels = new int[width * height];
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/*
	 * Method used in the Font class to print the fonts. Use the second render
	 * method in this class
	 */
	/*
	 * public void render(int xPos, int yPos, int tile, int colour) {
	 * render(xPos, yPos, tile, colour, 0x00); }
	 */

	/*
	 * scrolling the sprite sheet by y then by x tiles (8x8 size), index them
	 * then affect the right pixels with the right colors. We thinking by bits
	 * and not by Integer. The 'int mirrorDir' is a indicator (from 0 to 3) for
	 * the symmetry direction of the font (compared with the original font in
	 * the spritesheet) and it has to be in the 0x00, 0x01, ... form
	 */
	public void render(int xPos, int yPos, int tile, int colour, int mirrorDir,
			int scale) {
		// start position
		xPos -= xOffset;
		yPos -= yOffset;
		// We will use a '&' logical operation to find if there is a symmetry to
		// apply on the tile
		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
		int scaleMap = scale - 1;
		// start tile. The sheet is 32*32 tiles with 8*8 pixels (so <<3, because
		// 2^3=8)
		int xTile = tile % 32;
		int yTile = tile / 32;
		// index of the actual tile
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;

		// we will pick up the color off each pixel of the sheet and put them
		// into the 'pixels' array
		for (int y = 0; y < 8; y++) {
			// y of the actual pixel
			int ySheet = y;
			// Y axis symmetry if wanted in argument
			if (mirrorY)
				ySheet = 7 - y;
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);
			for (int x = 0; x < 8; x++) {
				int xSheet = x;
				// X axis symmetry if wanted in argument
				if (mirrorX)
					xSheet = 7 - x;
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
				// picking up the color of the pixel
				int col = (colour >> (sheet.pixels[xSheet + ySheet
						* sheet.width + tileOffset] * 8)) & 255;
				// if the color is not invisible, we put it in the pixels array
				if (col < 255) {
					for (int yScale = 0; yScale < scale; yScale++) {
						// if we are beyond the sheet borders
						if (yPixel + yScale < 0 || yPixel + yScale >= height)
							continue;
						for (int xScale = 0; xScale < scale; xScale++) {
							if (xPixel + xScale < 0 || xPixel + xScale >= width)
								continue;
							pixels[(xPixel + xScale) + (yPixel + yScale)
									* width] = col;
						}
					}
				}
			}// end for x
		}// end for y
	}// end render
}
