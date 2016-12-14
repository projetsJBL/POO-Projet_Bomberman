package fr.mrmephisto.game1.gfx;

/**
 * 
 * @author MrMephisto 01/12/2016 21:50
 *
 */
public class MyFont {

	// the string of the fonts of the sprite sheet, including empty spaces
	private static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZchn   "
			                         + "0123456789!?%-+                 ";

	/*
	 * Take a message in arguments and pick up the right fonts to print it from
	 * the sheet to the screen
	 */
	public static void render(String msg, Screen screen, int x, int y,
			int colour, int scale) {
		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars.indexOf(msg.charAt(i));
			// rendering to the screen. Remember that each tile is 8x8 pixels.
			// The fonts start at the 30th line until the 32th line.
			if (charIndex >= 0)
				screen.render(x + (i * 8), y, charIndex + 30 * 32, colour, 0x00, scale);
		}
	}
}
