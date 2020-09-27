
public class Water extends Tile {
	char current;
	
	public Water(int r, int c) { //Constructor
		super(r, c, "water", "blue", 0);
	}
	
	public void cycle() { //See tile.
		
	}
	
	public boolean goldilocksCheck() { //See tile.
		return false;
	}
	
	public int calcMaxLife() { //See tile.
		return 0;
	}
	
}
