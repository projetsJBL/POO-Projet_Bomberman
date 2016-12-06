package fr.mrmephisto.game1.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import fr.mrmephisto.game1.bombs.BasicBomb;
import fr.mrmephisto.game1.entities.Entity;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.tiles.Tile;

/**
 * Level class rendering tiles for the actual level
 * 
 * @author MrMephisto 04/12/2016 04/12/2016
 */
public class Level {
	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	public List<BasicBomb> bombs = new ArrayList<BasicBomb>();
	// Image for the level
	private String imagePath;
	private BufferedImage image;

	/**
	 * @param imagePath
	 *            : the path to the level spritesheet
	 */
	public Level(String imagePath) {
		// loading the level. The size is fixing in the method called
		// loadLevelFromFile.
		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			// else we loading an empty 64x64 level by default in case of error
			this.width = 64;
			this.height = 64;
			this.tiles = new byte[width * height];
			generateLevel();
		}
	}

	public Tile getTile(int x, int y) {
		// rendering nothing if the camera is off limits
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.VOID;

		return Tile.tiles[tiles[x + y * width]];
	}

	/********************************************************
	 * LOADING LEVEL
	 ********************************************************/

	/*
	 * Loading level from a file
	 */
	private void loadLevelFromFile() {
		try {
			// getting the level image in resources
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			// dimensions:
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[width * height];
			// to finish loading we have to load the tiles (colors and stuff)
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Loading tiles like the colors and stuff
	 */
	private void loadTiles() {
		// translate all the buffer datas into 'int' values for the colors of
		// the sheet
		int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0,
				width);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// getting all the tiles (located in the tiles array of Tile
				// class which is much easier to deal with). We break the for
				// loop if t is null.
				tileCheck: for (Tile t : Tile.tiles) {
					if (t != null
							&& t.getLevelColour() == tileColours[x + y * width]) {
						// getting all the tiles of Tile class into this byte
						// list of tiles
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}

			}
		}
	}

	/********************************************************
	 * SAVING LEVEL
	 ********************************************************/

	private void saveLevelToFile() {
		try {
			// saving into a file in resources
			ImageIO.write(image, "png",
					new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/********************************************************
	 * RENDERING LEVEL
	 ********************************************************/

	public void generateLevel() {
		// The sheet is 32*32 tiles each with 8*8 pixels. x<<3 is in fact x*2^3
		// so we gonna pick up each pixel of the sheet and attribute it a color
		// (not on the screen yet)
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// the sheet contains 4 colors.
				// we will put somewhere grass and somewhere stone
				if (x * y % 10 < 8)
					tiles[x + y * width] = Tile.GRASS.getId();
				else
					tiles[x + y * width] = Tile.STONE.getId();
			}
		}
	}

	/*
	 * This method will prevent the camera go outside the tiles wich are
	 * rendered
	 * 
	 * @param xOffset and yOffset is the location of the main character (in the
	 * center of the screen)
	 */
	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		// limit for the camera
		if (xOffset < 0)
			xOffset = 0;
		if (xOffset > ((width << 3) - screen.width))
			xOffset = ((width << 3) - screen.width);
		if (yOffset < 0)
			yOffset = 0;
		if (yOffset > ((height << 3) - screen.height))
			yOffset = ((height << 3) - screen.height);

		screen.setOffset(xOffset, yOffset);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}
		}
	}

	/*
	 * To generate the entities in the level
	 */
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void renderEntities(Screen screen) {
		for (Entity entity : entities) {
			entity.render(screen);
		}
	}
	
	/*
	 * To generate bombs in level
	 */	
	public void addBasicBomb(BasicBomb bomb) {
		// if it's the first bomb the player pose it.
		if (bombs.isEmpty())
			bombs.add(bomb);
		// else we prevent a bomb to be to close to another.
		else {
			BasicBomb lastBomb = bombs.get(bombs.size() - 1);
			// the speed of bomb drop is defined by the player's agility
			if ((bomb.getId() - lastBomb.getId()) >= 200) {
				this.bombs.add(bomb);
			}
		}
					
		
	}

	public void renderBasicBomb(Screen screen) {
		for (BasicBomb b : bombs) {
			b.render(screen);
		}
	}

	/********************************************************
	 * MECANICS
	 ********************************************************/

	/*
	 * 
	 * /** just in case we want to add kind of things
	 */
	public void tick() {
		for (Tile t : Tile.tiles) {
			if (t == null)
				break;
			else
				t.tick();
		}
		
		for (Entity entity : entities) {
			// check the entities class (tick for the player is moving depending
			// on
			// the inputhanlder)
			entity.tick();
		}
		
		for(BasicBomb b : bombs){
			b.tick();
		}
		
	}

	public void alterTile(int x, int y, Tile newTile) {
		this.tiles[x + y * width] = newTile.getId();
		image.setRGB(x, y, newTile.getLevelColour());
	}

}
