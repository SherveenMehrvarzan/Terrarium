import java.util.*;

public class Tree extends Tile {
	double plantConcentration;
	
	public Tree(int r, int c) { //Constructor
		super(r, c, "tree", "dark green", 25);
	}
	
	public Tree(int newR, int newC, double newWaterConcentration, double newHeat, ArrayList<Life> newLife) { //Constructor
		super(newR, newC, newWaterConcentration, newHeat, "tree", "dark green", 25, newLife);
	}
	
	public void cycle() { //Passive events with time.
		shiftWaterConcentration();
		shiftHeat();
		shiftStatus(); //Shifts local precipitation, max life, fire status, other weather status
		burn();
		plantConcentration += growPlants();
		if (plantConcentration > 1) plantConcentration = 1;
		if (goldilocksCheck()) goldilocksTime++;
		else goldilocksTime--;
		lifeformCycles();
		if (waterConcentration > 0.30 && heat > 60 && Math.random() > 0.97 && !atLifeCapacity()) { //Deer spawn.
			addLife(new Deer(r, c));
		}
		if (deerCount() > 1 && (Math.random() - (foxCount() * 2.0 / (double)maxLife))  > 0.9 && !atLifeCapacity()) { //Fox spawn. More foxes reduces odds of fox spawn.
			addLife(new Fox(r, c));
		}
		if (goldilocksTime < -40 + goldilocksVariance) toShrubbery();
		else if (goldilocksTime > 60) goldilocksTime = 60;
	}
	
	public boolean goldilocksCheck() { //Goldilocks at 25% water and 20 degrees.
		return (waterConcentration > 0.25 && heat > 20);
	}
	
	public int calcMaxLife() { //Determines max life form amount.
		int max = 25 + (int)(waterConcentration * 5 + plantConcentration * 5);
		if (heat < 32) max -= 7;
		return max; 
	}
	
	public void alterPlantConcentration(double num) { //Alter plant concentration.
		plantConcentration += num;
		if (plantConcentration > 1) plantConcentration = 1;
		if (plantConcentration < 0) plantConcentration = 0;
	}
	
	public double growPlants() { //Grow plants.
		double plants = 0;
		if (goldilocksTime > 3 && waterConcentration > 0.25 && heat > 32) {
			plants = Math.random()*0.50 * waterConcentration;
		}
		return plants;
	}
	
	public double getPlantConcentration() {
		return plantConcentration;
	}
	
	public void toShrubbery() { //Turns the tile into shrubbery.
		Tile t = new Shrubbery(r, c, waterConcentration, heat, lifeforms);
		World.setTile(r, c, t);
	}
	
}
