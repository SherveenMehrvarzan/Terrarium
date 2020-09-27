import java.util.*;

public class Shrubbery extends Tile {
	private double plantConcentration = 0.50;
	
	public Shrubbery(int r, int c) { //Constructor.
		super(r, c, "shrubbery", "green", 20);
	}
	
	public Shrubbery(int newR, int newC, double newWaterConcentration, double newHeat, ArrayList<Life> newLife) { //Constructor.
		super(newR, newC, newWaterConcentration, newHeat, "shrubbery", "green", 20, newLife);
	}
	
	public void cycle() { //Passive events with time.
		shiftWaterConcentration();
		shiftHeat();
		shiftStatus();
		burn();
		plantConcentration += growPlants();
		if (plantConcentration > 1) plantConcentration = 1;
		if (goldilocksCheck()) goldilocksTime++;
		else goldilocksTime--;
		lifeformCycles();
		if (waterConcentration > 0.50 && heat > 60 && Math.random() > 0.97 && !atLifeCapacity()) { //Deer spawn.
			addLife(new Deer(r, c));
		}
		if (deerCount() > 1 && (Math.random() - (foxCount() * 2.0 / (double)maxLife))  > 0.9 && !atLifeCapacity()) { //Fox spawn. More foxes reduces odds of fox spawn.
			addLife(new Fox(r, c));
		}
		if (toTree()) {
			Tile t = new Tree(r, c, waterConcentration, heat, lifeforms);
			World.setTile(r, c, t);
		}
		if (toGrass()) {
			Tile t = new Grass(r, c, waterConcentration, heat, lifeforms);
			World.setTile(r, c, t);
		}
	}
	
	public boolean goldilocksCheck() { //Goldilocks at 35% water and 50 degrees.
		return (waterConcentration > 0.35 && heat > 50);
	}
	
	public int calcMaxLife() { //Determines max amount of life forms.
		int max = 20 + (int)(waterConcentration * 5 + plantConcentration * 5);
		if (heat < 32) max -= 7;
		return max;
	}
	
	public void alterPlantConcentration(double num) { //Alters plant concentration.
		plantConcentration += num;
		if (plantConcentration > 1) plantConcentration = 1;
		if (plantConcentration < 0) plantConcentration = 0;
	}
	
	public double growPlants() { //Determines how many plants grow.
		double plants = 0;
		if (goldilocksTime > 3 && waterConcentration > 0.30 && heat > 40) {
			plants = Math.random()*0.50 * waterConcentration;
		}
		return plants;
	}
	
	public boolean toTree() { //Determines if trees will grow.
		return (goldilocksTime > 110 + goldilocksVariance);
	}
	
	public boolean toGrass() { //Turns the tile to grass.
		return (goldilocksTime < -25 + goldilocksVariance);
	}
	
	public double getPlantConcentration() {
		return plantConcentration;
	}
	
}
