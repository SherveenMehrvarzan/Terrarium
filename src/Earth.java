import java.util.*;

public class Earth extends Tile {

	public Earth(int r, int c) { //Constructor.
		super(r, c, "earth", "tan", 10);
	}
	
	public Earth(int newR, int newC, double newWaterConcentration, double newHeat, ArrayList<Life> newLife) { //Constructor.
		super(newR, newC, newWaterConcentration, newHeat, "earth", "tan", 10, newLife);
	}
	
	public void cycle() { //Updates every time cycle.
		shiftWaterConcentration();
		shiftHeat();
		shiftStatus();
		burn();
		if (goldilocksCheck()) goldilocksTime++;
		else goldilocksTime--;
		if (goldilocksTime < 0) goldilocksTime = 0;
		lifeformCycles();
		if (toGrass()) {
			Tile t = new Grass(r, c, waterConcentration, heat, lifeforms);
			World.setTile(r, c, t);
		}
	}
	
	public boolean goldilocksCheck() { //Goldilocks at 25% water and above 32 degrees.
		return (waterConcentration > 0.25 && heat > 32);
	}
	
	public int calcMaxLife() { //Earth tiles only support 10 animals.
		return 10;
	}
	
	public boolean toGrass() { //Determines if grass will grow. Goldilocks of 21 needed.
		return (goldilocksTime >= 25 + goldilocksVariance);
	}
	
}
