package fr.mrmephisto.game1;

import fr.mrmephisto.game1.bombs.BasicBomb;
import fr.mrmephisto.game1.entities.Player;
import fr.mrmephisto.game1.gfx.Colour;
import fr.mrmephisto.game1.gfx.MyFont;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

public class GameEvents {
	static long lastTime;
	private int playerHealth = 2;
	static boolean playerDead = false;

	public static int bombs = 10;

	public GameEvents() {

	}

	public void renderInterface(Screen screen, int x, int y) {
		if (!playerDead) {
			switch (playerHealth) {
			case 2:
				MyFont.render("c", screen, x, y, Colour.get(-1, 550, 500, -1),
						1);
				break;
			case 1:
				MyFont.render("h", screen, x, y, Colour.get(-1, 550, 500, -1),
						1);
				break;
			case 0:
				MyFont.render("n", screen, x, y, Colour.get(-1, 550, 500, -1),
						1);
				break;
			}
		}
	}

	public void renderEvents(Screen screen, int x, int y, InputHandler input,
			Player player, Level level) {
		if (input.space.isPressed() && !player.isSwimming && bombs > 0) {
			BasicBomb b = new BasicBomb(level, player.x, player.y);
			level.addBasicBomb(b);
			System.out.println(bombs);
		}

		// getting damages from bombs
		for (BasicBomb b : level.bombs) {
			if (b.isExploding() && (player.x == b.x || player.y == b.y)
					&& playerHealth > 0) {
				player.gettingDamage = true;
				if (System.currentTimeMillis() >= lastTime) {
					lastTime = System.currentTimeMillis() + 1000;
					playerHealth--;
					if (playerHealth <= 0)
						playerDead = true;
				} else {
					Player.gettingDamage = false;
				}
			}
		}

		if (playerDead) {
			Game.level = new Level("/game_over.png");
			MyFont.render("GAME OVER", screen, player.x - screen.width/2,
					player.y, Colour.get(-1, -1, -1, 555), 1);
		}
	}

}
