
public class Deer extends Life {

	public Deer(int newR, int newC) { //Constructor.
		super("deer", newR, newC);
	}
	
	public Deer(int newR, int newC, double hun, double hea, int ag) { //Constructor.
		super("deer", newR, newC, hun, hea, ag);
	}
	
	public void cycle() {
		age++;
		hunger -= 0.1;
		if (hunger < 0.8) eatPlant(); //Eats if hunger is below 80%.
		if (hunger > 1) hunger = 1;
		if (hunger < 0) hunger = 0;
		if (!healthCheck()) fadeToBlack(); //Dies if health isn't sufficient.
		if (movementCheck()) moveScan(); //Move to a new tile if needed.
	}
	
	public boolean movementCheck() { //Determines if movement off-tile is needed.
		boolean move = false;
		if (World.getTile(r, c) instanceof Shrubbery) {
			Shrubbery s = (Shrubbery) World.getTile(r, c);
			if (s.getPlantConcentration() < (0.05 + Math.random()*0.05)) move = true;
		} else if (World.getTile(r, c) instanceof Tree) {
			Tree t = (Tree) World.getTile(r, c);
			if (t.getPlantConcentration() < (0.05 + Math.random()*0.05)) move = true;
		} else if (hunger < 0.9) {
			move = true;
		}
		if (World.getTile(r, c).waterConcentration < (0.2 + Math.random()*0.05)) {
			move = true;
		}
		if (World.getTile(r, c).getStatus().equals("fire")) {
			move = true;
		}
		return move;
	}
	
	public void moveScan() { //Determines a suitable spot to move.
		int newR = r;
		int newC = c;
		Tile[] nearby = World.getTile(r, c).proximityCheck();
		int counter = 0; //Prioritize trees and shrubbery.
		while (counter < nearby.length && (!(nearby[counter] instanceof Tree || nearby[counter] instanceof Shrubbery) || nearby[counter].getStatus().equals("fire"))) { 
			counter++;
		}
		if (counter >= nearby.length) counter = nearby.length - 1; //Catch last.
		if (nearby[counter] instanceof Shrubbery || nearby[counter] instanceof Tree && !nearby[counter].getStatus().equals("fire")) { //Set new destination if shrubbery or trees found.
			newR = nearby[counter].r;
			newC = nearby[counter].c;
		}
		if (newR == r && newC == c) { //If there aren't nearby trees or shrubbery find an earth or grass. Otherwise don't move.
			int counter2 = 0;
			int t = (int)(Math.random()*nearby.length);
			while (counter2 < nearby.length*2 && (nearby[t] instanceof Water || nearby[t].getStatus().equals("fire"))) {
				counter2++;
				t = (int)(Math.random()*nearby.length);
			}
			if (!(nearby[t] instanceof Water)) {
				newR = nearby[t].r;
				newC = nearby[t].c;
			}
		}
		traverse(newR, newC); //Travel to new tile.
	}
	
	public boolean healthCheck() { //Returns if the deer is capable of living. Changes health status as well.
		boolean living = true;
		if (hunger < 0.25) health -= 0.3;
		else if (hunger < 0.10) health -= 0.2;
		if (World.getTile(r, c).heat < 32) health -= 0.08;
		else if (World.getTile(r, c).heat < 16) health -= 0.16;
		else if (World.getTile(r, c).heat > 120) health -= 0.16;
		else if (World.getTile(r, c).heat > 110) health -= 0.08;
		if (age / 365 > (12 + Math.random()*3)) living = false; //Checks old age.
		if (health <= 0) living = false; //Checks health.
		return living;
	}
	
	public void eatPlant() { //Deer eats plant concentration if viable.
		double eat = 0;
		if (World.getTile(r, c) instanceof Shrubbery) {
			Shrubbery s = (Shrubbery) World.getTile(r, c);
			eat = Math.random()*0.02 + 0.01;
			if (eat > s.getPlantConcentration()) eat = s.getPlantConcentration();
			s.alterPlantConcentration(eat);
			if (eat > 0) health += 0.05;
		} else if (World.getTile(r, c) instanceof Tree) {
			Tree t = (Tree) World.getTile(r, c);
			eat = Math.random()*0.02 + 0.01;
			if (eat > t.getPlantConcentration()) eat = t.getPlantConcentration();
			t.alterPlantConcentration(eat);
			if (eat > 0) health += 0.05;
		}
		hunger += (eat / 0.01) * 0.07; //Hunger improved based off amount eaten.
	}
	
}
