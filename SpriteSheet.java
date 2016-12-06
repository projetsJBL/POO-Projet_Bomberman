package fr.mrmephisto.game1.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * object class for the sprite sheet in resources
 * @author MrMephisto
 * maj 06/11/2016
 *
 */

public class SpriteSheet {
	
	public String path;
	public int width;
	public int height;
	public int [] pixels;
	
	public SpriteSheet(String path){
		//imade loading
		BufferedImage image = null;		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//attributes
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		//create transparency
		for(int i = 0; i < pixels.length; i++){
			//pixels divided by 64 because there is 255 colors (8bits) but we just need 4, and 255/4 = 64 (near)
			//in fact, 4 colors improved significantly the game performances
			pixels[i] = (pixels[i] & 0xff) / 64;
		}
	}
}
