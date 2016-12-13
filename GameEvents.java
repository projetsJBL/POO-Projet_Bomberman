package fr.mrmephisto.game1;

import fr.mrmephisto.game1.entities.Player;
import fr.mrmephisto.game1.gfx.Colour;
import fr.mrmephisto.game1.gfx.MyFont;
import fr.mrmephisto.game1.gfx.Screen;
import fr.mrmephisto.game1.level.Level;

public class GameEvents {
	static long lastTime;
	private int playerHealth = 2;
	static boolean playerDead = false;
	static int bullets = 0;
	public static int shotbullet = 0;
	
	public GameEvents() {
		
	}

	public void renderInterface(Screen screen, int x, int y) {		
			if(playerHealth == 2){
				//MyFont.render("c", screen, 50, 50, Colour.get(-1, 550, 500, -1), 2);
				MyFont.render("c", screen, x, y, Colour.get(-1, 550, 500, -1), 1);
			}
			/*
			switch(playerHealth){
			case 3:
				int colour = Colour.get(-1, 550, 500, -1);
				MyFont.render("ccc", screen, x, y, colour, 1);
			}
			*/
		}
	//}
	
	public void renderEvents(Screen screen, int x, int y, InputHandler input, Player player, Level level){
		
	}

}
