package fr.Bomber.comportement;

public class Comportement {

	private long delay = 5;
	private int type;
	private long id;

	public Comportement(int type) {
		this.id = System.currentTimeMillis();
		this.type = type;

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private boolean Change;

	public boolean hasChanged() {

		if (System.currentTimeMillis() >= delay) {
			delay = System.currentTimeMillis() + 3000;
			Change = false;
		
		} else {
			Change = true;
			
		}
		return Change;

	}
}