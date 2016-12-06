package fr.mrmephisto.game1.gfx;

/**
 * Didn't get it...
 * 
 * @author MrMephisto maj 17/11/2016
 */

public class Colour {

	/*
	 * Arguments: take the 3 RGB parts (with 6 shades) of each four game colors.
	 * The numbers represent the part of the R-G-B in the color with three
	 * numbers (R-G-B) between 0 and 6 (check Game class for the 6 shades).
	 * Example: 504 is a RGB color with 5 for red shade, 0 for green shade and 4
	 * for blue shade (it is kind of a violet color). To test it, go to
	 * Photoshop and check what color match with 5*(255/5) for red, 0*(255/5)
	 * for green and 4*(255/5) for blue. The 4 arguments are from the darkest
	 * color to the brightest. Example 2: Colours.getColour(000,-1,-1,-1) is
	 * totally black
	 */
	public static int get(int colour1, int colour2, int colour3,
			int colour4) {

		// returning the color
		return (get(colour4) << 24) + (get(colour3) << 16)
				+ (get(colour2) << 8) + (get(colour1));
	}

	/*
	 * Taking the first number (of the three numbers in argument) of the
	 * argument for the red shade, the second for the green shade and the last
	 * for the blue shade Example: 302 -> 3 for R, 0 for G, 2 for B.
	 */
	private static int get(int colour) {
		// If the color is incorrect (less than 0) the color is set invisible
		if (colour < 0) {
			return 255;
		}

		// taking each number of the argument
		int r = colour / 100 % 10;
		int g = colour / 10 % 10;
		int b = colour % 10;

		// returning the 6*6*6 color
		return r * 6 * 6 + g * 6 + b;
	}
}
