
public abstract class Life {
	String type;
	double hunger;
	double health;
	int age;
	int r;
	int c;
	int index;
	
	public Life(String newType, int newR, int newC) { //Constructor.
		type = newType;
		age = 0;
		hunger = 1;
		health = 1;
		r = newR;
		c = newC;
		index = World.getTile(newR, newC).getTotalLife();
	}
	
	public Life(String newType, int newR, int newC, double hun, double hea, int ag) { //Constructor.
		type = newType;
		hunger = hun;
		health = hea;
		age = ag;
		r = newR;
		c = newC;
		index = World.getTile(newR, newC).getTotalLife();
	}
	
	public abstract void cycle(); //Runs every time time shifts.
	
	public abstract boolean movementCheck(); //Determines if movement is needed.
	
	public abstract void moveScan(); //Finds a spot to move to and moves.
	
	public abstract boolean healthCheck(); //Checks if the life form is still living.
	
	public void traverse(int newR, int newC) { //Moves the life form to a new tile.
		if (this instanceof Fox) {
			World.getTile(newR, newC).addLife(new Fox(newR, newC, hunger, health, age));
			World.getTile(r, c).removeLife(index);
		} else {
			World.getTile(newR, newC).addLife(new Deer(newR, newC, hunger, health, age));
			World.getTile(r, c).removeLife(index);
		}
	}
	
	public void fadeToBlack() { //Life form leaves existence.
		World.getTile(r, c).removeLife(index);
	}
	
	public void lowerIndex() { //Lowers its position in a tile's life form arraylist.
		index = index - 1;
	}
	
}
