import java.util.*;

public class Grass extends Tile {

	public Grass(int r, int c) { //Constructor.
		super(r, c, "grass", "light green", 15);
	}
	
	public Grass(int newR, int newC, double newWaterConcentration, double newHeat, ArrayList<Life> newLife) { //Constructor.
		super(newR, newC, newWaterConcentration, newHeat, "grass", "light green", 15, newLife);
	}
	
	public void cycle() { //Passive events with time.
		shiftWaterConcentration();
		shiftHeat();
		shiftStatus();
		burn();
		if (goldilocksCheck()) goldilocksTime++;
		else goldilocksTime--;
		lifeformCycles();
		if (toShrubbery()) {
			Tile t = new Shrubbery(r, c, waterConcentration, heat, lifeforms);
			World.setTile(r, c, t);
		}
		if (goldilocksTime < -30) toEarth();
	}
	
	public boolean goldilocksCheck() { //Goldilocks at 30% water and 40 degrees.
		return (waterConcentration > 0.30 && heat > 40);
	}
	
	public int calcMaxLife() { //Calculates max life on tile based on heat and water.
		int max = 15 + (int)(waterConcentration * 5);
		if (heat < 32) max -= 7;
		return max;
	}
	
	public boolean toShrubbery() { //Determines if shrubs will grow. Goldilocks of 90 needed.
		return (goldilocksTime > 90 + goldilocksVariance);
	}
	
}
