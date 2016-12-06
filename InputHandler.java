package fr.mrmephisto.game1;

/**
 * Keyboards events
 * 
 * @author MrMephisto maj 11/11/2016
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	/**
	 * CONSTRUCTOR
	 */
	public InputHandler(Game game) {
		// click window to move bug fix he didn't add this
		game.requestFocus();
		game.addKeyListener(this);
	}

	// Key class
	public class Key {
		private int numTimesPressed = 0;
		private int numTimesReleased = 0;
		private boolean pressed = false;
		private boolean released = false;

		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}

		public boolean isPressed() {
			return pressed;
		}
		
		public boolean isReleased(){
			return released;
		}

		public void togglePressed(boolean isPressed) {
			pressed = isPressed;

			if (isPressed) {
				numTimesPressed++;
			}
		}
		
		public void toggleReleased(boolean isReleased){
			released = isReleased;
			
			if(isReleased){
				numTimesReleased++;
				numTimesPressed++;
				if(numTimesReleased > 1 || numTimesPressed > 1){
					released  = false;
					numTimesReleased = 0;
					numTimesPressed = 0;
				}
			}
			
			
		}
	}

	// key - directions
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key space = new Key();

	/**
	 * COMMON KEYLISTENER METHODS
	 */
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true, false);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false, true);
	}

	public void keyTyped(KeyEvent e) {

	}

	/**
	 * OTHER METHODS
	 */
	public void toggleKey(int KeyCode, boolean isPressed, boolean isReleased) {
		if (KeyCode == KeyEvent.VK_Z || KeyCode == KeyEvent.VK_UP
				|| KeyCode == KeyEvent.VK_NUMPAD8) {
			up.togglePressed(isPressed);
		}
		if (KeyCode == KeyEvent.VK_S || KeyCode == KeyEvent.VK_DOWN
				|| KeyCode == KeyEvent.VK_NUMPAD2) {
			down.togglePressed(isPressed);
		}
		if (KeyCode == KeyEvent.VK_Q || KeyCode == KeyEvent.VK_LEFT
				|| KeyCode == KeyEvent.VK_NUMPAD4) {
			left.togglePressed(isPressed);
		}
		if (KeyCode == KeyEvent.VK_D || KeyCode == KeyEvent.VK_RIGHT
				|| KeyCode == KeyEvent.VK_NUMPAD6) {
			right.togglePressed(isPressed);
		}
		if (KeyCode == KeyEvent.VK_SPACE) {
				space.togglePressed(isPressed);
		}
	}
}