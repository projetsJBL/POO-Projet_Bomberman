package fr.Bomber.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import fr.Bomber.game.entities.Ennemy;
import fr.Bomber.game.entities.Player;
import fr.Bomber.game.gfx.Screen;
import fr.Bomber.game.gfx.SpriteSheet;
import fr.Bomber.game.level.Level;

/**
 * 
 * @author JB
 *
 */

public class Game extends Canvas implements Runnable {

	/**
	 * ATTRIBUTS
	 */
	private static final long serialVersionUID = 1L;
	// size of the window
	public static final int WIDTH = 250;
	// 14:9 for resolution
	public static final int HEIGHT = WIDTH / 14 * 9;
	// sprite box adjustments
	public static final int SCALE = 3;
	// the name of the game
	public static final String NAME = "Game_v0.2";
	// frame
	private JFrame frame;
	// image
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private String spriteSheet = "/spritesheet_32x32_hero.png";
	// game colors: 6*6*6=216 colors cause we want 6 colors in each RGB
	// check init() method
	private int[] colours = new int[6 * 6 * 6];
	/*
	 * What needs to be initialized in the init() method:
	 */
	// sprite
	private Screen screen;
	// InputHandler for the keyboard
	public InputHandler input;
	// The level
	public Level level;
	public static GameEvents gameEvents;
	// The mobs
	public static Player player;

	public Ennemy ennemi1;
	public Ennemy ennemi2;
	public Ennemy ennemi3;
	// others
	public boolean running = false;
	public int tickCount = 0;

	/**
	 * CONSTRUCTOR
	 */
	public Game() {
		// sizes
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		// window
		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// type of layout: BorderLayout
		frame.setLayout(new BorderLayout());
		// center the layout
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		// use the layout
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	/**
	 * THREAD
	 */
	// initializing the screen and the colors in the sheetSprite
	// Each 4 colors of the sprite sheet will be ranges by 6 shades in a RGB
	// mode with 256 bits
	public void init() {
		// initializing the colors with an index with a RGB (256 bits) mode
		// remember: 6 colors for each R, G and B
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					// indexing red, green and blue colors
					// keeping the actual 255 colors and the 256th for the
					// invisible color (256 is the white color)
					// divided by five because we want 6 shades for each color
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					// number of bits for the range of each color (16 bits, 8
					// bits and 4 bits(by default) cause 2^4,2^3 and 2^2(by
					// default))
					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		// Initialize the screen, the inputHandler, the level, the player
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		level = new Level("/Levels/level.png");
		player = new Player(level, 0, 0, input);
		ennemi1 = new Ennemy(level, 90, 85, 1, -1, 000, 305, 210, 0);
		// ennemi2 = new Ennemy(level, 125, 200, 1, -1, 000, 225, 210, 1);
		// ennemi3 = new Ennemy(level, 190, 75, 1, -1, 000, 100, 210, 1);

		level.addEntity(player);
		level.addEntity(ennemi1);
		// level.addEntity(ennemi2);
		// level.addEntity(ennemi3);
		gameEvents = new GameEvents();
	}

	public static Player getPlayer() {
		return player;
	}

	// start the Thread of run() method
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	// run
	public void run() {

		/*
		 * Controlling the update rate of the game
		 */
		// update rate of the game
		long lastTime = System.nanoTime();
		// nanoseconds per ticks
		double nsPerTick = 1000000000D / 60D;
		// number of frames
		int frames = 0;
		// number of ticks (normally much lower than frames because of delta)
		// in fact it is FPS
		int ticks = 0;
		// how many nanoseconds so far
		long lastTimer = System.currentTimeMillis();
		// condition to update the game
		// delta equals 1 when now - lastTime equals nsPerTicks (i.e equals: 1
		// second / 60)
		double delta = 0;

		// initializing the screen with the sprite sheet
		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			// condition to render the game
			boolean shouldRender = true;

			// each time delta equals 1 (i.e each 60 frames) the game is updated
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;

				// we must rendering the game
				shouldRender = true;
			}

			// just dumb, but we want to make the frames a bit lower per second
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				// 1 second have passed so lastTimer is increased by 1000
				lastTimer += 1000;
				// let's see the frames and sticks state
				// System.out.println("frames: " + frames + ", ticks: " +
				// ticks);
				// frames and ticks are still increasing so we drop them down to
				// 0
				frames = 0;
				ticks = 0;

			}
		}
	}

	/*
	 * the game's updates such as events
	 */
	public void tick() {
		tickCount++;
		// ticking the level just in case we want to add kind of things (check
		// Level class)
		level.tick();
	}

	/*
	 * the prints of the tick() method graphics elements
	 */
	public void render() {
		// organize the data of the canvas
		BufferStrategy bs = getBufferStrategy();
		// if it doesn't exist yet, we create a new one
		if (bs == null) {
			// triple buffering
			// the higher it is, the higher the quality of animation is, but the
			// higher the resources it demands
			createBufferStrategy(3);
			return;
		}

		// Rendering the elements of the level
		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);

		level.renderTiles(screen, xOffset, yOffset);
		level.renderEntities(screen);
		level.renderBasicBomb(screen);

		// Now we have to render the screen with the colors
		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				// Picking up each color code of each pixel (what color is on
				// what pixel)
				int colourCode = screen.pixels[x + y * screen.width];
				// if the color is note the invisible color, we render it on the
				// screen
				if (colourCode < 255)
					pixels[x + y * WIDTH] = colours[colourCode];
			}
		}

		Graphics g = bs.getDrawGraphics();
		// background
		g.drawRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		new Game().start();
	}

}
