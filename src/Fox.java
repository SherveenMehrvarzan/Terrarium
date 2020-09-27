
public class Fox extends Life {

	public Fox(int newR, int newC) { //Constructor.
		super("fox", newR, newC);
	}
	
	public Fox(int newR, int newC, double hun, double hea, int ag) { //Constructor.
		super("fox", newR, newC, hun, hea, ag);
	}
	
	public void cycle() { //Performed every time time changes.
		age++;
		hunger -= 0.15;
		if (hunger < 0.35) eatDeer(); //Looks to eat if hunger is below 35%.
		if (hunger > 1) hunger = 1;
		if (hunger < 0) hunger = 0;
		if (!healthCheck()) fadeToBlack(); //Dies if health isn't sufficient.
		if (movementCheck()) moveScan(); //Move to a new tile if needed.
	}
	
	public boolean movementCheck() { //Determines if movement is necessary.
		boolean move = false;
		if (hunger < 0.7 && World.getTile(r, c).deerCount() < (1 + (int)(Math.random()*3))) {
			move = true;
		}
		if (World.getTile(r, c).getStatus().equals("fire")) { 
			move = true;
		}
		return move;
	}
	
	public void moveScan() { //Determines where to move and moves.
		int newR = r;
		int newC = c;
		Tile[] nearby = World.getTile(r, c).proximityCheck();
		int counter = 0;
		while (counter < nearby.length && (nearby[counter].deerCount() == 0 || nearby[counter].getStatus().equals("fire"))) { //Prioritize land with deer.
			counter++;
		}
		if (counter >= nearby.length) counter = nearby.length - 1; //Catch last.
		if (nearby[counter].deerCount() > 0 && !nearby[counter].getStatus().equals("fire")) { //Set new destination if land has deer.
			newR = nearby[counter].r;
			newC = nearby[counter].c;
		}
		if (newR == r && newC == c) { //If there aren't nearby deer go to a random land tile. Otherwise don't move.
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
	
	public boolean healthCheck() { //Determine if fox can still survive. Update health status.
		boolean living = true;
		if (hunger < 0.25) health -= 0.2;
		else if (hunger < 0.10) health -= 0.3;
		if (World.getTile(r, c).heat < 32) health -= 0.1;
		else if (World.getTile(r, c).heat < 16) health -= 0.2;
		else if (World.getTile(r, c).heat > 120) health -= 0.1;
		else if (World.getTile(r, c).heat > 110) health -= 0.05;
		if (age / 365 > (2 + Math.random()*3)) living = false; //Checks old age.
		if (health <= 0) living = false; //Checks health.
		if (health > 1) health = 1;
		return living;
	}
	
	public void eatDeer() { //Removes a deer if available and caught, updates fox's hunger status.
		if (World.getTile(r, c).deerCount() > 0 && Math.random() + (age / 365 / 9 / 4) > 0.25) {
			Deer hunted = World.getTile(r, c).removeDeer();
			hunger += 0.25 + hunted.health * 0.25;
			health += 0.3;
		}
	}
	
}
